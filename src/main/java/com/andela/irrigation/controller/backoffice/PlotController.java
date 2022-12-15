package com.andela.irrigation.controller.backoffice;

import com.andela.irrigation.AsyncUtils;
import com.andela.irrigation.controller.BaseController;
import com.andela.irrigation.dto.backoffice.*;
import com.andela.irrigation.model.backoffice.Plot;
import com.andela.irrigation.service.backoffice.PlotService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import org.mapstruct.factory.Mappers;


import java.util.List;

/**
 * <p>REST Interface to <code>com.andela.irrigation.service.backoffice.PlotService</code> service.</p>
 */
@RestController
@RequestMapping("backoffice/plot")
@CrossOrigin(origins = "*", maxAge=3600, methods={
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.HEAD,
        RequestMethod.OPTIONS
})
@Slf4j
public class PlotController extends BaseController {
    /**
     * <p>Mapper that converts DTOs into Plot instances.</p>
     */
    @Mapper
    public interface PlotMapper {

        /**
         * Mapper instance.
         */
        PlotMapper MAPPER = Mappers.getMapper(PlotMapper.class);

        /**
         * Converts a creation request into a plot instance.
         * @param request Creation request
         * @return Plot object
         */
        Plot toPlot(CreatePlotRequest request);

        /**
         * Converts an update request in to a plot instance.
         * @param request Update request
         * @return Plot object
         */

        Plot toPlot(UpdatePlotRequest request);

        /**
         *
         * @param plot Plot instance
         * @return Plot creation response.
         */
        CreatePlotResponse toCreateResponse(Plot plot);

        /**
         *
         * @param plot Plot instance
         * @return Plot creation response.
         */
        UpdatePlotResponse toUpdateResponse(Plot plot);

        /**
         *
         * @param plot Plot instance
         * @return Plot creation response.
         */
        GetPlotResponse toGetResponse(Plot plot);

        /**
         *
         * @param plotList List of plots
         * @return List of plot responses.
         */
        List<GetPlotResponse> toGetPlotListResponse(List<Plot> plotList);
    }

    /**
     * Wrapped CRUD service for plot entities.
     */
    PlotService plotService;

    /**
     * <p>Constructor used for dependency injection and unit tests.</p>
     * @param plotService Wrapped CRUD service for plot entities.
     */
    public PlotController(PlotService plotService) {
        this.plotService = plotService;
    }

    /**
     * <p>Creates a new plot entity</p>
     * @param request Plot creation request
     * @return Plot creation response.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePlotResponse post(@RequestBody CreatePlotRequest request) {
        Plot plot = PlotMapper.MAPPER.toPlot(request);
        return PlotMapper.MAPPER.toCreateResponse(AsyncUtils.fetchAsync(plotService.create(plot)));
    }

    /**
     *  <p>Updates an existing plot entity</p>
     * @param request
     * @param plotId
     * @return
     */
    @PutMapping("{plotId}")
    @ResponseStatus(HttpStatus.OK)
    public UpdatePlotResponse put(@RequestBody UpdatePlotRequest request, @PathVariable Long plotId) {
        Plot plot = PlotMapper.MAPPER
            .toPlot(request)
            .toBuilder()
            .plotId(plotId)
            .build();
        return PlotMapper.MAPPER.toUpdateResponse(AsyncUtils.fetchAsync(plotService.update(plot)));
    }

    /**
     * Deletes a plot entity from database.
     * @param id A non-null plot id.
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        AsyncUtils.fetchAsync(plotService.delete(id));
    }

    /**
     * Finds a plot in database
     * @param id A non-null plot id.
     */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetPlotResponse get(@PathVariable Long id) {
        return PlotMapper.MAPPER.toGetResponse(AsyncUtils.fetchAsync(plotService.findOrFail(id)));
    }
}
