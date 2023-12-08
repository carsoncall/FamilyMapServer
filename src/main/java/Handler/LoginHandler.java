package Handler;

import Request.LoginRequest;
import Result.LoginResult;
import Service.LoginService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class LoginHandler implements HttpHandler {
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
                LoginRequest request = new Gson().fromJson(reqData, LoginRequest.class);
                LoginService service = new LoginService();
                LoginResult result = service.login(request);

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
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                //TODO: Write error JSON handler
            }

        } catch (IOException e) {
            System.out.print("The LoginHandler failed\n");
            e.printStackTrace();
        }
    }

    /**
     * Takes an InputStream and reads it using a buffer, and makes it into a String.
     * @param is the InputStream
     * @return a string with the contents of the InputString
     * @throws IOException if missing...?
     */
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
