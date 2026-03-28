package org.example.framework.http;

import java.io.PrintStream;
import java.util.Map;

public class HttpResponse {

    private int statusCode;          // 200, 404, 500...
    private String statusMessage;    // "OK", "Not Found", "Internal Server Error"
    private Map<String, String> headers; // "Content-Type": "application/json"
    private String body;             // JSON de retorno, mensagem de erro, etc.
    private String httpVersion;

    public HttpResponse() {
    }

    public HttpResponse(int statusCode, String statusMessage, Map<String, String> headers, String body, String httpVersion) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = headers;
        this.body = body;
        this.httpVersion = httpVersion;
    }

    public static HttpResponse ok(String body) {
        return new HttpResponse(200, "OK", Map.of("Content-Type", "application/json; charset=UTF-8"), body, "HTTP/1.1");
    }

    public static HttpResponse created(String body) {
        return new HttpResponse(201, "Created", Map.of("Content-Type", "application/json; charset=UTF-8"), body, "HTTP/1.1");
    }

    public static HttpResponse notFound(String body) {
        return new HttpResponse(404, "Not Found", Map.of("Content-Type", "application/json; charset=UTF-8"), body, "HTTP/1.1");
    }

    public static HttpResponse serverError(String body) {
        return new HttpResponse(500, "Internal Server Error", Map.of("Content-Type", "text/plain; charset=UTF-8"), body, "HTTP/1.1");
    }

    public static HttpResponse deleteSuccess(String body) {
        return new HttpResponse(204, "No Content", Map.of(), body, "HTTP/1.1");
    }

    public static HttpResponse patchSuccess(String body) {
        return new HttpResponse(204, "No Content", Map.of(), body, "HTTP/1.1");
    }

    public void write(PrintStream out) {
        out.println(httpVersion + " " + statusCode + " " + statusMessage);
        headers.forEach((k, v) -> out.println(k + ": " + v));
        out.println();  // linha em branco obrigatória do protocolo HTTP
        out.println(body);
    }


    public void send(PrintStream out) {
        write(out);
    }

}
