package com.andela.irrigation.dto.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

/**
 * This DTO is a snapshot of current scheduler's status.
 */
@Jacksonized
@Builder(toBuilder=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
@FieldNameConstants
public class SchedulerStatusResponse {

    /**
     * No comments
     */
    public final Boolean active;

    /**
     * Indicates how many jobs are currently running.
     */
    public final Integer jobCount;
}
