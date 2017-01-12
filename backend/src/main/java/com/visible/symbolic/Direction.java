package com.visible.symbolic;

public enum Direction {
    LEFT("stepLeft"),
    RIGHT("stepRight");

    private String endpoint;

    Direction(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}

