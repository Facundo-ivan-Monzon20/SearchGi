package com.searchgi.SearchGi.services;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleService {
    private final SpiderService spiderService;

    public ScheduleService(SpiderService spiderService) {
        this.spiderService = spiderService;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleIndexWebPages(){
        spiderService.indexWebPages();
    }
}
