package com.visible;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.visible.jpf.JPFAdapter;

@SpringBootApplication
public class VisibleServerApplication {

	public static void main(String[] args) {
		JPFAdapter.runJPF("TestTree", "maxOfThree", 3);
		SpringApplication.run(VisibleServerApplication.class, args);
	}
}
