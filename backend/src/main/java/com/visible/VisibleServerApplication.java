package com.visible;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.visible.symbolic.docker.DockerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;

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
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public DockerContainer dockerContainer() {
        System.out.println("CREATING DOCKER CONTAINER");
        if (dockerClient == null) {
            throw new RuntimeException("Docker client was null");
		}
		if (dockerClient.createContainerCmd("visible") == null) {
            throw new RuntimeException("Did not manage to get visible image");
		}

        Volume input = new Volume("/backend/input/");
        Volume jpfCore = new Volume("/jpf/jpf-core");
        Volume jpfSymbc = new Volume("/jpf/jpf-symbc");
        CreateContainerResponse container = dockerClient.createContainerCmd("visible")
                .withPublishAllPorts(true)
                .withVolumes(input)
                .withBinds(new Bind(System.getProperty("user.dir") + "/backend/input/", input),
                            new Bind(System.getProperty("user.dir") + "/backend/jpf-core", jpfCore),
                            new Bind(System.getProperty("user.dir") + "/backend/jpf-symbc", jpfSymbc))
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
            System.out.println(portBindings);
            for(Ports.Binding portBinding : portBindings) {
                if (portBinding != null) {
                    String containerIP = portBinding.getHostIp();
                    int containerPort = Integer.valueOf(portBinding.getHostPortSpec());
                    System.out.println(Integer.valueOf(portBinding.getHostPortSpec()));

                    dockerContainer = new DockerContainer(containerIP, containerPort);
                }
            }
        }

		return dockerContainer;
	}

    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

        return restTemplate;
    }

}
