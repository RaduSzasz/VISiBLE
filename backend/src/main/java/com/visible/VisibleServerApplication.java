package com.visible;

import com.visible.symbolic.jpf.JPFAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;

@SpringBootApplication
public class VisibleServerApplication {

	private static int NUMBER_OF_THREADS = 1;
	private static ExecutorService executor;
	private static JPFAdapter adapter;

	public static void main(String[] args) {
		SpringApplication.run(VisibleServerApplication.class, args);
	}
}