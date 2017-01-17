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
        byte[] expectedData = Files.readAllBytes(Paths.get("backend/src/test/resources/MaxOfFour.jar"));
        JavaProgram javaProgram = new JavaProgram("MaxOfFour.jar", expectedData);

        // Check that file is saved
        boolean success = javaProgram.saveToDirectory();
        assertTrue(success);

        // Check that saved file is correct
        byte[] data = Files.readAllBytes(Paths.get("backend/input/MaxOfFour.jar"));
        assertArrayEquals(expectedData, data);
    }

    @Test
    public void saveNotJarFailsTest() throws IOException {
        byte[] expectedData = Files.readAllBytes(Paths.get("backend/src/test/resources/WouldIUseJPFAgain.java"));
        JavaProgram javaProgram = new JavaProgram("WouldIUseJPFAgain.java", expectedData);

        // Check that file is not saved
        boolean success = javaProgram.saveToDirectory();
        assertFalse(success);
    }

    @Test
    public void getClassMethodsFromJarSimpleTest() throws IOException, ClassNotFoundException, InterruptedException {
        byte[] expectedData = Files.readAllBytes(Paths.get("backend/src/test/resources/MaxOfFour.jar"));
        String jarName = "MaxOfFour.jar";
        JavaProgram javaProgram = new JavaProgram(jarName, expectedData);

        // Check that file is saved
        boolean success = javaProgram.saveToDirectory();
        assertTrue(success);

        // Build expected ClassMethods
        ClassMethods expected = new ClassMethods();
        expected.addMethodToClass("MaxOfFour", "main", 1,
                "public static void MaxOfFour.main(java.lang.String[])");
        expected.addMethodToClass("MaxOfFour", "symVis", 4,
                "private static java.lang.String MaxOfFour.symVis(int,int,int,int)");
        expected.setJarName(jarName);

        // Check correct class methods are returned
        ClassMethods classMethods = javaProgram.getClassMethods();
        assertEquals(expected, classMethods);
    }

    @Test
    public void getClassMethodsFromJarPackageFolderTest() throws IOException, ClassNotFoundException, InterruptedException {
        byte[] expectedData = Files.readAllBytes(Paths.get("backend/src/test/resources/Zero.jar"));
        String jarName = "Zero.jar";
        JavaProgram javaProgram = new JavaProgram(jarName, expectedData);

        // Check that file is saved
        boolean success = javaProgram.saveToDirectory();
        assertTrue(success);

        // Build expected ClassMethods
        ClassMethods expected = new ClassMethods();
        expected.addMethodToClass("Zero", "zeroMethod", 1,
                "public void Zero.zeroMethod(java.lang.String)");
        expected.addMethodToClass("one.One", "oneMethod", 1,
                "public void one.One.oneMethod(java.lang.String)");
        expected.addMethodToClass("one.two.Two", "twoMethod", 1,
                "public void one.two.Two.twoMethod(java.lang.String)");
        expected.addMethodToClass("one.three.Three", "threeMethod", 1,
                "public void one.three.Three.threeMethod(java.lang.String)");
        expected.setJarName(jarName);

        // Check correct class methods are returned
        ClassMethods classMethods = javaProgram.getClassMethods();
        assertEquals(expected, classMethods);
    }

    @After
    public void clearInputFolder() throws IOException, InterruptedException {
        Process processRM = Runtime.getRuntime().exec("rm -rf input/*");
        processRM.waitFor();
    }


}
