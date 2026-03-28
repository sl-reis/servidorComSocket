package org.example.framework.http;

public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PATCH("PATCH");

    HttpMethod(String nomeMetodo) {
        this.nomeMetodo = nomeMetodo;
    }

    private final String nomeMetodo;

    public  String getNomeMetodo() {
        return nomeMetodo;
    }
}
