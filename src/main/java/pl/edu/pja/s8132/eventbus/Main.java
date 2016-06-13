package pl.edu.pja.s8132.eventbus;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Observable;

public class Main extends AbstractVerticle{

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Main.class.getName());
    }

    @Override
    public void start() throws Exception {
        System.out.println("Start");
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer("heatsensor1").toObservable().
                subscribe(message -> {
                    System.out.println("get 1");
                    message.reply(1);
                });

        eventBus.consumer("heatsensor2").toObservable().
                subscribe(message -> {
                    System.out.println("get 2");
                    message.reply(2);
                });


        Observable<Message<Integer>> replay1 = eventBus.<Integer>sendObservable("heatsensor1", "ping");
        Observable<Message<Integer>> replay2 = eventBus.<Integer>sendObservable("heatsensor2", "ping");
        Observable<Integer> replay = replay1.zipWith(replay2, (msg1, msg2) -> msg1.body() + msg2.body());

        replay.subscribe(sum -> {
            System.out.println("Suma: " + sum);
        }, err -> {
            System.out.println("error");
            err.printStackTrace();
        });

    }
}
