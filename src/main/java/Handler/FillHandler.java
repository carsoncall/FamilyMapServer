package Handler;

import Request.FillRequest;
import Result.FillResult;
import Service.FillService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class FillHandler implements HttpHandler {
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
        String username;
        FillRequest request;
        FillResult result;

        try {

            if (exchange.getRequestMethod().toLowerCase(Locale.ROOT).equals("post")) {
                String URI = exchange.getRequestURI().toString();
                System.out.print(URI);
                String[] URISplit = URI.split("/");

                if (URISplit.length >= 3) {
                    username = URISplit[2];
                    if (URISplit.length == 4) {
                        request = new FillRequest(username, Integer.parseInt(URISplit[3]));
                    } else {
                        request = new FillRequest(username);
                    }
                    FillService fillService = new FillService();
                    result = fillService.fill(request);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream resBody = exchange.getResponseBody();

                    try (Writer w = new OutputStreamWriter(resBody, StandardCharsets.UTF_8)) {
                        w.write(new Gson().toJson(result));
                    }

                } else {
                    //Username was not provided
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
            } else {
                //wrong HTTP method
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            System.out.print("There was an error with the FillHandler\n");
            e.printStackTrace();
        }

    }
}