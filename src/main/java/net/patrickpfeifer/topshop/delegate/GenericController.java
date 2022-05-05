package net.patrickpfeifer.topshop.delegate;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class GenericController implements HttpHandler {

    private final ContentProducer contentProducer; // a.k.a. "content production delegate"
    public GenericController(ContentProducer contentProducer) {
        this.contentProducer = contentProducer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] rb = (getHeader() + " | " + contentProducer.getContent()).getBytes();
        exchange.sendResponseHeaders(200, rb.length);
        exchange.getResponseBody().write(rb);
        exchange.getResponseBody().close();
    }

    String getHeader() {
        return "Top Shop";
    }
}
