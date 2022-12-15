package com.andela.irrigation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

/**
 * This configuration bean creates and setup the Executor object used by asynchronous services called by controllers.
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {

    /**
     * We multiply <code></code>Runtime.getRuntime().availableProcessors()</code> by this value to set the max pool size.
     */
    private Integer maxPoolSizeFactor = 8;

    /**
     * Share executor instance.
     */
    ThreadPoolTaskExecutor executor;

    @PostConstruct
    public void postConstruct() {
        int poolSize = Runtime.getRuntime().availableProcessors();
        int maxPoolSize = poolSize * maxPoolSizeFactor;

        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
    }
    /**
     * Executor object used by asynchronous services called by controllers
     * @return New executor Object.
     */
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor()
    {
        return executor;
    }
}