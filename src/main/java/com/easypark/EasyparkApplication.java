package com.easypark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.easypark.models.*;

@SpringBootApplication
@ComponentScan
public class EasyparkApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyparkApplication.class, args);

	}
}

