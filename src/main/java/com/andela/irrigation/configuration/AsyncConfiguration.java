package com.andela.irrigation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {
    @Value("${spring.async.maxPoolSizeFactor}")
    Integer maxPoolSizeFactor;

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor()
    {
        int poolSize = Runtime.getRuntime().availableProcessors();
        int maxPoolSize = poolSize * maxPoolSizeFactor;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }
}