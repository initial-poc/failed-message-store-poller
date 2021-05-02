package com.infogain.gcp.poc.component;

import com.infogain.gcp.poc.poller.service.MessageGroupRecordProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class FailedMessageGroupRecordPollerExector {
    private final MessageGroupRecordProcessorService messageGroupRecordProcessorService;

    @Scheduled(cron = "*/5 * * * * *")
    public void processFailedRecords() {
        log.info("Failed Record poller started at {}", LocalTime.now());
        messageGroupRecordProcessorService.processFailedRecords();
        log.info("Failed Record poller completed at {}", LocalTime.now());
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void processStuckRecords() {
        log.info("Failed Record poller started at {}", LocalTime.now());
        messageGroupRecordProcessorService.processStuckRecords();
        log.info("Failed Record poller completed at {}", LocalTime.now());
    }
}
