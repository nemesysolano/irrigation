package com.andela.irrigation.service.job.impl;

import com.andela.irrigation.dto.job.JobStatusCode;
import com.andela.irrigation.model.backoffice.Sensor;
import com.andela.irrigation.model.job.JobStatus;
import com.andela.irrigation.repository.backoffice.SensorRepository;
import com.andela.irrigation.repository.job.JobStatusRepository;
import com.andela.irrigation.service.NonExistingEntityError;
import com.andela.irrigation.service.job.JobPool;
import com.andela.irrigation.service.job.JobPoolService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class JobServiceImpl implements JobPoolService {
    JobStatusRepository jobStatusRepository;
    SensorRepository sensorRepository;
    JobPool jobPool;

    public JobServiceImpl (
            JobStatusRepository jobStatusRepository, SensorRepository sensorRepository, JobPool jobPool
    ) {
        this.jobStatusRepository = jobStatusRepository;
        this.sensorRepository = sensorRepository;
        this.jobPool = jobPool;
    }

    @PostConstruct
    public void postConstruct() {
        jobPool.start();
    }

    @PreDestroy
    public void preDestroy() {
        jobPool.stop();
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<List<JobStatus>> submit(List<Long> sensorIdList) {
        List<JobStatus> jobStatusList = sensorIdList.parallelStream()
            .map(sensorId -> jobStatusRepository.save(
                JobStatus.builder()
                .jobStatusId(
                    UUID.randomUUID().toString()+':'+ RandomStringUtils.randomAlphanumeric(24)
                )
                .startTime(new Date())
                .trials(0)
                .sensor(sensorRepository.findById(sensorId).orElseThrow(
                    () -> new NonExistingEntityError(Sensor.class.getSimpleName()))
                )
                .jobStatusCode(JobStatusCode.SCHEDULED)
                .build()
        )).collect(Collectors.toList());

        this.jobPool.submit(jobStatusList, jobStatusRepository);

        return CompletableFuture.completedFuture(jobStatusList);
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<List<Sensor>> getReadySensors(Date time) {
        return CompletableFuture.completedFuture(sensorRepository.findReadySensors(time));
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<Long> getRunningJobsCount() {
        return CompletableFuture.completedFuture(
            jobStatusRepository.countJobStatusByJobStatusCode(JobStatusCode.RUNNING)
        );
    }
}
