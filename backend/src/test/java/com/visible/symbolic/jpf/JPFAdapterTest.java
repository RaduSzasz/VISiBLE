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
import java.util.HashMap;
import java.util.Map;
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
                new JPFAdapter(JAR_NAME, CLASS_NAME, SYMBOLIC_METHOD_NAME, SYMBOLIC_METHOD_NO_ARGS, generateBooleanArray(true, true, true, true));

        State expectedResult = new State(0, null)
                                .setIfPC("x>=y")
                                .setElsePC("x<y")
                                .setType("normal")
                                .setConcreteValues(new HashMap<>());

        assertEquals(expectedResult.toString(), jpfAdapter.execute().toString());
    }

    @Test
    public void stepLeftReturnsIfNode() throws ExecutionException, InterruptedException {
        JPFAdapter jpfAdapter = new JPFAdapter(JAR_NAME, CLASS_NAME, SYMBOLIC_METHOD_NAME, SYMBOLIC_METHOD_NO_ARGS, generateBooleanArray(true, true, true, true));
        State parent = jpfAdapter.execute();
        Map<String, Integer> map = new HashMap<>();
        map.put("x_1_SYMINT", -10);
        map.put("y_2_SYMINT", -10);
        State expectedResult = new State(1, parent).setIfPC("x>=z")
                                                        .setElsePC("x<z")
                                                        .setType("normal")
                                                        .setConcreteValues(map);

        assertEquals(expectedResult.toString(), jpfAdapter.stepLeft().toString());
    }

    @Test
    public void stepRightReturnsElseNode() throws ExecutionException, InterruptedException {
        JPFAdapter jpfAdapter = new JPFAdapter(JAR_NAME, CLASS_NAME, SYMBOLIC_METHOD_NAME, SYMBOLIC_METHOD_NO_ARGS, generateBooleanArray(true, true, true, true));
        State parent = jpfAdapter.execute();
        Map<String, Integer> map = new HashMap<>();
        map.put("x_1_SYMINT", -10);
        map.put("y_2_SYMINT", -9);
        State expectedResult = new State(1, parent).setIfPC("y>=z")
                                                        .setElsePC("y<z")
                                                        .setType("normal")
                                                        .setConcreteValues(map);

        assertEquals(expectedResult.toString(), jpfAdapter.stepRight().toString());
    }

    @Test
    public void leafTypeIsSet() throws ExecutionException, InterruptedException {
        JPFAdapter jpfAdapter = new JPFAdapter(JAR_NAME, CLASS_NAME, SYMBOLIC_METHOD_NAME, SYMBOLIC_METHOD_NO_ARGS, generateBooleanArray(true, true, true, true));
        jpfAdapter.execute();
        jpfAdapter.stepLeft();
        State parent = jpfAdapter.stepLeft();
        State child = jpfAdapter.stepLeft();
        Map<String, Integer> map = new HashMap<>();
        map.put("x_1_SYMINT", -10);
        map.put("y_2_SYMINT", -10);
        map.put("z_3_SYMINT", -10);
        map.put("t_4_SYMINT", -10);
        State expectedResult = new State(3, parent).setIfPC(null)
                                                        .setElsePC(null)
                                                        .setType("leaf")
                                                        .setConcreteValues(map);

        assertEquals(expectedResult, child);
    }

    private boolean[] generateBooleanArray(boolean... isSymb) {
        return isSymb;
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        Process processRM = Runtime.getRuntime().exec("rm -rf backend/input/");
        processRM.waitFor();
    }
}