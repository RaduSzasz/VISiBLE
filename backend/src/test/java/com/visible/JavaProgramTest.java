package com.visible;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class JavaProgramTest {

    @BeforeClass
    public static void setup() throws IOException, InterruptedException {
        Process processRM = Runtime.getRuntime().exec("rm -rf input/*");
        processRM.waitFor();
    }

    @Test
    public void saveJarTest() throws IOException {
        byte[] expectedData = Files.readAllBytes(Paths.get("src/test/resources/MaxOfFour.jar"));
        JavaProgram javaProgram = new JavaProgram("MaxOfFour.jar", expectedData);

        // Check that file is saved
        boolean success = javaProgram.saveToDirectory();
        assertTrue(success);

        // Check that saved file is correct
        byte[] data = Files.readAllBytes(Paths.get("input/MaxOfFour.jar"));
        assertArrayEquals(expectedData, data);
    }

    @Test
    public void saveNotJarFailsTest() throws IOException {
        byte[] expectedData = Files.readAllBytes(Paths.get("src/test/resources/WouldIUseJPFAgain.java"));
        JavaProgram javaProgram = new JavaProgram("WouldIUseJPFAgain.java", expectedData);

        // Check that file is not saved
        boolean success = javaProgram.saveToDirectory();
        assertFalse(success);
    }

    @Test
    public void getClassMethodsFromJarSimpleTest() throws IOException, ClassNotFoundException, InterruptedException {
        byte[] expectedData = Files.readAllBytes(Paths.get("src/test/resources/MaxOfFour.jar"));
        JavaProgram javaProgram = new JavaProgram("MaxOfFour.jar", expectedData);

        // Check that file is saved
        boolean success = javaProgram.saveToDirectory();
        assertTrue(success);

        // Build expected ClassMethods
        ClassMethods expected = new ClassMethods();
        expected.addMethodToClass("MaxOfFour", "main", 1,
                "public static void MaxOfFour.main(java.lang.String[])");
        expected.addMethodToClass("MaxOfFour", "symVis", 4,
                "private static java.lang.String MaxOfFour.symVis(int,int,int,int)");

        // Check correct class methods are returned
        ClassMethods classMethods = javaProgram.getClassMethods();
        assertEquals(expected, classMethods);
    }

    @Test
    public void getClassMethodsFromJarPackageFolderTest() {
        // TODO: Test that can get class methods from jar with packages in the correct folders
    }

    @After
    public void clearInputFolder() throws IOException, InterruptedException {
        Process processRM = Runtime.getRuntime().exec("rm -rf input/*");
        processRM.waitFor();
    }


}
