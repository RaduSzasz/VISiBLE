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
    JavaProgram javaProgram = new JavaProgram(fileName, file.getBytes());
    return fileName + " uploaded successfully";
  }
}
