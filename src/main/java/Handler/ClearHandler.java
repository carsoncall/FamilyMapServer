package Handler;

import Result.ClearResult;
import Service.ClearService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class ClearHandler implements HttpHandler {
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
        boolean success = false;
        ClearResult result;

        try {

            if (exchange.getRequestMethod().toLowerCase(Locale.ROOT).equals("post")) {
                ClearService clearService = new ClearService();
                result = clearService.clear();

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                OutputStream resBody = exchange.getResponseBody();

                try (Writer w = new OutputStreamWriter(resBody, StandardCharsets.UTF_8)) {
                    w.write(new Gson().toJson(result));
                }
                success = true;

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            }

        } catch (IOException e ) {
            System.out.print("There was an error with the ClearHandler\n");
            e.printStackTrace();
        }
    }
}
