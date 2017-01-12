package com.visible.symbolic.docker;

public class DockerContainer {
    private String ip;
    private int port;

    public DockerContainer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}
