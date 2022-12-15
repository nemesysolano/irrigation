package com.andela.irrigation.repository.job;

import com.andela.irrigation.dto.job.JobStatusCode;
import com.andela.irrigation.model.job.JobStatus;
import org.springframework.data.repository.CrudRepository;

public interface JobStatusRepository extends CrudRepository<JobStatus, String> {
    Long countJobStatusByJobStatusCode(JobStatusCode jobStatusCode);;
}
