package com.visible.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.visible.ClassMethods;
import com.visible.JavaProgram;
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

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VisibleServerTest {
	
	private ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
    private JavaProgram javaProgram;

	//@MockBean private SymbolicExecutor executor;
	//@MockBean private Future<State> future;
	//@MockBean private ExecutorService service;

	@Test
	public void testUpload() throws IOException, ClassNotFoundException, InterruptedException {
		String filePath = "src/test/resources/MaxOfFour.jar";

        // Build expected ClassMethods
        ClassMethods expected = new ClassMethods();
        expected.addMethodToClass("MaxOfFour", "main", 1,
                "public static void MaxOfFour.main(java.lang.String[])");
        expected.addMethodToClass("MaxOfFour", "symVis", 4,
                "private static java.lang.String MaxOfFour.symVis(int,int,int,int)");
        expected.setJarName("MaxOfFour.jar");

        given(this.javaProgram.saveToDirectory()).willReturn(true);
		given(this.javaProgram.getClassMethods()).willReturn(expected);

		// Make POST request
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		parts.add("file", new FileSystemResource(filePath));
		String response = this.restTemplate.postForObject("/upload", parts, String.class);

		// Assert that both JSON objects are equivalent
		assertEquals(om.readValue(expected.toString(), Map.class),
					 om.readValue(response, Map.class));
	}
	
}

