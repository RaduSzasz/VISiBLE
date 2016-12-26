package com.visible;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
  @PostMapping
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
      RedirectAttributes redirectAttributes) throws java.io.IOException, java.io.UnsupportedEncodingException, InterruptedException {

    String fileName = file.getOriginalFilename();
    String name = fileName.substring(0, fileName.lastIndexOf("."));
    JavaProgram javaProgram = new JavaProgram(name, file.getBytes());

    // TODO Get Symbolic Method name and number of arguments from frontend
    VisibleServerApplication.setupJPF(name, "symVis", 4);
    return fileName + " uploaded " + (javaProgram.isCompilationSuccessful() ?
            "and compiled successfully." : "but could not be compiled.");
  }
}