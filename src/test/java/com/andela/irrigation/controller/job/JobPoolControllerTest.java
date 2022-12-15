package com.andela.irrigation.controller.job;

import com.andela.irrigation.BaseTestSetup;
import com.andela.irrigation.dto.job.CreateJobRequest;
import com.andela.irrigation.dto.job.CreateJobResponse;
import com.andela.irrigation.dto.job.GetReadySensorsResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class JobPoolControllerTest extends BaseTestSetup {
    @ParameterizedTest
    @CsvSource({"12:00:00,8",  "13:00:00,2", "14:00:00,4", "15:00:00,7"})
    @DisplayName("Start irrigation jobs and waits they finish.")
    void testSubmitJobs(String time, final int count) throws Exception {
        String runningCountUrl = "/job/running-count";
        GetReadySensorsResponse response =  get(
            "/job/ready-sensors/" + time,
            GetReadySensorsResponse.class,
            MockMvcResultMatchers.status().isOk(),
            Map.of()
        );

        assertEquals(count, response.sensorIdList.size());

        post("/job/create",
            CreateJobRequest.builder().sensorIdList(response.sensorIdList).build(),
            CreateJobResponse.class,
            MockMvcResultMatchers.status().isOk(),
            Map.of()
        );

        Long runningCount = get(
            runningCountUrl,
            Long.class,
            MockMvcResultMatchers.status().isOk(),
            Map.of()
        );

        while(runningCount > 0) {
            runningCount = get(
                runningCountUrl,
                Long.class,
                MockMvcResultMatchers.status().isOk(),
                Map.of()
            );
        }
    }
}
