package com.sunghyun.todoapp;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

import com.auth0.jwt.JWT;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TodoappApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodoappApplication.class, args);
	}
}
