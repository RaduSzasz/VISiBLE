package com.visible;

import com.visible.jpf.JPFAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
public class VisibleServerApplication {

	private static int NUMBER_OF_THREADS = 2;

	public static void main(String[] args) {
		Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		executor.execute(new JPFAdapter("TestTree", "maxOfThree", 3));
		SpringApplication.run(VisibleServerApplication.class, args);
	}
}
