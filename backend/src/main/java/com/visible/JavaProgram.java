/* This class represents the uploaded Java files */
package com.visible;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class JavaProgram {

  private static final String JAVA_EXTENSION = ".java";
  private static final String JAVAC = "javac -g ";
  private static final String PATH_TO_INPUT = "backend/input/";

  private static String path;
  private static String fileNameWithExt;
  private static byte[] code;

  public static boolean saveAndCompile(String fileName, byte[] data) {
    // Method takes filename without file extension
    fileNameWithExt = fileName + JAVA_EXTENSION;
    code = data;
    path = System.getProperty("user.dir") + "/" + PATH_TO_INPUT;
    saveToDirectory();
    return compile();
  }

  private static void saveToDirectory() {
    try {
      File file = new File(path + fileNameWithExt);
      file.createNewFile();

      PrintStream stream = new PrintStream(file);
      stream.println(new String(code, "UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static boolean compile() {
    try {
      Process process = Runtime.getRuntime().exec(JAVAC + PATH_TO_INPUT + fileNameWithExt);
      int exitCode = process.waitFor();
      return (exitCode == 0);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }

}