package com.andela.irrigation.service.backoffice.impl;


import com.andela.irrigation.model.FieldError;
import com.andela.irrigation.model.backoffice.Plot;
import com.andela.irrigation.repository.backoffice.PlotRepository;
import com.andela.irrigation.service.NonExistingEntityError;
import com.andela.irrigation.service.backoffice.PlotService;
import com.andela.irrigation.service.ValidationError;
import com.andela.irrigation.service.ConflictError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
 *
 * Implements <code>PlotService</code> for details about implemented methods.
 */
@Service
public class PlotServiceImpl implements PlotService {

    /**
     *  Repository to perform CRUD operations in PLOT table.
     */
    PlotRepository plotRepository;

    /**
     * IOC Constructor
     * @param plotRepository Repository to perform CRUD operations in PLOT table.
     */
    public PlotServiceImpl(PlotRepository plotRepository) {
        this.plotRepository = plotRepository;
    }

    /**
     * <p>Validates that <code>plot</code>'s property are inside valid ranges</p>
     * @param plot Non-null plot
     * @return Same argument.
     * @throws ValidationError <code>if StringUtils.isBlank(plot.name) == true || area.doubleValue() < -1.0d</code>.
     */
    Plot expectsValidPlotForInsert(Plot plot) throws  ValidationError {
        boolean duplicatedName = false;
        Map<String, FieldError> errorMap = new HashMap<>();

        if(StringUtils.isBlank(plot.name) || plot.name.length() > MAX_PLOT_NAME_LENGTH) {
            errorMap.put(Plot.Fields.name, FieldError.TEXT_FIELD_EMPTY_OR_TOO_LARGE);
        } else if (plotRepository.findPlotByName(plot.name).isPresent()) {
            duplicatedName = true;
            errorMap.put(Plot.Fields.name, FieldError.DUPLICATED_NAME);
        }

        BigDecimal area = plot.area == null ? BigDecimal.ZERO : plot.area;
        if(area.doubleValue() < 1.0d) {
            errorMap.put(Plot.Fields.area, FieldError.NUMBER_OUT_OF_RANGE);
        }

        throwErrorIfContainsInvalidFields(errorMap, duplicatedName);
        return plot;
    }

    /**
     * <p>Validates that <code>plot</code>'s property are inside valid ranges</p>
     * @param plot Non-null plot
     * @return Same argument.
     * @throws ValidationError <code>if StringUtils.isBlank(plot.name) == true || area.doubleValue() < -1.0d</code>.
     */
    Plot expectsValidPlotForUpdate(Plot plot) throws  ValidationError {
        boolean duplicatedName = false;
        Map<String, FieldError> errorMap = new HashMap<>();

        if(StringUtils.isBlank(plot.name) || plot.name.length() > MAX_PLOT_NAME_LENGTH) {
            errorMap.put(Plot.Fields.name, FieldError.TEXT_FIELD_EMPTY_OR_TOO_LARGE);
        } else {
            Plot other = plotRepository.findPlotByName(plot.name).orElse(null);

            if(other != null && !other.plotId.equals(plot.plotId)) {
                duplicatedName = true;
                errorMap.put(Plot.Fields.name, FieldError.DUPLICATED_NAME);
            }
        }

        BigDecimal area = plot.area == null ? BigDecimal.ZERO : plot.area;
        if(area.doubleValue() < 1.0d) {
            errorMap.put(Plot.Fields.area, FieldError.NUMBER_OUT_OF_RANGE);
        }

        throwErrorIfContainsInvalidFields(errorMap, duplicatedName);
        return plot;
    }

    private void throwErrorIfContainsInvalidFields(Map<String, FieldError> errorMap, boolean duplicatedName) {
        if(errorMap.size() > 0) {
            if(duplicatedName) {
                throw new ConflictError(errorMap);
            } else {
                throw new ValidationError(errorMap);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async("asyncExecutor")
    public CompletableFuture<Plot> create(Plot plot) {
        Plot validPlot = expectsValidPlotForInsert(plot);
        return CompletableFuture.completedFuture(plotRepository.save(validPlot));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async("asyncExecutor")
    public CompletableFuture<Plot> findOrFail(Long id) {
        return CompletableFuture.completedFuture(
                plotRepository.findById(id).orElseThrow(PlotServiceImpl::nonExistingEntityError)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async("asyncExecutor")
    public CompletableFuture<Plot> delete(Long plotId) {
        Plot plot = plotRepository.findById(plotId).orElseThrow(PlotServiceImpl::nonExistingEntityError);
        plotRepository.delete(plot);
        return CompletableFuture.completedFuture(plot);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Async("asyncExecutor")
    public CompletableFuture<Plot> update(Plot plot) {
        Optional<Plot> retrievedPlot = plotRepository.findById(plot.plotId);

        if(retrievedPlot.isEmpty()) {
            throw nonExistingEntityError();
        }

        Plot updatedPlot = plotRepository.save(expectsValidPlotForUpdate(plot));
        return CompletableFuture.completedFuture(updatedPlot);
    }


    static NonExistingEntityError nonExistingEntityError() {
        return new NonExistingEntityError(Plot.class.getSimpleName());
    }
}
