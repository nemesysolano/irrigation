package com.andela.irrigation.repository;

import com.andela.irrigation.model.Sensor;
import org.springframework.data.repository.CrudRepository;

/**
 * <p>Generic CRUD operations for <code>com.andela.irrigation.model.Sensor</code> entities.</p>
 */
public interface SensorRepository  extends CrudRepository<Sensor, Long> {
}
