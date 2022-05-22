package com.example.rsocket.demo.server.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.rsocket.demo.server.entity.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

}
