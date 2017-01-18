package com.visible.web;

import com.visible.ClassMethods;
import com.visible.JavaProgram;
import com.visible.symbolic.docker.DockerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping
    public ClassMethods handleFileUploadInContainer(@RequestParam("file") MultipartFile file)
                throws IOException, InterruptedException, ClassNotFoundException {

        String fileName = file.getOriginalFilename();
        JavaProgram javaProgram = new JavaProgram(fileName, file.getBytes());
        boolean success = javaProgram.saveToDirectory();

        if (success && !"true".equals(System.getProperty("WITHIN_DOCKER"))) {
            DockerContainer dockerContainer = applicationContext.getBean(DockerContainer.class);
            System.out.println("UPLOADED FILE AND CREATED DOCKER CONTAINER:");
            System.out.println(dockerContainer.getIp());
            System.out.println(dockerContainer.getPort());
            return javaProgram.getClassMethods();
        } else {
            ClassMethods classMethods = new ClassMethods();
            classMethods.setError();
            return classMethods;
        }


    }

}