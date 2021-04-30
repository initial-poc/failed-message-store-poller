package com.infogain.gcp.poc.component;

import java.time.LocalTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.infogain.gcp.poc.poller.service.OutboxRecordProcessorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxFailedRecordPollerExector {
    private final OutboxRecordProcessorService pollerOutboxRecordProcessorService;

    @GetMapping("/processOutboxFailedRecords")
    public void processFailedRecords() {
        log.info("Failed Record poller started at {}", LocalTime.now());
        pollerOutboxRecordProcessorService.processFailedRecords();
        log.info("Failed Record poller completed at {}", LocalTime.now());
    }

    @GetMapping("/processOutboxStuckRecords")
    public void processStuckRecords() {
        log.info("Failed Record poller started at {}", LocalTime.now());
        pollerOutboxRecordProcessorService.processStuckRecords();
        log.info("Failed Record poller completed at {}", LocalTime.now());
    }
}
