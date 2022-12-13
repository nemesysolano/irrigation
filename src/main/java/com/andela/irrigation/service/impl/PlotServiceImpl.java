package com.andela.irrigation.service.impl;


import com.andela.irrigation.model.FieldError;
import com.andela.irrigation.model.Plot;
import com.andela.irrigation.repository.PlotRepository;
import com.andela.irrigation.service.NonExistingEntityError;
import com.andela.irrigation.service.PlotService;
import com.andela.irrigation.service.ServiceException;
import com.andela.irrigation.service.ValidationError;
import com.andela.irrigation.service.ConflictError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


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
    Plot expectsValidNewPlot(Plot plot) throws  ValidationError {
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
    Plot expectsValidUpdatedPlot(Plot plot) throws  ValidationError {
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
    public Plot create(Plot plot) throws ServiceException {
        Plot validPlot = expectsValidNewPlot(plot);
        return plotRepository.save(validPlot);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Plot findOrFail(Long id) throws ServiceException {
        return plotRepository.findById(id).orElseThrow(PlotServiceImpl::nonExistingEntityError);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long plotId) throws ServiceException {
        Plot plot = plotRepository.findById(plotId).orElseThrow(PlotServiceImpl::nonExistingEntityError);
        plotRepository.delete(plot);
    }

    /**
     * {@inheritDoc}
     */
    public Plot update(Plot plot) throws ServiceException {
        plotRepository
            .findById(plot.plotId)
            .orElseThrow(PlotServiceImpl::nonExistingEntityError);

        return plotRepository.save(expectsValidUpdatedPlot(plot));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Plot> findByIrrigationTime(Date time) throws ServiceException {
        return plotRepository.findPlotByTime(time);
    }

    static NonExistingEntityError nonExistingEntityError() {
        return new NonExistingEntityError(Plot.class.getSimpleName());
    }
}
