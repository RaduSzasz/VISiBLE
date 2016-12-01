package com.visible;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.multipart.MultipartFile;

import javax.tools.JavaFileObject;
import java.io.File;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

  @PostMapping
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
      RedirectAttributes redirectAttributes) throws java.io.IOException, java.io.UnsupportedEncodingException{

    String fileName = file.getOriginalFilename();
    String name = fileName.substring(0, fileName.lastIndexOf("."));
    JavaProgram javaProgram = new JavaProgram(name, file.getBytes());

    // TODO Get symbolic method name and number of symbolic arguments from user
    VisibleServerApplication.setupJPF(name, "symVis", 1);
    return fileName + " uploaded " + (javaProgram.isCompilationSuccessful() ? "and compiled successfully" :
            "but could not be compiled");
  }
}
