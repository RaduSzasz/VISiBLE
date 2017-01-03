/* This class represents the uploaded Java files */
package com.visible;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class JavaProgram {

  private static String path;
  private static String fileName;
  private static byte[] code;

  public static boolean storeFile(String name, byte[] data) {
    fileName = name;
    if (!fileName.contains(".jar")) {
      return false;
    }
    code = data;
    String pwd = System.getProperty("user.dir");

    String pathToInput;
    if (pwd.endsWith("backend")) {
      // Spring tests run from VISiBLE/backend
      pathToInput = "/input/";
    } else {
      // Server runs from VISiBLE/
      pathToInput = "/backend/input/";
    }

    path = pwd + pathToInput;
    return saveToDirectory();
  }

  private static boolean saveToDirectory() {
    try {
      File file = new File(path + fileName);
      boolean success = true;
      if (!file.getParentFile().exists()) {
         success = file.getParentFile().mkdirs();
      }
      if (!file.exists()) {
        success &= file.createNewFile();
      }

      PrintStream stream = new PrintStream(file);
      stream.write(code);
      stream.close();

      return success;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

}