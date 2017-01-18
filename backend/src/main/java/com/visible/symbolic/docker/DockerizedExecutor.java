package com.visible.symbolic.docker;

import com.visible.symbolic.Direction;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DockerizedExecutor implements SymbolicExecutor {
    @Autowired
    private RestTemplate restTemplate;

    private DockerContainer dockerContainer;

    private String jarName;
    private String className;
    private String methodName;
    private int numArgs;
    private boolean[] isSymb;

    @Autowired
    public DockerizedExecutor(DockerContainer dockerContainer) {
        this.dockerContainer = dockerContainer;
    }

    private State makeStepRequest(Direction direction) {
        try {
            URL url = new URL("http", dockerContainer.getIp(), dockerContainer.getPort(), direction.getEndpoint());
            System.out.println(url.toString());
            return restTemplate.getForObject(url.toURI(), State.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new State(-1, null).withError("Could not connect to Dockerized JPF");
        }
    }

    @Override
    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public void setMethod(String method) {
        this.methodName = method;
    }

    @Override
    public void setArgNum(int argNum) {
        this.numArgs = argNum;
    }

    @Override
    public void setIsSymb(boolean[] isSymb) {
        this.isSymb = isSymb;
    }

    @Override
    public State stepLeft() {
        return makeStepRequest(Direction.LEFT);
    }

    @Override
    public State stepRight() {
        return makeStepRequest(Direction.RIGHT);
    }

    @Override
    public State execute() throws ExecutionException, InterruptedException, MalformedURLException, URISyntaxException {
        if (dockerContainer == null) {
            System.out.println("Docker container is null :( in DockerizedExecutor");
        } else {
            System.out.println("We have a docker container in DockerizedExecutor");
        }
        URL url = new URL("http", dockerContainer.getIp(), dockerContainer.getPort(), "/symbolicmethod");
        System.out.println(url.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("jar_name", jarName);
        map.add("class_name", className);
        map.add("method_name", methodName);
        map.add("no_args", Integer.toString(numArgs));
        for (boolean symbArg : isSymb) {
            map.add("is_symb", symbArg);
        }

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

        return restTemplate.postForObject(url.toURI(), request, State.class);
    }

    @Override
    public State restart() throws ExecutionException, InterruptedException {
        return null;
    }

}
