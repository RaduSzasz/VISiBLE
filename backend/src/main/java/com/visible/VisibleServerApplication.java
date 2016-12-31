package com.visible;

import com.visible.jpf.JPFAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class VisibleServerApplication {

	private static int NUMBER_OF_THREADS = 1;
	private static ExecutorService executor;
	private static JPFAdapter adapter;

	public static void main(String[] args) {
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

	public static void setupJPF(String fileName, String symMethod, int numArgs) throws InterruptedException {
		executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		CountDownLatch latch = new CountDownLatch(1);
		adapter = new JPFAdapter(fileName, symMethod, numArgs, latch);
		executor.execute(adapter);

		latch.await();
	}
}
