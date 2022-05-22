package com.example.rsocket.demo.rest.controller;

import org.springframework.stereotype.Component;

import com.example.rsocket.demo.rest.client.RSocketClient;
import com.example.rsocket.demo.shared.BookPayload;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class BookController {

    private final RSocketClient rSocketClient;

    public Flux<BookPayload> getAllBooks() {
        return rSocketClient.getAllBooks();
    }

    public Mono<BookPayload> getBookById(String id) {
        return rSocketClient.getBookById(id);
    }

    public Mono<BookPayload> createBook(BookPayload book) {
        return rSocketClient.createBook(book);
    }

    public Mono<Void> deleteBookById(String id) {
        return rSocketClient.deleteBookById(id);
    }

}
