package com.visible.web;

import com.visible.JavaProgram;
import com.visible.VisibleServerApplication;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.jpf.JPFAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Configuration
@RestController
@RequestMapping("/upload")
public class FileUploadController {
  private String name;
  private String method;
  private int argNumber;

  @PostMapping
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
      RedirectAttributes redirectAttributes) throws java.io.IOException, java.io.UnsupportedEncodingException, InterruptedException {

    String fileName = file.getOriginalFilename();
    String name = fileName.substring(0, fileName.lastIndexOf("."));
    JavaProgram javaProgram = new JavaProgram(name, file.getBytes());

    // TODO Get Symbolic Method name and number of arguments from frontend
    VisibleServerApplication.setupJPF(name, "symVis", 4);

    this.name = name;
    this.method = "symVis";
    this.argNumber = 4;
    return fileName + " uploaded " + (javaProgram.isCompilationSuccessful() ?
            "and compiled successfully." : "but could not be compiled.");
  }

  @Bean
  public SymbolicExecutor symbolicExecutor() {
    return new JPFAdapter(name, method, argNumber);
  }

}