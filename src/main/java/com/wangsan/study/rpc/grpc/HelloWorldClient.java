package com.wangsan.study.rpc.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

/**
 * created by wangsan on 2019-04-02 in project of example .
 *
 * @author wangsan
 * @date 2019-04-02
 */
public class HelloWorldClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldClient.class);

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50001).usePlaintext().build();
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);

        String name = "wangsan";
        LOGGER.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            LOGGER.info("RPC failed: {}", e.getStatus());
            return;
        }
        LOGGER.info("Greeting: " + response.getMessage());
    }
}
