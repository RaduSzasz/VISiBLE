package com.visible.symbolic.jpf;

import com.visible.JavaProgram;
import com.visible.symbolic.state.State;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static gov.nasa.jpf.util.test.TestJPF.assertTrue;
import static org.junit.Assert.assertEquals;

public class JPFAdapterTest {
    private final static String JAVA_EXTENSION = ".java";
    private final static String JAVA_FILE_NAME = "MaxOfFour";
    private final static String SYMBOLIC_METHOD_NAME = "symVis";
    private final static int SYMBOLIC_METHOD_NO_ARGS = 4;

    // We need to get back to the base package, which is 4 packages up.
    private final static String PACKAGE_LEVEL = "../../../../";

    private ExecutorService service;

    @BeforeClass
    public static void setUpJavaProgram() throws Exception {
        File f = new File(JPFAdapterTest.class.getResource(PACKAGE_LEVEL + JAVA_FILE_NAME + JAVA_EXTENSION).toURI());
        boolean success = JavaProgram.saveAndCompile(JAVA_FILE_NAME + JAVA_EXTENSION, Files.readAllBytes(f.toPath()));
        assertTrue(success);
    }

    @Before
    public void generateExecutorService() {
        service = Executors.newFixedThreadPool(4);
    }

    @Test
    public void atStartFirstNodeIsReturned() throws ExecutionException, InterruptedException {
        JPFAdapter jpfAdapter =
                new JPFAdapter(JAVA_FILE_NAME, SYMBOLIC_METHOD_NAME, SYMBOLIC_METHOD_NO_ARGS, service);
        State expectedResult = new State(0, null)
                                .setIfPC("x_1_SYMINT>=y_2_SYMINT")
                                .setElsePC("x_1_SYMINT<y_2_SYMINT")
                                .setType("normal");
        assertEquals(service.submit(jpfAdapter).get(), expectedResult);
    }


    @AfterClass
    public static void tearDown() {
        // TODO: Remove java program from File System

    }
}