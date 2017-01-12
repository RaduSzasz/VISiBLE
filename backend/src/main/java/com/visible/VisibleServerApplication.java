package com.visible;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import com.visible.symbolic.docker.DockerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.Map;

@SpringBootApplication
public class VisibleServerApplication {

	@Autowired
	private DockerClient dockerClient;

	public static void main(String[] args) {
		SpringApplication.run(VisibleServerApplication.class, args);
	}

	@Bean
	public DockerClient dockerClient() {
		return DockerClientBuilder.getInstance().build();
	}

	@Bean
	@Scope("session")
	public DockerContainer dockerContainer() {
		if (dockerClient == null) {
			throw new RuntimeException("Docker client was null");
		}
		if (dockerClient.createContainerCmd("visible") == null) {
			throw new RuntimeException("Did not manage to get visible image");
		}
        CreateContainerResponse container = dockerClient.createContainerCmd("visible")
                .withPublishAllPorts(true)
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        if (inspectContainerResponse == null) {
            throw new RuntimeException("NULL inspectorContainerResponse");
        }

		DockerContainer dockerContainer = null;

        NetworkSettings networkSettings = inspectContainerResponse.getNetworkSettings();
        Map<ExposedPort, Ports.Binding[]> bindings = networkSettings.getPorts().getBindings();
        for (ExposedPort exposedPort : bindings.keySet()) {
            Ports.Binding[] portBindings = bindings.get(exposedPort);
            for(Ports.Binding portBinding : portBindings) {
                String containerIP = portBinding.getHostIp();
                int containerPort = Integer.getInteger(portBinding.getHostPortSpec());

                dockerContainer = new DockerContainer(containerIP, containerPort);
            }
        }

		return dockerContainer;
	}
}
