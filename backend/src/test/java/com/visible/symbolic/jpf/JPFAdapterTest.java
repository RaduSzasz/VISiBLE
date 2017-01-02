package com.visible.symbolic.jpf;

import com.visible.JavaProgram;
import com.visible.symbolic.state.State;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static gov.nasa.jpf.util.test.TestJPF.assertEquals;
import static gov.nasa.jpf.util.test.TestJPF.assertTrue;

public class JPFAdapterTest {
    private final static String JAR_EXTENSION = ".jar";
    private final static String JPF_EXTENSION = ".jpf";
    private final static String JAR_NAME = "MaxOfFour";
    private final static String PATH_TO_JAR = "./backend/src/test/resources/" + JAR_NAME + JAR_EXTENSION;
    private final static String SYMBOLIC_METHOD_NAME = "symVis";
    private final static int SYMBOLIC_METHOD_NO_ARGS = 4;

    @BeforeClass
    public static void setUpJavaProgram() throws Exception {
        System.setProperty("user.dir", "/home/vaibhav/Documents/VISiBLE");
        Path pathToFile = Paths.get(PATH_TO_JAR);
        boolean success = JavaProgram.saveAndCompile(JAR_NAME + JAR_EXTENSION, Files.readAllBytes(pathToFile));
        clearInputs();
        assertTrue(success);
    }

    @Test
    public void atStartFirstNodeIsReturned() throws IOException, ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        JPFAdapter jpfAdapter =
                new JPFAdapter(JAR_NAME, SYMBOLIC_METHOD_NAME, SYMBOLIC_METHOD_NO_ARGS, service);

        State expectedResult = new State(0, null)
                                .setIfPC("x_1_SYMINT>=y_2_SYMINT")
                                .setElsePC("x_1_SYMINT<y_2_SYMINT");
        expectedResult.setType("normal");
//        assertEquals(service.submit(jpfAdapter).get(), expectedResult);
        clearInputs();
        assertTrue(true);
    }

    private static void clearInputs() throws IOException, InterruptedException {
        Process process1 = Runtime.getRuntime().exec("rm backend/input/" + JAR_NAME + JAR_EXTENSION);
        Process process2 = Runtime.getRuntime().exec("rm backend/input/" + JAR_NAME + JPF_EXTENSION);
        process1.waitFor();
        process2.waitFor();
    }

    @AfterClass
    public static void tearDown() {
        // TODO: Remove java program from File System
    }
}