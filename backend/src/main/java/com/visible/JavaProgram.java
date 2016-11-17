/* This class represents the uploaded Java files */
package com.visible;

import javax.tools.*;
import java.io.File;
import java.io.IOException;

public class JavaProgram{

  private File file;

  public JavaProgram(File source) {
    this.file = source;
  }

  public void compile() throws IOException {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    compiler.run(null, null, null, file.getPath());
  }

}