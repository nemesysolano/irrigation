package com.andela.irrigation.controller;

import com.andela.irrigation.dto.*;
import com.andela.irrigation.model.Plot;
import com.andela.irrigation.service.PlotService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * <p>REST Interface to <code>com.andela.irrigation.service.PlotService</code> service.</p>
 */
@RestController
@RequestMapping("plot")
@CrossOrigin(origins = "*", maxAge=3600, methods={
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.HEAD,
        RequestMethod.OPTIONS
})
@Slf4j
public class PlotController {
    private static SimpleDateFormat TIME_FORMTTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * <p>Mapper that converts DTOs into Plot instances.</p>
     */
    @Mapper
    public static interface PlotMapper {

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
        return PlotMapper.MAPPER.toCreateResponse(plotService.create(plot));
    }

    @PutMapping("{plotId}")
    @ResponseStatus(HttpStatus.OK)
    public UpdatePlotResponse put(@RequestBody UpdatePlotRequest request, @PathVariable Long plotId) {
        Plot plot = PlotMapper.MAPPER
            .toPlot(request)
            .toBuilder()
            .plotId(plotId)
            .build();
        return PlotMapper.MAPPER.toUpdateResponse(plotService.update(plot));
    }

    /**
     * Deletes a plot entity from database.
     * @param id A non-null plot id.
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        plotService.delete(id);
    }

    /**
     * Finds a plot in database
     * @param id A non-null plot id.
     */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetPlotResponse get(@PathVariable Long id) {
        return PlotMapper.MAPPER.toGetResponse(plotService.findOrFail(id));
    }

    @GetMapping("ready/{time}")
    @ResponseStatus(HttpStatus.OK)
    public GetPlotListResponse ready(
            @PathVariable("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time
    ) {
        Date dateTime =timeToDateTime(time);

        List<Plot> plotList = plotService.findByIrrigationTime(dateTime);
        return GetPlotListResponse.builder().plots(
            PlotMapper.MAPPER.toGetPlotListResponse(plotList)
        ).build();
    }

    private  Date timeToDateTime(LocalTime time) {
        try {
            return TIME_FORMTTER.parse(
                String.format("1970-01-01 0%2d:%02d:%02d",
                        time.getHour(),
                        time.getMinute(),
                        time.getSecond()
                ));
        } catch (ParseException e) {
            return null;
        }
    }
}
