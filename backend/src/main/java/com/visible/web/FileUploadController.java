package com.visible.web;

import com.visible.ClassMethods;
import com.visible.JavaProgram;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.jpf.JPFAdapter;
import com.visible.symbolic.state.State;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RestController
@Scope("session")
@RequestMapping("/upload")
public class FileUploadController {
  private String name;
  private String method;
  private int argNumber;
  private static final String ERROR_MSG = " is invalid.";

  @PostMapping
  public ClassMethods handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
          throws java.io.IOException, InterruptedException, ExecutionException, ClassNotFoundException {

    String fileName = file.getOriginalFilename();
    JavaProgram javaProgram = new JavaProgram(fileName, file.getBytes());
    boolean success = javaProgram.saveToDirectory();

    if (success) {
      return javaProgram.getClassMethods();
    } else {
      ClassMethods classMethods = new ClassMethods();
      classMethods.setError();
      return classMethods;
    }

  }
}