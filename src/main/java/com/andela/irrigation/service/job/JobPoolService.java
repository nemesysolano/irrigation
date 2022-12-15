package com.andela.irrigation.service.job;

import com.andela.irrigation.model.backoffice.Sensor;
import com.andela.irrigation.model.job.JobStatus;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface JobPoolService {

    CompletableFuture<List<JobStatus>> submit(List<Long> sensorIdList);
    CompletableFuture<List<Sensor>> getReadySensors(Date time);

    CompletableFuture<Long> getRunningJobsCount();
}
