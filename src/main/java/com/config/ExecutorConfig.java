package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author qbanxiaoli
 * @description
 * @create 2020-04-21 15:46
 */
@Configuration
public class ExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(50);
        threadPoolTaskExecutor.setMaxPoolSize(200);
        threadPoolTaskExecutor.setQueueCapacity(1000);
        threadPoolTaskExecutor.setThreadNamePrefix("thread-task");
        return threadPoolTaskExecutor;
    }

}