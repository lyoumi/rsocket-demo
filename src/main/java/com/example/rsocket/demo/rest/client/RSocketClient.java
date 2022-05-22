package com.example.rsocket.demo.rest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.stereotype.Component;

import com.example.rsocket.demo.shared.api.BookPayload;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class RSocketClient {

    private final RSocketRequester rsocketRequester;


    @Autowired
    public RSocketClient(
            RSocketRequester.Builder builder,
            @Qualifier("rSocketStrategies") RSocketStrategies strategies,
            @Value("${book.server.port}") Integer auditServerPort,
            @Value("${book.server.host}") String auditServerHost) {
        this.rsocketRequester = builder
                .rsocketConnector(connector -> connector.acceptor(
                        RSocketMessageHandler.responder(strategies, new ClientHandler())))
                .tcp(auditServerHost, auditServerPort);
    }

    public Mono<Void> deleteBookById(String id) {
        return rsocketRequester.route("delete.{id}", id)
                .retrieveMono(Void.TYPE)
                .doOnSuccess(unused -> log.info("Incoming delete response for {}", id));
    }

    public Mono<BookPayload> createBook(BookPayload book) {
        return rsocketRequester.route("save")
                .data(book)
                .retrieveMono(BookPayload.class)
                .doOnSuccess(it -> log.info("Incoming payload {}", it));
    }

    public Mono<BookPayload> getBookById(String id) {
        return rsocketRequester.route("find.{id}", id)
                .retrieveMono(BookPayload.class)
                .doOnSuccess(it -> log.info("Incoming payload {}", it));
    }

    public Flux<BookPayload> getAllBooks() {
        return rsocketRequester.route("find.all")
                .retrieveFlux(BookPayload.class)
                .doOnEach(it -> log.info("Incoming payload chunk {}", it));
    }

    @Slf4j
    static class ClientHandler {

        @MessageMapping("client-status")
        public Flux<String> statusUpdate(String status) {
            log.info("Connection {}", status);
            return Flux.interval(Duration.ofSeconds(5)).map(index -> String.valueOf(Runtime.getRuntime().freeMemory()));
        }

    }

}
