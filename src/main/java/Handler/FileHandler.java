package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;


public class FileHandler implements HttpHandler {
    /**
     * Handle the given request and generate an appropriate response.
     * See {@link HttpExchange} for a description of the steps
     * involved in handling an exchange.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws NullPointerException if exchange is {@code null}
     * @throws IOException          if an I/O error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            if (exchange.getRequestMethod().toLowerCase(Locale.ROOT).equals("get")) {
                String urlPath = exchange.getRequestURI().toString();
                if (urlPath.equals("/")) {
                    urlPath = "/index.html";
                }
                if (urlPath.equals("/index.html") || urlPath.equals("/css/main.css")) {

                    urlPath = "web" + urlPath;
                    File html = new File(urlPath);

                    if (html.exists()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream resBody = exchange.getResponseBody();
                        Files.copy(html.toPath(), resBody);
                        resBody.close();
                    } else {
                        throw new IOException("File does not exist");
                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream resBody = exchange.getResponseBody();
                    Files.copy(Path.of("web/HTML/404.html"), resBody);
                    resBody.close();
                }

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }
        } catch (IOException e) {
            System.out.print("The Filehandler failed to handle file");
            e.printStackTrace();
        }
    }
}