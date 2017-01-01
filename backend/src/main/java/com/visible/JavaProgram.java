/* This class represents the uploaded Java files */
package com.visible;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class JavaProgram {

  private static final String JAVAC = "javac -g ";
  private static final String PATH_TO_INPUT = "backend/input/";
  private static final String JAVA_EXTENSION = ".java";

  private static String path;
  private static String fileName;
  private static byte[] code;

  public static boolean saveAndCompile(String name, byte[] data) {
    fileName = name;
    code = data;
    path = System.getProperty("user.dir") + "/" + PATH_TO_INPUT;
    saveToDirectory();
    return compile();
  }

  private static void saveToDirectory() {
    try {
      System.out.println(path + fileName);
      File file = new File(path + fileName + JAVA_EXTENSION);
      if (!file.getParentFile().exists())
        file.getParentFile().mkdirs();
      if (!file.exists())
        file.createNewFile();

      PrintStream stream = new PrintStream(file);
      stream.println(new String(code, "UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static boolean compile() {
    try {
      System.out.println(JAVAC + path + fileName + JAVA_EXTENSION);
      Process process = Runtime.getRuntime().exec(JAVAC + path + fileName + JAVA_EXTENSION);
      int exitCode = process.waitFor();
      System.out.println(exitCode);
      return (exitCode == 0);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }

}