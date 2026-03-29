package org.example;

import org.example.framework.*;
import org.example.framework.http.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final Integer NUMERO_PORTA = 8000;

    private static final Integer NUMERO_THREADS = 50;

    public static void main(String[] args) throws Exception {

        Router router = new Router();
        Dispatcher dispatcher = new Dispatcher();

        Locale localeUs = new Locale("en", "US");
        Locale localePtBr = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        DateTimeFormatter formatterBr = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy - HH:mm", localePtBr);
        logger.info("Default locale: " + Locale.getDefault());
        logger.info("Data locale US: " + formatter.withLocale(localeUs).format(ZonedDateTime.now()));
        logger.info("Data locale BR: " + formatter.withLocale(localePtBr).format(ZonedDateTime.now()));
        logger.info("Data locale BR2: " + formatterBr.format(ZonedDateTime.now()));

        try(ExecutorService threadPool = Executors.newFixedThreadPool(NUMERO_THREADS)) {
            try (ServerSocket server = new ServerSocket(NUMERO_PORTA)) {
                logger.info("Servidor iniciado na porta " + NUMERO_PORTA);

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
            logger.fine(() -> "Detalhes da requisição: " + reqText);

            HttpRequest req = HttpRequest.from(reqText);

            HttpResponse resp;
            //if (HttpMethod.GET.equals(req.getMethod()) && "/".equals(req.getPath())) {
            //    String html = recuperarHtml();
            //    resp = new HttpResponse(200,
            //            "OK",
            //            Map.of("Content-Type", "text/html; charset=UTF-8"),
            //            html,
            //            "HTTP/1.1");
            //} else {
                ControllerMethodMatch found = router.find(req);
                resp = dispatcher.dispatch(found, req);
            //}

            resp.write(out);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro no servidor", e);
        }
    }

    private static String recuperarHtml() {
        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Florinda Eats - Cardápio</title>
                    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@2.1.1/css/pico.min.css">
                </head>
                <body>
                
                <header class="container">
                    <hgroup>
                        <h1>Florinda Eats</h1>
                        <p>O sabor da Vila direto pra você</p>
                    </hgroup>
                </header>
                
                <main class="container">
                    <h2>Cardápio</h2>
                    
                    </main>
                
                    <footer class="container">
                        <p><small><em>Preços de acordo com 30 de Agosto de 2025 15:18</em></small></p>
                        <p><strong>Florinda Eats</strong> Todos os direitos reservados - Agosto/2025</p>
                    </footer>
                    </body>
                    </html>
                """;
        return html;
    }
}