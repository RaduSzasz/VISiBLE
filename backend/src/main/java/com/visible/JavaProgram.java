/* This class represents the uploaded Java files */
package com.visible;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class JavaProgram {

  private String path;
  private String fileName;
  private byte[] code;

  public JavaProgram(String fileName, byte[] code) {
    this.fileName = fileName;
    this.code = code;
    this.path = System.getProperty("user.dir") + "/backend/input/";
    saveToDirectory();
  }

  public void saveToDirectory() {
    try {
      File file = new File(path + fileName);
      file.createNewFile();

      PrintStream stream = new PrintStream(file);
      stream.println(new String(code, "UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}