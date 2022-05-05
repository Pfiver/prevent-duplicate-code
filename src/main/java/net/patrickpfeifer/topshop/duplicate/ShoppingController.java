package net.patrickpfeifer.topshop.duplicate;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ShoppingController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] rb = (getHeader() + " | " + getContent()).getBytes();
        exchange.sendResponseHeaders(200, rb.length);
        exchange.getResponseBody().write(rb);
        exchange.getResponseBody().close();
    }

    private String getHeader() {
        return "Top Shop";
    }

    private String getContent() {
        return "Shop 'til you drop!";
    }
}
