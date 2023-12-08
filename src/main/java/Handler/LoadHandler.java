package Handler;

import Request.LoadRequest;
import Result.LoadResult;
import Service.LoadService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class LoadHandler implements HttpHandler {
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
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                LoadRequest request = new Gson().fromJson(reqData, LoadRequest.class);
                LoadService service = new LoadService();
                LoadResult result = service.load(request);

                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                }
                OutputStream resBody = exchange.getResponseBody();

                try (Writer w = new OutputStreamWriter(resBody, StandardCharsets.UTF_8)) {
                    w.write(new Gson().toJson(result));
                }
                resBody.close();
                success = true;
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }

        } catch (IOException e) {
            System.out.print("The LoadHandler failed\n");
            e.printStackTrace();
        } catch (JsonSyntaxException jsonSyntaxException) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            LoadResult loadResult = new LoadResult();
            loadResult.setSuccess(false); loadResult.setMessage("Error: Invalid request data");
            OutputStream resBody = exchange.getResponseBody();
            try (Writer w = new OutputStreamWriter(resBody, StandardCharsets.UTF_8)) {
                w.write(new Gson().toJson(loadResult));
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
