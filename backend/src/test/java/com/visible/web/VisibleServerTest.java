package com.visible.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.visible.ClassMethods;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VisibleServerTest {
	
	private ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	// TODO: Figure out mocking problem for JavaProgram
	//@MockBean
	//private JavaProgram javaProgram;
	
	@MockBean
	private SymbolicExecutor executor;
	
	@MockBean
	private Future<State> future;
	
	@MockBean 
	private ExecutorService service;
	
	@Test
	public void dummyTest() {
		assertTrue(true);
	}

	@Test
	public void testUploadedFileIsJAR() throws java.io.IOException {
		String filePath = "src/test/resources/WouldIUseJPFAgain.java";

		ClassMethods expected = new ClassMethods();
		expected.setError();

		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		parts.add("file", new FileSystemResource(filePath));

		String response = this.restTemplate.postForObject("/upload", parts, String.class);

		// Assert that both JSON objects are equivalent
		assertEquals(om.readValue(expected.toString(), Map.class),
				om.readValue(response, Map.class));
	}
	
}

