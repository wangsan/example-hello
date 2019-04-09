package com.wangsan.study.rpc.grpc;

import io.grpc.stub.StreamObserver;

/**
 * created by wangsan on 2019-04-02 in project of example .
 *
 * @author wangsan
 * @date 2019-04-02
 */
public class HelloWorldImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
