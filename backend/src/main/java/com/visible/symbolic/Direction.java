package com.visible.symbolic;

public enum Direction {
    LEFT("/stepleft"),
    RIGHT("/stepright");

    private String endpoint;

    Direction(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}

