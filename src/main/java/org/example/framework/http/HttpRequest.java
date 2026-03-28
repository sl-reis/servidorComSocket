package org.example.framework.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;   // GET, POST, PUT, DELETE...
    private final String path;         // /cardapio
    private final String httpVersion;  // HTTP/1.1
    private final Map<String, String> headers; // Host: localhost, Content-type: ...
    private final String body;

    public HttpRequest(HttpMethod method, String path, String httpVersion, Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest from(String stringRequest) {
        String[] secoesRequest = stringRequest.split("\r?\n\r?\n", 2);

        String[] cabecalhos = secoesRequest[0].split("\r?\n");

        // --- Request Line: "GET /cardapio HTTP/1.1" ---
        String[] requestLine = cabecalhos[0].split(" ");
        HttpMethod method;
        try {
            method = HttpMethod.valueOf(requestLine[0].toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Método não suportado: " + requestLine[0]);
        }
        String path = requestLine[1];
        String httpVersion = requestLine[2];

        // --- Headers ---
        Map<String, String> mapCabecalho = new HashMap<>();
        for (int i = 1; i < cabecalhos.length; i++) {
            String[] partesCabecalho = cabecalhos[i].split(":", 2);
            if (partesCabecalho.length == 2) {
                mapCabecalho.put(partesCabecalho[0].trim(), partesCabecalho[1].trim());
            }
        }

        // --- Body (presente em POST, PUT, etc.) ---
        String body = secoesRequest.length > 1 ? secoesRequest[1].trim() : "";

        return new HttpRequest(method, path, httpVersion, mapCabecalho, body);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getBody() {
        return body;
    }
}
