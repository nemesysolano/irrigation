package com.andela.irrigation.repository.backoffice;

import com.andela.irrigation.model.backoffice.Sensor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * <p>Generic CRUD operations for <code>com.andela.irrigation.model.backoffice.Sensor</code> entities.</p>
 */
public interface SensorRepository  extends CrudRepository<Sensor, Long> {

    @Query("SELECT s FROM Sensor s WHERE s.plot.time = ?1 ")
    List<Sensor> findReadySensors(Date time);
}
