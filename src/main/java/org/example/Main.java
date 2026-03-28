package org.example;

import org.example.framework.*;
import org.example.framework.http.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Integer NUMERO_PORTA = 8000;

    private static final Integer NUMERO_THREADS = 50;

    public static void main(String[] args) throws Exception {

        Router router = new Router();
        Dispatcher dispatcher = new Dispatcher();

        try(ExecutorService threadPool = Executors.newFixedThreadPool(NUMERO_THREADS)) {
            try (ServerSocket server = new ServerSocket(NUMERO_PORTA)) {
                System.out.println("Servidor iniciado na porta " + NUMERO_PORTA);

                while (true) {
                    Socket client = server.accept();
                    threadPool.submit(() -> tratarRequisicao(client, router, dispatcher));
                }
            }
        }
    }

    private static void tratarRequisicao(
            Socket clientSocket,
            Router router,
            Dispatcher dispatcher
    ) {
        try (clientSocket;
             InputStream clientIS = clientSocket.getInputStream();
             PrintStream out = new PrintStream(clientSocket.getOutputStream())) {
            StringBuilder requestBuilder = new StringBuilder();

            int data;
            do {
                data = clientIS.read();
                requestBuilder.append((char) data);
            } while (clientIS.available() > 0);

            String reqText = requestBuilder.toString();

            HttpRequest req = HttpRequest.from(reqText);

            ControllerMethodMatch found = router.find(req);

            HttpResponse resp = dispatcher.dispatch(found, req);

            resp.write(out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}