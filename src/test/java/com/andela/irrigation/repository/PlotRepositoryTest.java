package com.andela.irrigation.repository;


import com.andela.irrigation.BaseTestSetup;
import com.andela.irrigation.model.Plot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>This test validates that <code>com.andela.irrigation.repository.PlotRepository</code> CRUD properly perform
 * CRUD operations.</p>
 */
class PlotRepositoryTest extends BaseTestSetup {
    @Autowired
    PlotRepository plotRepository;

    @Test
    @DisplayName("findAll returns a non empty list.")
    void testFindAllReturnsData() {
        List<Plot> plotList =
            StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                    plotRepository.findAll().iterator(), Spliterator.ORDERED),
                    false
            )
            .collect(Collectors.toList());

        assertTrue(!plotList.isEmpty());

        for(Plot plot: plotList) {
            List<Plot> retrievedPlots = plotRepository.findPlotByTime(plot.time);
            assertTrue(retrievedPlots.size() > 0);
        }
    }
}
