package com.visible.symbolic.jpf;

import com.visible.JavaProgram;
import com.visible.symbolic.state.State;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JPFAdapterTest {
    private final static String JAR_NAME = "MaxOfFour.jar";
    private final static String SYMBOLIC_METHOD_NAME = "symVis";
    private final static int SYMBOLIC_METHOD_NO_ARGS = 4;
    private final static String PACKAGE_LEVEL = "../../../../";

    // We need to get back to the base package, which is 4 packages up.

    private ExecutorService service;

    @BeforeClass
    public static void setUpJavaProgram() throws Exception {
        File f = new File(JPFAdapterTest.class.getResource(PACKAGE_LEVEL + JAR_NAME).toURI());
        JavaProgram javaProgram = new JavaProgram(JAR_NAME, Files.readAllBytes(f.toPath()));
        boolean success = javaProgram.saveToDirectory();
        assertTrue(success);
    }

    @Before
    public void generateExecutorService() {
        service = Executors.newFixedThreadPool(4);
    }

    @Ignore
    @Test
    public void atStartFirstNodeIsReturned() throws IOException, ExecutionException, InterruptedException {
        JPFAdapter jpfAdapter =
                new JPFAdapter(JAR_NAME, SYMBOLIC_METHOD_NAME, SYMBOLIC_METHOD_NO_ARGS, service);

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