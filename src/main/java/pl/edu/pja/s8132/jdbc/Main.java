package pl.edu.pja.s8132.jdbc;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Observable;

public class Main extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Main.class.getName());
    }

    @Override
    public void start() throws Exception {
        JsonObject config = new JsonObject().put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                .put("driver_class", "org.hsqldb.jdbcDriver");

        JDBCClient jdbcClient = JDBCClient.createShared(vertx, config);

        jdbcClient.getConnectionObservable().subscribe(sqlConnection -> {
            Observable<ResultSet> resultSet = sqlConnection.updateObservable("CREATE TABLE test(col INTEGER)").
                    flatMap(result -> sqlConnection.updateObservable("INSERT INTO test (col) VALUES (1)")).
                    flatMap(result -> sqlConnection.updateObservable("INSERT INTO test (col) VALUES (2)")).
                    flatMap(result -> sqlConnection.queryObservable("SELECT * FROM test"));

            resultSet.subscribe(rs -> {
                System.out.println("Result: " + rs.getRows());
            });

        }, Throwable::printStackTrace);
    }
}
