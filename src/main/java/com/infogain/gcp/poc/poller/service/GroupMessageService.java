package com.infogain.gcp.poc.poller.service;

import org.springframework.stereotype.Service;

import com.infogain.gcp.poc.poller.entity.GroupMessageStoreEntity;
import com.infogain.gcp.poc.poller.gateway.OutboxGateway;
import com.infogain.gcp.poc.poller.repository.SpannerGroupMessageStoreRepository;
import com.infogain.gcp.poc.util.RecordStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupMessageService {

	private final SpannerGroupMessageStoreRepository groupMessageStoreRepository;
	private final OutboxGateway gateway;

	public void processRecord(GroupMessageStoreEntity entity) {
		updateRecord(entity, RecordStatus.CREATED.getStatusCode());
		Mono<String> responseBody = gateway.callService(entity.buildModel());

		responseBody.doOnError(exp -> {
			log.info("on Error {}", exp.getMessage());
			updateRetryCount(entity);
			updateRecord(entity, RecordStatus.FAILED.getStatusCode());

		}).subscribe(s -> log.info("Got the response -> {}", s));
	}

	private void updateRetryCount(GroupMessageStoreEntity entity) {

		entity.setRetry_count(entity.getRetry_count() + 1);

	}

	private void updateRecord(GroupMessageStoreEntity entity, int status) {
		entity.setStatus(status);
		log.info("Going to update status for the record {}", entity);
		groupMessageStoreRepository.save(entity);
	}

}
