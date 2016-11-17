/* This class represents the uploaded Java files */
package com.visible;

import java.io.IOException;
import java.io.PrintStream;

public class JavaProgram{

  private String path;
  private String fileName;
  private byte[] code;

  public JavaProgram(String fileName, byte[] code) {
    this.fileName = fileName;
    this.code = code;
    this.path = System.getProperty("user.dir") + "/backend/input/";
  }

  // TODO Finish saving file to directory and call in VisibleSeverApplication
  // using static reference
  public void saveToDirectory() {
    try {
      String fileCode = new String(code, "UTF-8");
      PrintStream stream = new PrintStream(path + fileName);
      stream.println(fileCode);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}