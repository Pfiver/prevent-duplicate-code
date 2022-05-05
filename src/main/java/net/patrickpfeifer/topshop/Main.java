package net.patrickpfeifer.topshop;

import com.sun.net.httpserver.HttpServer;
import net.patrickpfeifer.topshop.delegate.CheckoutContentProducer;
import net.patrickpfeifer.topshop.delegate.ShoppingContentProducer;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

public class Main {

    public static void main(String[] args) throws Exception {

        record Strategy(String name, Consumer<HttpServer> setup) {}

        Strategy[] strategies = {
                new Strategy("good", server -> {
                    server.createContext("/shop", new net.patrickpfeifer.topshop.duplicate.ShoppingController());
                    server.createContext("/checkout", new net.patrickpfeifer.topshop.duplicate.CheckoutController());
                }),
                new Strategy("better", server -> {
                    server.createContext("/shop", new net.patrickpfeifer.topshop.inherit.ShoppingController());
                    server.createContext("/checkout", new net.patrickpfeifer.topshop.inherit.CheckoutController());
                }),
                new Strategy("best", server -> {
                    server.createContext("/shop", new net.patrickpfeifer.topshop.delegate.GenericController(new ShoppingContentProducer()));
                    server.createContext("/checkout", new net.patrickpfeifer.topshop.delegate.GenericController(new CheckoutContentProducer()));
                })
        };

        for (Strategy strategy : strategies) {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            strategy.setup.accept(server);
            server.start();
            System.out.println(strategy);
            for (String path : asList("/shop", "/checkout")) {
                var request = HttpRequest.newBuilder(URI.create("http://localhost:8000" + path)).build();
                HttpClient.newHttpClient().sendAsync(request, BodyHandlers.ofString())
                        .thenApply(rsp -> "    " + path + " -> " + rsp.body())
                        .thenAccept(System.out::println)
                        .get();
            }
            System.out.println();
            server.stop(0);
        }
    }
}
