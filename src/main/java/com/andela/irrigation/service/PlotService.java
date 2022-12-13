package com.andela.irrigation.service;

import com.andela.irrigation.model.Plot;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * <p>This service manages <code>plots</code> lifecycle and also provides
 * query functions.</p>
 */
public interface PlotService {
    /**
     * Max length for plot name.
     */
    public static final int MAX_PLOT_NAME_LENGTH = 64;

    /**
     * <p>Creates a new plot entity</p>
     * @param plot A plot with valid data.
     * @return A new plot record containing argument's fields with new id.
     * @throws ServiceException Reports data integrity violations or system problems.
     */
    Plot create(Plot plot) throws ServiceException;


    /**
     * <p>Searches a plot instance</p>
     * @param id Plot id
     * @return The plot instance identified by <code>id</code> argument.
     * @throws ServiceException If the no instance with the given id exists
     */
    Plot findOrFail(Long id) throws ServiceException;

    /**
     * <p>Deletes a plot entity from database</p>
     * @param id A non-null plot id
     * @throws ServiceException Reports data integrity violations or system problems.
     */
    void delete(Long id) throws ServiceException;

    /**
     * <p>Updates an existing plot</p>
     * @param plot A plot instance whose <p>id</p> property matches a plot record in database.
     * @return Same instance passed as argument.
     */
    Plot update(Plot plot) throws ServiceException;

    /**
     * <p>Finds all plots ready for irrigation</p>
     * @param time Expected irrigation time
     * @return A list of plots
     */

    List<Plot> findByIrrigationTime(Date time) throws ServiceException;
}
