package com.andela.irrigation.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

/**
 * <p>An irrigation sensor.</p>
 */
@Jacksonized @Builder(toBuilder=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
@FieldNameConstants
@Entity
@Table(name = "SENSOR")
public class Sensor {
    /**
     * <p>Unique sensor identifier.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public final Long sensorId;

    /**
     * Plot where this sensor is deployed
     */
    @ManyToOne
    @JoinColumn(name="PLOT_ID", referencedColumnName = "ID")
    public final Plot plot;
}
