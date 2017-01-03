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
    private static final String CLASS_EXT = ".class";
    private static String PATH_TO_INPUT = "backend/input/";

  private String pathToJar;
  private String fileName;
  private byte[] data;

  public JavaProgram(String fileName, byte[] data) {
      String pwd = System.getProperty("user.dir");
      // Hack to make Spring tests work
      if (pwd.endsWith("backend")) {
          PATH_TO_INPUT = "input/";
      }
      this.pathToJar = pwd + "/" + PATH_TO_INPUT + fileName;
      this.fileName = fileName;
      this.data = data;
  }

  public boolean saveToDirectory() {
    if (!fileName.endsWith(".jar")) {
        return false;
    }

    try {
        File file = new File(pathToJar);
        boolean success = true;
        if (!file.getParentFile().exists()) {
              success = file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
              success &= file.createNewFile();
        }

        PrintStream stream = new PrintStream(file);
        stream.write(data);
        stream.close();

        return success;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
  }
  
  public ClassMethods getClassMethods() throws IOException, ClassNotFoundException, InterruptedException {
	  
	  ClassMethods classMethods = new ClassMethods();
	  
	  JarFile jarFile = new JarFile(pathToJar);
	  Enumeration<JarEntry> entries = jarFile.entries();

	  URL[] urls = { new URL("file://" + pathToJar) };
	  URLClassLoader cl = new URLClassLoader(urls);
	  
	  while (entries.hasMoreElements()) {
		  JarEntry entry = entries.nextElement();
		  String entryName = entry.getName();
		  if (entryName.endsWith(CLASS_EXT)) {
			  
			  // Remove ".class" extension
			  String className = entryName.substring(0, entryName.lastIndexOf("."));
			  
			  // Get full class name from path
			  className = className.replace('/', '.');
			  
			  Class<?> cls = cl.loadClass(className);
			  
			  for (Method m : cls.getDeclaredMethods()) {
				  classMethods.addMethodToClass(className, m.getName(), 
						  m.getParameterTypes().length, m.toString());
			  }
		  }
	  }
	  
	  jarFile.close();
	  return classMethods;
  }

}