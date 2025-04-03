package com.example.demo.batch;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class SchedulingConfig {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importProductJob;

    @Scheduled(fixedRate = 300000)
    public void runBatchJob() {
        try {
            jobLauncher.run(importProductJob, new JobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
