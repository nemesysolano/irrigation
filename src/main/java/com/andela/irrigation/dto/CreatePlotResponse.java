package com.andela.irrigation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * DTO Object containing data for the recently created plot of land
 */
@Jacksonized
@Builder(toBuilder=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
@FieldNameConstants
public class CreatePlotResponse {

    /**
     * * <p>Unique plot identifier.</p>
     */
    public final Long plotId;

    /**
     * <p>Plot size in acres.</p>
     */
    public final BigDecimal area;

    /**
     * <p>Plot name for easy search in database (mut be unique).</p>
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
