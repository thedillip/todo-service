package com.todo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class TodoServiceApplication {

    public static void main(String[] args) {
        log.info("***** TodoServiceApplication Started *****");
        SpringApplication.run(TodoServiceApplication.class, args);
    }

}
