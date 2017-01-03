package com.visible.web;

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
  public State handleFileUpload(@RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) throws java.io.IOException, InterruptedException, ExecutionException {

    String fileName = file.getOriginalFilename();
    boolean success = JavaProgram.storeFile(fileName, file.getBytes());

    if (!success) {
        State errorState = new State(-1, null);
        errorState.setError(fileName + ERROR_MSG);
    	return errorState;
    }
    
    this.name = fileName.substring(0, fileName.lastIndexOf("."));
    this.method = "symVis";
    this.argNumber = 4;

    SymbolicExecutor symbolicExecutor = symbolicExecutor();
    return executorService().submit(symbolicExecutor).get();
  }

  @Bean
  @Scope("session")
  public SymbolicExecutor symbolicExecutor() {
    JPFAdapter adapter = new JPFAdapter(name, method, argNumber, executorService());
    return adapter;
  }

  @Bean
  @ApplicationScope
  public ExecutorService executorService() {
    return Executors.newFixedThreadPool(8);
  }

}