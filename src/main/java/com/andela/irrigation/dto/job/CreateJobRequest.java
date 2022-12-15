package com.andela.irrigation.dto.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;
import com.andela.irrigation.model.backoffice.Sensor;

import java.util.List;

/**
 * A request to start irrigation process.
 */
@Jacksonized
@Builder(toBuilder=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
@FieldNameConstants
public class CreateJobRequest {
    /**
     * Sensors ready to start irrigation.
     */
    public final List<Long> sensorIdList;

}
