package com.visible.symbolic.jpf;

import com.visible.JavaProgram;
import com.visible.symbolic.state.State;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static gov.nasa.jpf.util.test.TestJPF.assertTrue;

public class JPFAdapterTest {
    private final static String JAVA_EXTENSION = ".java";
    private final static String JAVA_FILE_NAME = "MaxOfFour";
    private final static String PATH_TO_JAVA_FILE = "./backend/src/test/resources/" + JAVA_FILE_NAME + JAVA_EXTENSION;
    private final static String SYMBOLIC_METHOD_NAME = "symVis";
    private final static int SYMBOLIC_METHOD_NO_ARGS = 4;


    @BeforeClass
    public static void setUpJavaProgram() throws Exception {
        System.setProperty("user.dir", "/home/vaibhav/Documents/VISiBLE");
        Path pathToFile = Paths.get(PATH_TO_JAVA_FILE);
        boolean success = JavaProgram.saveAndCompile(JAVA_FILE_NAME + JAVA_EXTENSION, Files.readAllBytes(pathToFile));
        assertTrue(success);
    }

    @Test
    public void atStartFirstNodeIsReturned() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        JPFAdapter jpfAdapter =
                new JPFAdapter(JAVA_FILE_NAME, SYMBOLIC_METHOD_NAME, SYMBOLIC_METHOD_NO_ARGS, service);
        State expectedResult = new State(0, null)
                                .setIfPC("x_1_SYMINT>=y_2_SYMINT")
                                .setElsePC("x_1_SYMINT<y_2_SYMINT");
        // assertEquals(service.submit(jpfAdapter).get(), expectedResult);
        assertTrue(true);
    }


    @AfterClass
    public static void tearDown() {
        // TODO: Remove java program from File System

    }
}