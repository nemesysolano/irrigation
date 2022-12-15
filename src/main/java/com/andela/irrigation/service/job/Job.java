package com.andela.irrigation.service.job;

import com.andela.irrigation.dto.job.JobStatusCode;
import com.andela.irrigation.model.backoffice.Sensor;
import com.andela.irrigation.model.job.JobStatus;
import com.andela.irrigation.repository.job.JobStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.util.Date;
import java.util.concurrent.Callable;


@Slf4j
public class Job implements Callable<JobStatusCode> {

    Sensor sensor;

    /**
     * Start time
     */
    Date startTime;

    /**
     * End time
     */
    Date endTime;

    /**
     * Trials
     */
    int trials = 1;

    JobStatusCode jobStatusCode;

    String jobStatusId;

    JobStatusRepository jobStatusRepository;

    public Job(
        String jobStatusId,
        Date startTime,
        int trials,
        Sensor sensor,
        JobStatusCode jobStatusCode,
        JobStatusRepository jobStatusRepository
    ) {
        this.sensor = sensor;
        this.jobStatusId = jobStatusId;
        this.startTime = startTime;
        this.trials = trials;
        this.jobStatusCode = jobStatusCode;
        this.jobStatusRepository = jobStatusRepository;
    }

    private void updateStatus(JobStatusCode jobStatusCode, int trials) {
        this.trials = trials;
        this.endTime = new Date();
        this.jobStatusCode = jobStatusCode;

        JobStatus jobStatus = jobStatusRepository.findById(this.jobStatusId).get();
        JobStatus updatedJobStatus = jobStatus.toBuilder()
                .jobStatusCode(this.jobStatusCode)
                .trials(this.trials)
                .endTime(this.endTime)
                .build();

        jobStatusRepository.save(updatedJobStatus);

    }

    @Override
    public JobStatusCode call() throws Exception {
        int trialsBeforeFailing = RandomUtils.nextInt(0, 10);
        int index = 0;
        log.debug(String.format("Starting job for sensor #%d.", this.sensor.sensorId));

        try {
            updateStatus(JobStatusCode.RUNNING, index + 1);

            if(trialsBeforeFailing % 2 == 0) {
                for( index = 0; index < trialsBeforeFailing; index++) {
                    Thread.sleep(1000);
                }

                updateStatus(JobStatusCode.REACHED_MAX_TRIALS, index);
            } else {
                updateStatus(JobStatusCode.SUCCESS, index + 1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            updateStatus(JobStatusCode.CANCELED, index + 1);
        }

        log.debug(String.format("Job for sensor #%d finished '%s' status.", this.sensor.sensorId, this.jobStatusCode));
        endTime = new Date();

        return jobStatusCode;
    }

    public JobStatusCode getJobStatusCode() {
        return jobStatusCode;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getTrials() {
        return trials;
    }

    public String getJobStatusId() {
        return jobStatusId;
    }


}
