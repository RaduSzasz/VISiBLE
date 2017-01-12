package com.visible.symbolic.docker;

import com.visible.symbolic.Direction;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.concurrent.ExecutionException;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DockerizedExecutor implements SymbolicExecutor {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DockerContainer dockerContainer;


    public DockerizedExecutor() {}

    private State makeStepRequest(Direction direction) {
        try {
            URL url = new URL("http", dockerContainer.getIp(), dockerContainer.getPort(), direction.getEndpoint());
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
        return null;
    }

    @Override
    public State restart() throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public State call() throws Exception {
        return null;
    }

}
