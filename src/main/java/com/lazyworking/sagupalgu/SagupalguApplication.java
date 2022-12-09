package com.lazyworking.sagupalgu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class SagupalguApplication {

	public static void main(String[] args) {
		SpringApplication.run(SagupalguApplication.class, args);
	}

}
