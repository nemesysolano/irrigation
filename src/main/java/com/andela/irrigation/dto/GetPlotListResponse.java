package com.andela.irrigation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 *  * This DTO contains information for multiple plots of land.
 */
@Jacksonized
@Builder(toBuilder=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
@FieldNameConstants
public class GetPlotListResponse {

    /**
     * All plots matching the search criteria
     */
    public final List<GetPlotResponse> plots;
}
