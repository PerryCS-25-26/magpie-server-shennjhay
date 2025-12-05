import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Date;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

public class MagpieServer implements HttpHandler{
    private Magpie maggie = new Magpie();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        // Log the request
        System.out.println(new Date() + " " + requestMethod + " request for Magpie: " + requestPath);

        // Respnd to POST request from the chat client
        if ("POST".equals(requestMethod) && "/chat".equals(requestPath)) {
            String statement = new String(exchange.getRequestBody().readAllBytes());
            System.out.println("User said: " + statement);
            String response = maggie.getResponse(statement);
            System.out.println("Magpie responded" + response);

            // Send the response back to the user
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes());
            responseBody.close();

        }
        else {
            // If the request is not a POST request, return a 405 Method Not Allowed error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            exchange.getResponseBody().close();
        }
    }
    
    
    public static void main (String[] args) {
        // Set the default port to 8080 and the root folder to the public folder
        int port = 8080;
        String rootFolder = "./public";

        

        // Create and start a new FileServer to handle file requests
        try {
            final HttpServer server = FileServer.buildFileServer(port, rootFolder);
            server.createContext("/chat", new MagpieServer());
            server.start();
        }
        catch (IOException e) {
            System.err.println(new Date() + " Error starting server: " + e);
            System.exit(1); 
        }

        System.out.println(new Date() + " Server listening at http://localhost:" + port);
    }
}

