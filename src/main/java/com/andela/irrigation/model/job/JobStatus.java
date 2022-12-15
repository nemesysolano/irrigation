package com.andela.irrigation.model.job;

import com.andela.irrigation.dto.job.JobStatusCode;
import com.andela.irrigation.model.backoffice.Sensor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.*;
import java.util.Date;

@Jacksonized
@Builder(toBuilder=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
@FieldNameConstants
@Entity
@Table(name = "JOB_STATUS")
public class JobStatus {
    @Id
    @Column(name = "ID")
    public final String jobStatusId;
    @ManyToOne
    @JoinColumn(name = "SENSOR_ID", referencedColumnName = "ID")
    public final Sensor sensor;

    @Column(name = "START_TIME")
    public final Date startTime;

    @Column(name = "END_TIME")
    public final Date endTime;

    @Column(name = "TRIALS")
    public final int trials;

    @Enumerated(EnumType.STRING)
    @Column(name = "JOB_STATUS_CODE")
    public final JobStatusCode jobStatusCode;
}
