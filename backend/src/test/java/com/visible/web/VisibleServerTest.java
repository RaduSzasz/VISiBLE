package com.visible.web;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.*;
import org.springframework.http.*;

import static org.mockito.BDDMockito.*;

import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.*;
import java.util.Map;

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
	public void testUploadJARSuccess() throws java.io.IOException, InterruptedException, ExecutionException {
		String filePath = "src/test/resources/MaxOfFour.jar";
		State expectedState = new State(5, null);

		// Convert file to multipart form
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new FileSystemResource(filePath));

		// Specifying return values of mock objects
		given(this.service.submit(executor)).willReturn(future);
		given(this.future.get()).willReturn(expectedState);

		String response = this.restTemplate.postForObject("/upload", parts, String.class);

		// Assert that both JSON objects are equivalent
		assertEquals(om.readValue(expectedState.toString(), Map.class),
				om.readValue(response, Map.class));
	}
	
	@Test
	public void testUploadFileSuccess() throws java.io.IOException, InterruptedException, ExecutionException {
		String filePath = "src/test/resources/MaxOfFour.java";
		//byte[] data = Files.readAllBytes(Paths.get(filePath));
		//given(this.javaProgram.saveAndCompile("MaxOfFour.java", data)).willReturn(true);
		State expectedState = new State(5, null);
	
		// Convert file to multipart form
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new FileSystemResource(filePath));
	
		// Specifying return values of mock objects
		given(this.service.submit(executor)).willReturn(future);
		given(this.future.get()).willReturn(expectedState);
			
		String response = this.restTemplate.postForObject("/upload", parts, String.class);
		
		// Assert that both JSON objects are equivalent
		assertEquals(om.readValue(expectedState.toString(), Map.class),
				     om.readValue(response, Map.class));
	}

	private static final String COMPILE_ERROR_MSG = " could not be compiled.";
	
	@Test
	public void testUploadFileCannotCompile() throws java.io.IOException {
		String filePath = "src/test/resources/CannotCompile.java";
		
		// State for invalid upload file
		State expectedState = new State(-1, null);
		expectedState.setError("CannotCompile.java" + COMPILE_ERROR_MSG);
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new FileSystemResource(filePath));
		
		String response = this.restTemplate.postForObject("/upload", parts, String.class);
		System.out.println("RESPONSE: " + response);
		
		// Assert that both JSON objects are equivalent
		assertEquals(om.readValue(expectedState.toString(), Map.class),
				     om.readValue(response, Map.class));
		
	}
	
}
