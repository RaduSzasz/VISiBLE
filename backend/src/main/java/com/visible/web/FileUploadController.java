package com.visible.web;

import com.github.dockerjava.api.DockerClient;
import com.visible.ClassMethods;
import com.visible.JavaProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
    @Autowired
    private DockerClient dockerClient;

    @PostMapping
    public ClassMethods handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
            throws java.io.IOException, InterruptedException, ExecutionException, ClassNotFoundException {

        return getClassMethods(file);
    }

    private ClassMethods getClassMethods(MultipartFile file) throws IOException, InterruptedException, ClassNotFoundException {
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