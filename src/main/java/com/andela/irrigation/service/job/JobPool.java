package com.andela.irrigation.service.job;
import com.andela.irrigation.model.job.JobStatus;
import com.andela.irrigation.repository.job.JobStatusRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class JobPool {
    Integer poolSize;
    Lock lock = new ReentrantLock();
    ExecutorService executorService;

    boolean active;

    public JobPool(Integer poolSize) {
        this.poolSize = poolSize;
        initializeThreadPool();
    }

    public JobPool() {
        this(Runtime.getRuntime().availableProcessors()*2);
    }

    protected void initializeThreadPool() {
        executorService = Executors.newFixedThreadPool(poolSize);
    }

    public void start() {
        lock.lock();
        try {
            active = true;
        } finally {
            lock.unlock();
        }
    }

    public void submit(List<JobStatus> jobStatusList, JobStatusRepository jobStatusRepository) {
        lock.lock();
        try {
            if(active) {
                jobStatusList.forEach(jobStatus -> executorService.submit(new Job(
                    jobStatus.jobStatusId,
                    jobStatus.startTime,
                    jobStatus.trials,
                    jobStatus.sensor,
                    jobStatus.jobStatusCode,
                    jobStatusRepository
                )));
            }
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        lock.lock();
        try {
            active = false;
            executorService.shutdownNow();
            executorService.awaitTermination(60, TimeUnit.MINUTES);
        } catch (InterruptedException cause) {
            log.debug("Shutdown process terminated abruptly", cause);
        } finally {
            lock.unlock();
        }
    }
}
