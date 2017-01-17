package com.visible.symbolic.jpf;

import com.visible.JavaProgram;
import com.visible.symbolic.state.State;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JPFAdapterTest {
    private final static String JAR_NAME = "MaxOfFour.jar";
    private final static String CLASS_NAME = "MaxOfFour";
    private final static String SYMBOLIC_METHOD_NAME = "symVis";
    private final static int SYMBOLIC_METHOD_NO_ARGS = 4;
    // We need to get back to the base package, which is 4 packages up.
    private final static String PACKAGE_LEVEL = "../../../../";

    @BeforeClass
    public static void setUpJavaProgram() throws Exception {
        File f = new File(JPFAdapterTest.class.getResource(PACKAGE_LEVEL + JAR_NAME).toURI());
        JavaProgram javaProgram = new JavaProgram(JAR_NAME, Files.readAllBytes(f.toPath()));
        boolean success = javaProgram.saveToDirectory();
        assertTrue(success);
    }

    @Test
    public void atStartFirstNodeIsReturned() throws IOException, ExecutionException, InterruptedException {
        JPFAdapter jpfAdapter =
                new JPFAdapter(JAR_NAME, CLASS_NAME, SYMBOLIC_METHOD_NAME, SYMBOLIC_METHOD_NO_ARGS, generateBooleanArray());

        jpfAdapter.setIsTest();
        State expectedResult = new State(0, null)
                                .setIfPC("x>=y")
                                .setElsePC("x<y")
                                .setType("normal");

        // The assertEquals only works with String representations, not actual States
        assertEquals(expectedResult.toString(), jpfAdapter.execute().toString());
    }

    private boolean[] generateBooleanArray() {
        boolean[] array = new boolean[SYMBOLIC_METHOD_NO_ARGS];
        for (int i = 0; i < SYMBOLIC_METHOD_NO_ARGS; i++) {
            array[i] = true;
        }
        return array;
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        Process processRM = Runtime.getRuntime().exec("rm -rf input/*");
        processRM.waitFor();
    }
}