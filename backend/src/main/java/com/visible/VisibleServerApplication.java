package com.visible;

import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.jpf.JPFAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.*;

@SpringBootApplication
public class VisibleServerApplication {

	private static int NUMBER_OF_THREADS = 1;
	private static ExecutorService executor;
	private static JPFAdapter adapter;

	public static void main(String[] args) {
		SpringApplication.run(VisibleServerApplication.class, args);
	}

	public static SymbolicExecutor setupJPF(String fileName, String symMethod, int numArgs)
								throws ExecutionException, InterruptedException {
		executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		adapter = new JPFAdapter(fileName, symMethod, numArgs);
		Future<CountDownLatch> jpfInitialised = executor.submit(adapter);
		jpfInitialised.get().await();
		return adapter;
	}
}