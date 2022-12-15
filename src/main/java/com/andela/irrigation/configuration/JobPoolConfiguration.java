package com.andela.irrigation.configuration;
import com.andela.irrigation.service.job.JobPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * This configuration bean creates the job pool for irrigation jobs.
 */
@Configuration
public class JobPoolConfiguration {
    /**
     *  <p>Job pool for irrigation jobs</p>.
     */
    JobPool jobPool;

    @PostConstruct
    public void postConstruct() {
        jobPool = new JobPool();
    }
    @Bean
    public JobPool getJobPool() {
        return jobPool;
    }
}
