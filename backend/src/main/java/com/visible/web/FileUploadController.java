package com.visible.web;

import com.visible.ClassMethods;
import com.visible.JavaProgram;
import com.visible.conditions.DockerCondition;
import com.visible.conditions.MainServerCondition;
import com.visible.symbolic.docker.DockerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
    @Autowired
    private DockerContainer dockerContainer;

    @Autowired
    private UploadHandler uploadHandler;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ClassMethods handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
            throws java.io.IOException, InterruptedException, ExecutionException, ClassNotFoundException {

        return uploadHandler.getClassMethods(file);
    }

    private ClassMethods extractClassMethods(MultipartFile file) throws IOException, InterruptedException, ClassNotFoundException {
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

    private interface UploadHandler {
        ClassMethods getClassMethods(MultipartFile file) throws InterruptedException, IOException, ClassNotFoundException;
    }

    @Bean
    @Conditional(MainServerCondition.class)
    private UploadHandler getClassMethodsAndUploadToDockerContainer() {
        return file -> {
            ClassMethods classMethods = extractClassMethods(file);
            try {
                URL url = new URL("http", dockerContainer.getIp(), dockerContainer.getPort(), "upload");
                System.out.println(url.toString());

                return restTemplate.postForEntity(url.toURI(), null, ClassMethods.class).getBody();

            } catch (URISyntaxException e) {
                throw new RuntimeException("Failed upload file to Docker container");
            }
        };
    }

    @Bean
    @Conditional(DockerCondition.class)
    private UploadHandler getClassMethod() {
        return file -> {
            return extractClassMethods(file);
        };
    }



}