package com.example.rsocket.demo.rest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.rsocket.demo.rest.controller.BookController;
import com.example.rsocket.demo.shared.api.BookPayload;
import reactor.core.publisher.Mono;


@Configuration
public class WebConfiguration {

    @Bean
    public RouterFunction routerFunction(BookController bookController) {
        return RouterFunctions.route(RequestPredicates.GET("/book"), request ->
                ServerResponse.ok().body(bookController.getAllBooks(), BookPayload.class))
                .andRoute(RequestPredicates.GET("/book/{id}"), request ->
                        ServerResponse.ok().body(
                                Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(bookController::getBookById), BookPayload.class))
                .andRoute(RequestPredicates.POST("/book"), request ->
                        ServerResponse.ok().body(
                                request.bodyToMono(BookPayload.class)
                                        .flatMap(bookController::createBook), BookPayload.class))
                .andRoute(RequestPredicates.DELETE("/book/{id}"), request ->
                        ServerResponse.noContent().build(
                                Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(bookController::deleteBookById)));
    }

}
