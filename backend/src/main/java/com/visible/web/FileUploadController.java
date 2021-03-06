package com.visible.web;

import com.visible.ClassMethods;
import com.visible.JavaProgram;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

@Configuration
@RestController
@Scope("session")
@RequestMapping("/upload")
public class FileUploadController {

  @PostMapping
  public ClassMethods handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
          throws java.io.IOException, InterruptedException, ExecutionException, ClassNotFoundException {

    String fileName = file.getOriginalFilename();
    JavaProgram javaProgram = javaProgram(fileName, file.getBytes());
    boolean success = javaProgram.saveToDirectory();

    if (success) {
      return javaProgram.getClassMethods();
    } else {
      ClassMethods classMethods = new ClassMethods();
      classMethods.setError();
      return classMethods;
    }

  }

//  @Bean
  public JavaProgram javaProgram(String fileName, byte[] data) {
      return new JavaProgram(fileName, data);
  }
}