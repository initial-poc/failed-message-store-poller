package com.infogain.gcp.poc.poller.service;

import java.net.InetAddress;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.data.spanner.core.SpannerOperations;
import org.springframework.stereotype.Service;

import com.google.cloud.spanner.Statement;
import com.google.common.base.Stopwatch;
import com.infogain.gcp.poc.poller.entity.GroupMessageStoreEntity;
import com.infogain.gcp.poc.poller.repository.SpannerGroupMessageStoreRepository;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageGroupRecordProcessorService {

    private final SpannerGroupMessageStoreRepository groupMessageStoreRepository;
    private final String ip;
private final GroupMessageService groupMessageService;
    @Value(value = "${limit}")
    private int recordLimit=10;

    private static final String GRP_MSG_STORE_FAILED_SQL =
            "SELECT * FROM group_message_store WHERE STATUS =4 and retry_count<=3";
    private static final String GRP_MSG_STORE_STUCK_RECORD_SQL =
            "SELECT * FROM group_message_store WHERE STATUS in(1,2) and TIMESTAMP_DIFF(CURRENT_TIMESTAMP,updated, MINUTE)>5";

    @Autowired
    @SneakyThrows
    public MessageGroupRecordProcessorService(SpannerGroupMessageStoreRepository spannerGroupMessageStoreRepository,GroupMessageService groupMessageService) {
        this.groupMessageStoreRepository = spannerGroupMessageStoreRepository;
        ip = InetAddress.getLocalHost().getHostAddress();
        this.groupMessageService=groupMessageService;
    }

   public void processFailedRecords() {
        doProcessFailedRecords(getRecord(GRP_MSG_STORE_FAILED_SQL));
    }

    public void processStuckRecords() {
    	doProcessFailedRecords(getRecord(GRP_MSG_STORE_STUCK_RECORD_SQL));
    }

    
    
     
    
    
    
    
    private void doProcessFailedRecords(List<GroupMessageStoreEntity> recordToProcess) {
        log.info("total record -> {} to process by application->  {}", recordToProcess.size(), ip);
        log.info("RECORD {}", recordToProcess);
        recordToProcess.stream().forEach( groupMessageService::processRecord);
    }

    
    private List<GroupMessageStoreEntity> getRecord(String sql) {
        log.info("Getting record to process by application->  {}", ip);
        Stopwatch stopWatch = Stopwatch.createStarted();
        SpannerOperations spannerTemplate = groupMessageStoreRepository.getSpannerTemplate();
        List<GroupMessageStoreEntity> recordToProcess = spannerTemplate.query(GroupMessageStoreEntity.class,
                Statement.of(String.format(sql, recordLimit)), null);
        stopWatch.stop();
        log.info("Total time taken to fetch the records {}", stopWatch);
        return recordToProcess;
    }

    

}
