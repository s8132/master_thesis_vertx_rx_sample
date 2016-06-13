package pl.edu.pja.s8132.http;

import io.vertx.core.http.HttpMethod;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;
import io.vertx.rxjava.core.http.HttpClientResponse;

public class Main extends AbstractVerticle{

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Main.class.getName());
        Vertx.vertx().deployVerticle(Server.class.getName());
    }

    static class User {
        public String login;
        public String name;
    }

    @Override
    public void start() throws Exception {
        HttpClient client = vertx.createHttpClient();
        HttpClientRequest request = client.request(HttpMethod.GET, 8080, "localhost", "/");

        request.toObservable().
                flatMap(HttpClientResponse::toObservable).
                lift(RxHelper.unmarshaller(User.class)).
                subscribe(user -> {
                    System.out.println("user login: " + user.login);
                    System.out.println("user name: " + user.name);
                });

        request.end();
    }
}
