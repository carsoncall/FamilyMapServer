package Handler;


import Request.RegisterRequest;
import Result.RegisterResult;
import Service.RegisterService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class RegisterHandler implements HttpHandler {
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

        try {

            if (exchange.getRequestMethod().toLowerCase(Locale.ROOT).equals("post")) {
                // Pull information from the request
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                RegisterRequest request = new Gson().fromJson(reqData, RegisterRequest.class);

                RegisterService service = new RegisterService();
                RegisterResult result = service.register(request);

                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                }

                OutputStream resBody = exchange.getResponseBody();

                try (Writer w = new OutputStreamWriter(resBody, StandardCharsets.UTF_8)) {
                    w.write(new Gson().toJson(result));
                }
                success = true;
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }

        } catch (IOException e) {
            System.out.print("The RegisterHandler failed\n");
            e.printStackTrace();
        } catch (JsonSyntaxException jsonSyntaxException) {
            //If we got here, the json was invalid. We will handle the response right here.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            RegisterResult registerResult = new RegisterResult();
            registerResult.setSuccess(false); registerResult.setMessage("Error: Invalid registration data");
            OutputStream resBody = exchange.getResponseBody();
            try (Writer w = new OutputStreamWriter(resBody, StandardCharsets.UTF_8)) {
                w.write(new Gson().toJson(registerResult));
            }
            resBody.close();
        }
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf,0,len);
        }
        return sb.toString();
    }
}
