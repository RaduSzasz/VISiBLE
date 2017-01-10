package com.visible.symbolic;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.visible.symbolic.state.State;

public class DockerizedExecutor implements SymbolicExecutor {
    @Override
    public State stepLeft() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                                        .withRegistryUrl("I Have no clue")
                                        .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        return null;
    }

    @Override
    public State stepRight() {
        return null;
    }

    @Override
    public State call() throws Exception {
        return null;
    }
}
