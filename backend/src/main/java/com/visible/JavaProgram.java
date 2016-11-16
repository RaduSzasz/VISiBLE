/* This class represents the uploaded Java files */
package com.visible;

import javax.tools.JavaCompiler;

public class JavaProgram{

  private String code;
  JavaCompiler compiler;

  public JavaProgram(String code){
    this.code = code;
  }
  
  public String getCode(){
    return this.code;
  }

}