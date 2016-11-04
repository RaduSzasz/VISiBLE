package com.visible;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.visible.jpf.JPFAdapter;

@SpringBootApplication
public class VisibleServerApplication {

	public static void main(String[] args) {
    JPFAdapter jpf = new JPFAdapter();
    jpf.runJPF("TestTree.jpf");
		SpringApplication.run(VisibleServerApplication.class, args);
	}
}
