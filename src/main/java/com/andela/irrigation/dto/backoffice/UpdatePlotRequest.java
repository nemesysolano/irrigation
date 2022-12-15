package com.andela.irrigation.dto.backoffice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO Object used to update new plots of land.
 */
@Jacksonized
@Builder(toBuilder=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
@FieldNameConstants
public class UpdatePlotRequest {
    /**
     * <p>Plot size in acres.</p>
     */
    public final BigDecimal area;

    /**
     * <p>Plot name for easy search in database (mut be unique)</p>
     */
    public final String name;
    /**
     * Amount of water
     */
    public final BigDecimal amount;

    /**
     * Irrigation time
     */
    public final Date time;
}

