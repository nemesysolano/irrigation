package com.andela.irrigation.model.backoffice;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

/**
 * <p>A plot of land.</p>
 */
@Jacksonized @Builder(toBuilder=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
@FieldNameConstants
@Entity
@Table(name = "PLOT")
public class Plot {
    /**
     * <p>Unique plot identifier.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public final Long plotId;

    /**
     * <p>Plot size in acres.</p>
     */
    @Column(name="AREA")
    public final BigDecimal area;

    /**
     *
     */
    @Column(name="PLOT_NAME")
    public final String name;

    /**
     * Amount of water
     */
    @Column(name="AMOUNT")
    public final BigDecimal amount;

    /**
     * Irrigation time
     */
    @Column(name="IRRIGATION_TIME")
    public final Date time;
}

