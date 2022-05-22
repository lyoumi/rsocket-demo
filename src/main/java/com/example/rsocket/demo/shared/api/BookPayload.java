package com.example.rsocket.demo.shared.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookPayload {

    private String id;
    private String title;
    private String author;
}
