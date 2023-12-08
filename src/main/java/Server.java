import java.io.*;
import java.net.*;

import Handler.*;
import com.sun.net.httpserver.*;

/**
 * This class is the main class that has the server and calls all handlers/other classes in the project. In other words,
 * this is the gateway to all the functions of the server. The server consists of a java HttpServer class that uses
 * contexts that call corresponding handlers for each web API.
 */

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    private void run(String portNumber) {
        System.out.print("Initializing the FamilyMap Server with port " + portNumber + "\n");

        try {
            //Creates a new HttpServer object.
            server = HttpServer.create( new InetSocketAddress(Integer.parseInt(portNumber)),MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            System.out.print("Failed to create a HttpServer object.");
            e.printStackTrace();
        }

        //necessary for server function
        server.setExecutor(null);

        //associates each URL path with a handler.
        System.out.print("Creating the contexts...\n");
        server.createContext("/", new FileHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear",new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());

        System.out.print("Starting server...\n");
        server.start();
        System.out.print("Server started successfully\n");
    }

    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
