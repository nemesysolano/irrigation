package com.andela.irrigation.controller.backoffice;

import com.andela.irrigation.BaseTestSetup;
import com.andela.irrigation.dto.backoffice.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static com.andela.irrigation.service.backoffice.PlotService.MAX_PLOT_NAME_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class PlotControllerTest extends BaseTestSetup {

    CreatePlotRequest createPlotRequest;

    CreatePlotResponse createPlotResponse;

    @Override
    @BeforeAll
    public void setup() throws Exception {
        super.setup();
        /* Creates a plot */
        createPlotRequest = CreatePlotRequest
            .builder()
            .area(new BigDecimal(RandomStringUtils.randomNumeric(5)))
            .name(RandomStringUtils.randomAlphabetic(12))
            .amount(BigDecimal.TEN)
            .time(new Date(System.currentTimeMillis()))
            .build();

        createPlotResponse = post("/backoffice/plot",
            createPlotRequest,
            CreatePlotResponse.class,
            MockMvcResultMatchers.status().isCreated(),
            Map.of()
        );

        assertEquals(createPlotRequest.name, createPlotResponse.name);
        assertEquals(createPlotRequest.area, createPlotResponse.area);

        log.debug(String.format("Created new Plot instance %s", createPlotResponse));
    }
    @Test
    @Order(1)
    void testRetrieve() throws Exception {

        /* Retrieves the post */
        GetPlotResponse getResponse = get(
            "/backoffice/plot/" + createPlotResponse.plotId,
            GetPlotResponse.class,
            MockMvcResultMatchers.status().isOk(),
            Map.of()
        );

        assertEquals(getResponse.plotId, createPlotResponse.plotId);

    }

    @Test
    @Order(2)
    void testUpdate() throws Exception {
        String url ="/backoffice/plot/" + createPlotResponse.plotId;

        /* Retrieves the post */
        GetPlotResponse getResponse = get(
            url,
            GetPlotResponse.class,
            MockMvcResultMatchers.status().isOk(),
            Map.of()
        );

        assertEquals(getResponse.plotId, createPlotResponse.plotId);

        UpdatePlotRequest updatePlotRequest = UpdatePlotRequest.builder()
            .name(RandomStringUtils.randomAlphanumeric(32))
            .area(new BigDecimal(RandomUtils.nextLong(5, 20)))
            .amount(BigDecimal.TEN)
            .time(new Date(System.currentTimeMillis()))
            .build();


        UpdatePlotResponse updatePlotResponse = put(
            url,
            updatePlotRequest,
            UpdatePlotResponse.class,
            MockMvcResultMatchers.status().isOk(),
            Map.of()
        );

        assertEquals(updatePlotRequest.area, updatePlotResponse.area);
        assertNotEquals(getResponse.area, updatePlotResponse.area);
        assertEquals(updatePlotRequest.name, updatePlotResponse.name);
        assertNotEquals(getResponse.name, updatePlotResponse.name);

    }

    @Test
    @Order(3)
    void testValidation() throws Exception {
        final String CONFLICTING_NAME = "2 SQUARE ACRES";
        /*
         * Validates:
         *  1. Name is not empty.
         *  2. Area greater than 1.
         */
        ErrorResponse response = post("/backoffice/plot",
                CreatePlotRequest
                        .builder()
                        .area(BigDecimal.ZERO)
                        .name(StringUtils.EMPTY)
                        .build(),
                ErrorResponse.class,
                MockMvcResultMatchers.status().isBadRequest(),
                Map.of()
        );

        assertNotNull(response.errorMap);
        assertTrue(response.errorMap.size() > 0);
        assertNotNull(response.errorMap.get(CreatePlotRequest.Fields.name));
        assertNotNull(response.errorMap.get(CreatePlotRequest.Fields.area));

        /*
         * Validates:
         *  1. Name length is less than MAX_PLOT_NAME_LENGTH
         */
        response = post("/backoffice/plot",
                CreatePlotRequest
                        .builder()
                        .area(BigDecimal.ONE)
                        .name(RandomStringUtils.randomAlphanumeric(MAX_PLOT_NAME_LENGTH+10))
                        .build(),
                ErrorResponse.class,
                MockMvcResultMatchers.status().isBadRequest(),
                Map.of()
        );

        assertNotNull(response.errorMap);
        assertEquals(1,response.errorMap.size());
        assertNotNull(response.errorMap.get(CreatePlotRequest.Fields.name));

        /*
        * Validates:
        * 1. Can't duplicate names.
        */

        post("/backoffice/plot",
            CreatePlotRequest
                    .builder()
                    .area(BigDecimal.TEN)
                    .name(CONFLICTING_NAME)
                    .build(),
            ErrorResponse.class,
            MockMvcResultMatchers.status().isConflict(),
            Map.of()
        );

        put(
            "/backoffice/plot/" + createPlotResponse.plotId,
            UpdatePlotRequest.builder().name(CONFLICTING_NAME).area(createPlotResponse.area).build(),
            UpdatePlotResponse.class,
            MockMvcResultMatchers.status().isConflict(),
            Map.of()
        );
    }

    @AfterAll
    public void cleanup() throws Exception {
        /* Deletes the plot */
        delete("/backoffice/plot/" + createPlotResponse.plotId, MockMvcResultMatchers.status().isNoContent(), Map.of());

        /*
         * Confirms that the plot was deleted
         */
        get(
            "/backoffice/plot/" + createPlotResponse.plotId,
            GetPlotResponse.class,
            MockMvcResultMatchers.status().isNotFound(),
            Map.of()
        );
    }
}
