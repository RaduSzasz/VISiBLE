/* This class represents the uploaded Java files */
package com.visible;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class JavaProgram {

  private static final String JAVA_EXTENSION = ".java";
  private static final String JAVAC = "javac -g ";
  private static final String PATH_TO_INPUT = "backend/input/";

  private String path;
  private String fileName;
  private byte[] code;

  public JavaProgram(String fileName, byte[] code) {

    // Constructor takes filename without file extension
    this.fileName = fileName + JAVA_EXTENSION;
    this.code = code;
    this.path = System.getProperty("user.dir") + "/" + PATH_TO_INPUT;
    saveToDirectory();
    compile();
  }

  private void saveToDirectory() {
    try {
      File file = new File(path + fileName);
      file.createNewFile();

      PrintStream stream = new PrintStream(file);
      stream.println(new String(code, "UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void compile() {
    try {
      Process process = Runtime.getRuntime().exec(JAVAC + PATH_TO_INPUT + fileName);
      int exitCode = process.waitFor();
      if (exitCode != 0) {
        //TODO Inform frontend that code doesn't compile
        System.err.print(fileName + JAVA_EXTENSION + " does not compile.");
        return;
      }
      return;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}