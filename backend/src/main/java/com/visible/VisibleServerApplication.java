package com.visible;

import com.visible.jpf.JPFAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class VisibleServerApplication {

	private static int NUMBER_OF_THREADS = 1;
	private static ExecutorService executor;
	private static JPFAdapter adapter;

	public static void main(String[] args) {
		executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		adapter = new JPFAdapter("TestTree", "maxOfThree", 3);
		executor.execute(adapter);
		SpringApplication.run(VisibleServerApplication.class, args);
	}

	public static void restartJPF() {
		executor.shutdownNow();
		while (!executor.isTerminated()) {
			// Do Nothing
		}
		executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		executor.execute(adapter);
	}
}
