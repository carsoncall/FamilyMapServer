package Handler;

import Request.EventRequest;
import Result.EventDataResult;
import Service.EventService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class EventHandler implements HttpHandler {
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
        String eventID;
        EventRequest request;
        EventDataResult result;

        try {
            if (exchange.getRequestMethod().toLowerCase(Locale.ROOT).equals("get")) {
                Headers requestHeaders = exchange.getRequestHeaders();

                if (requestHeaders.containsKey("Authorization")) {

                    String authtoken = requestHeaders.getFirst("Authorization");

                    String URI = exchange.getRequestURI().toString();
                    System.out.print(URI);
                    String[] URISplit = URI.split("/");

                    if (URISplit.length >= 3) {
                        eventID = URISplit[2];
                        request = new EventRequest(eventID,authtoken);
                    } else {
                        request = new EventRequest(null, authtoken);
                    }

                    EventService eventService = new EventService();
                    result = eventService.retrieve(request);

                    if (result.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                    }
                    OutputStream resBody = exchange.getResponseBody();
                    try (Writer w = new OutputStreamWriter(resBody, StandardCharsets.UTF_8)) {
                        w.write(new Gson().toJson(result));
                    }

                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                    //TODO: error json
                }

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                //TODO: error json
            }
        } catch (IOException e) {
            System.out.print("There was an error with the PersonHandler\n");
            e.printStackTrace();
        }
    }
}
