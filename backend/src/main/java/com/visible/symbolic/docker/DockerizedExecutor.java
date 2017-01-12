package com.visible.symbolic.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import com.visible.symbolic.Direction;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Configuration
@Scope("session")
public class DockerizedExecutor implements SymbolicExecutor {
    @Autowired
    private RestTemplate restTemplate;

    private String containerIP;
    private int containerPort;

    public DockerizedExecutor() {}

    private State makeStepRequest(Direction direction) {
        try {
            URL url = new URL("http", containerIP, containerPort, direction.getEndpoint());
            System.out.println(url.toString());

            ResponseEntity<State> responseEntity = restTemplate.postForEntity(url.toURI(), null, State.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            return new State(-1, null).withError("Could not connect to Dockerized JPF");
        }
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
    public State execute() throws ExecutionException, InterruptedException {

    }

    @Override
    public State restart() throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public State call() throws Exception {
        return null;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        return restTemplate;
    }

}
