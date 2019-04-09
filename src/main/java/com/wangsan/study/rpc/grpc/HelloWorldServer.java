package com.wangsan.study.rpc.grpc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * created by wangsan on 2019-04-02 in project of example .
 *
 * @author wangsan
 * @date 2019-04-02
 */
public class HelloWorldServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50001;
        Server server = ServerBuilder.forPort(port).addService(new HelloWorldImpl()).build().start();
        LOGGER.info("gRPC server started: {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                server.shutdown();
                System.err.println("*** server shut down");
            }
        });

        server.awaitTermination();
    }
}
