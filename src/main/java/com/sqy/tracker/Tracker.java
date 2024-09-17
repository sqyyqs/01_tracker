package com.sqy.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Tracker {
    public static void main(String[] args) {
        SpringApplication.run(Tracker.class, args);
    }
}
