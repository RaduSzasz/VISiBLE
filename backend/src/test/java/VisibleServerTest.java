package com.visible;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VisibleServerTest {
	
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void testUploadFile() {
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		//System.out.println("I AM WORKING IN: " + System.getProperty("user.dir"));
		parts.add("file", new FileSystemResource("src/test/resources/MaxOfFour.java"));
		//String result = this.restTemplate.postForObject("/upload", parts, String.class);
		//System.out.println("Response from server: " + result);
		//String body = this.restTemplate.getForObject("/stepleft", String.class);
		//System.out.println(body);
		//assertEquals("Hello World", body);
		//assertEquals(5, 10);
	}

}
