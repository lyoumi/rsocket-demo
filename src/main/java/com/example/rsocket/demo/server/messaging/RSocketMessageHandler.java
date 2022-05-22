package com.example.rsocket.demo.server.messaging;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.example.rsocket.demo.server.entity.Book;
import com.example.rsocket.demo.server.repository.BookRepository;
import com.example.rsocket.demo.shared.BookPayload;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Controller
@Configuration
@AllArgsConstructor
public class RSocketMessageHandler {

    private final BookRepository bookRepository;

    @MessageMapping("find.all")
    public Flux<BookPayload> findAll() {
        return bookRepository.findAll()
                .map(it -> new BookPayload(it.getId(), it.getTitle(), it.getAuthor()));
    }

    @MessageMapping("find.{id}")
    public Mono<BookPayload> findById(@DestinationVariable String id) {
        return bookRepository.findById(id)
                .map(it -> new BookPayload(it.getId(), it.getTitle(), it.getAuthor()));
    }

    @MessageMapping("delete.{id}")
    public Mono<Void> deleteById(@DestinationVariable String id) {
        return bookRepository.deleteById(id);
    }

    @MessageMapping("save")
    public Mono<BookPayload> createBook(Mono<BookPayload> book) {
        return book.map(it -> new Book(it.getId(), it.getTitle(), it.getAuthor()))
                .flatMap(bookRepository::save)
                .map(it -> new BookPayload(it.getId(), it.getTitle(), it.getAuthor()));
    }


}
