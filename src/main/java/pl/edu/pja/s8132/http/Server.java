package pl.edu.pja.s8132.http;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;

public class Server extends AbstractVerticle{

    @Override
    public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestStream().toObservable().subscribe(httpServerRequest -> {
            httpServerRequest.response().
                    putHeader("content-type", "application/json").
                    end("{\"login\":\"s8132\", \"name\": \"Marcin Michalik\"}");
        });
        httpServer.listen(8080);
    }
}
