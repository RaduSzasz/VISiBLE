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

import com.visible.JavaProgram;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.*;
import java.nio.file.*;
import java.util.Map;
import java.io.InputStream;

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
	public void testUploadFileSuccess() throws java.io.IOException, InterruptedException, ExecutionException {
		String filePath = "src/test/resources/CannotCompile.java";
		//byte[] data = Files.readAllBytes(Paths.get(filePath));
		//given(this.javaProgram.saveAndCompile("MaxOfFour.java", data)).willReturn(true);
		State expectedState = new State(-1, null);
		
		/* Create mock POST request. */
		// Define headers.
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	
		// Convert file to multipart form.
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		// ClassPathResource
		FileSystemResource fsr = new FileSystemResource(filePath);
		parts.add("file", fsr);
		InputStream stream = fsr.getInputStream();
		for (int i = 0; i < 100; i++) {
			System.out.println((char) stream.read());
		}
		
		// Create POST request.
		HttpEntity<MultiValueMap<String, Object>> request = 
				new HttpEntity<MultiValueMap<String, Object>>(parts, headers);
		
		System.out.println("Http Headers" + request.getHeaders());
		System.out.println("Http Body" + request.getBody());
		// Send request and recieve respose.
	
		// Specifying return values of mock objects
		//given(this.service.submit(executor)).willReturn(future);
		//given(this.future.get()).willReturn(expectedState);
		
		ResponseEntity<String> response = restTemplate.postForEntity("/upload", request, String.class);
	
		//	String response = this.restTemplate.postForObject("/upload", parts, String.class);
		
		// Assert that both JSON objects are equivalent
		assertEquals(om.readValue(expectedState.toString(), Map.class),
				     om.readValue(response.getBody(), Map.class));
	}
	
	
	
	@Test
	public void testUploadFileCannotCompile() throws java.io.IOException {
		String filePath = "src/test/resources/CannotCompile.java";
		
		// State for invalid upload file
		State expectedState = new State(-1, null);
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new FileSystemResource(filePath));
		
		String response = this.restTemplate.postForObject("/upload", parts, String.class);
		System.out.println("RESPONSE: " + response);
		
		// Assert that both JSON objects are equivalent
		
		assertEquals(om.readValue(expectedState.toString(), Map.class),
				     om.readValue(response, Map.class));
		
	}
	
}
