package com.visible;

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

import static org.mockito.BDDMockito.*;

import com.visible.JavaProgram;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;

import java.util.concurrent.*;
import java.nio.file.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VisibleServerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	//@MockBean
	//private JavaProgram javaProgram;
	
	@MockBean
	private SymbolicExecutor executor;
	
	@MockBean
	private Future<State> future;
	
	@MockBean 
	private ExecutorService service;
	
	@Test
	public void testUploadFile() throws java.io.IOException, InterruptedException, ExecutionException {
		String filePath = "backend/src/test/resources/MaxOfFour.java";
		//byte[] data = Files.readAllBytes(Paths.get(filePath));
		//given(this.javaProgram.saveAndCompile("MaxOfFour.java", data)).willReturn(true);
		State returnState = new State(5, null);
		
		// Specifying return values of mock objects
		given(this.service.submit(executor)).willReturn(future);
		given(this.future.get()).willReturn(returnState);
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new FileSystemResource(filePath));
		String response = this.restTemplate.postForObject("/upload", parts, String.class);
		System.out.println("Response from server: " + response);
		//String body = this.restTemplate.getForObject("/stepleft", String.class);
		//System.out.println(body);
		//assertEquals("Hello World", body);
		assertEquals(5, 10);
	}

}
