package com.andela.irrigation.repository;

import com.andela.irrigation.model.Plot;
import org.springframework.data.repository.CrudRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * <p>Generic CRUD operations for <code>com.andela.irrigation.model.Plot</code> entities.</p>
 */
public interface PlotRepository extends CrudRepository<Plot, Long> {


    /**
     *
     * @param name Plot name.
     * @return The plot whose name matches the given argument
     */

    Optional<Plot> findPlotByName(String name);

    /**
     *
     * @param time
     * @return All plots whose time field match <code>time</code>.
     */
    List<Plot> findPlotByTime(Date time);
}
