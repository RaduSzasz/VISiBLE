/* This class represents the uploaded Java files */
package com.visible;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JavaProgram {

  private static final String JAVAC = "javac -g ";
  private static String PATH_TO_INPUT = "backend/input/";

  private static String path;
  private static String fileName;
  private static byte[] code;

  public static boolean saveAndCompile(String name, byte[] data) {
    fileName = name;
    if (!fileName.contains(".jar")) {
      return false;
    }
    code = data;
    String pwd = System.getProperty("user.dir");
    // Hack to make Spring tests work
    if (pwd.endsWith("backend")) {
    	PATH_TO_INPUT = "input/";
    }
    path = pwd + "/" + PATH_TO_INPUT;
    return saveToDirectory() && compile();
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

      try {
		javaMethodNames();
	} catch (Exception e) {
		e.printStackTrace();
	}
      return success;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private static boolean compile() {
    try {
      // Hack to make jar file uploads work
      if (fileName.contains(".jar")) {
        return true;
      }
      Process process = Runtime.getRuntime().exec(JAVAC + path + fileName);
      int exitCode = process.waitFor();
      return (exitCode == 0);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }
  
  private static ClassMethods javaMethodNames() throws IOException, ClassNotFoundException, InterruptedException {
	  
	  ClassMethods classMethods = new ClassMethods();
	  String pathToJar = path + fileName;
	  
	  JarFile jarFile = new JarFile(pathToJar);
	  Enumeration<JarEntry> entries = jarFile.entries();

	  URL[] urls = { new URL("file://" + pathToJar) };
	  URLClassLoader cl = new URLClassLoader(urls);
	  
	  while (entries.hasMoreElements()) {
		  JarEntry entry = entries.nextElement();
		  String entryName = entry.getName();
		  if (entryName.endsWith(".class")) {
			  
			  // Remove ".class" extension
			  String className = entryName.substring(0, entryName.lastIndexOf("."));
			  
			  // Get full class name from path
			  className = className.replace('/', '.');
			  
			  Class<?> cls = cl.loadClass(className);
			  
			  for (Method m : cls.getDeclaredMethods()) {
				  System.out.println(className + " " + m.getName());
				  classMethods.addMethodToClass(className, m.getName(), 
						  m.getParameterTypes().length, m.toString());
			  }
		  }
	  }
	  
	  jarFile.close();
	  System.out.println(classMethods);
	  return classMethods;
  }

}