package net.patrickpfeifer.topshop.inherit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class BaseController implements HttpHandler {

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

    protected abstract String getContent();
}
