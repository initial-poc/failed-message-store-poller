package com.infogain.gcp.poc.poller.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.infogain.gcp.poc.poller.domainmodel.GroupMessageStoreModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxGateway {

	private final	WebClient webClient;

	 

	public Mono<String> callService(GroupMessageStoreModel req) {
		Mono<String> response = null;

		response = webClient.post().uri("/api/pnrs").body(Mono.just(req), GroupMessageStoreModel.class).retrieve()
				.onStatus((HttpStatus::isError), (it -> handleError(it.statusCode().getReasonPhrase())))
				.bodyToMono(String.class);

		log.info("service called");
		return response;
	}

	private Mono<Throwable> handleError(String reasonPhrase) {
		 log.info("Got the error while calling the service -> {}",reasonPhrase);
		return Mono.error(RuntimeException:: new);
	}
	 
}
