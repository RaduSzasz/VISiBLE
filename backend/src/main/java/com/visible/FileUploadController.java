package com.visible;

import com.visible.jpf.Direction;
import com.visible.jpf.JPFAdapter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    JPFAdapter.moveForward(Direction.LEFT).map(latch -> {
      try {
        latch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return true;
    });

    return JPFAdapter.getListenerState().toJSON();
  }
}
