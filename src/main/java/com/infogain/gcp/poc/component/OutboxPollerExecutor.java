package com.infogain.gcp.poc.component;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.infogain.gcp.poc.poller.service.OutboxRecordProcessorService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Component
public class OutboxPollerExecutor {

    @Autowired
    private OutboxRecordProcessorService pollerOutboxRecordProcessorService;

    @GetMapping("/processOutboxRecords")
    public void process() {
        log.info("poller started at {}", LocalTime.now());
        pollerOutboxRecordProcessorService.processRecords();
        log.info("poller completed at {}", LocalTime.now());
    }

}