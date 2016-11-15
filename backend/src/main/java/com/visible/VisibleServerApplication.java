package com.visible;

import com.visible.jpf.JPFAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
public class VisibleServerApplication {

	public static void main(String[] args) {

		Executor executor = Executors.newFixedThreadPool(2);
		executor.execute(new JPFAdapter("TestTree", "maxOfThree", 3));
		SpringApplication.run(VisibleServerApplication.class, args);
	}
}
