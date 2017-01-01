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
public class FileUploadController {

  private String fileName;

  @PostMapping
  @RequestMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
      RedirectAttributes redirectAttributes) throws java.io.IOException, java.io.UnsupportedEncodingException, InterruptedException {

    String fileName = file.getOriginalFilename();
    this.fileName= fileName.substring(0, fileName.lastIndexOf("."));
    JavaProgram javaProgram = new JavaProgram(this.fileName, file.getBytes());

    // TODO: Return the methods that are contained within the jar file provided
    return null;
  }

  @PostMapping
  @RequestMapping("/symbolicmethod")
  public String handleSymbolicMethodSelection(@RequestParam("name") String methodName,
                                              @RequestParam("no_args") int noArgs,
                                               RedirectAttributes redirectAttributes) throws InterruptedException {
    VisibleServerApplication.setupJPF(fileName, methodName, noArgs);

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
