package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/clients
*/
@SpringBootApplication
public class WebServerApp {

    public static void main(String[] args) {
        SpringApplication.run(WebServerApp.class, args);
    }
}
