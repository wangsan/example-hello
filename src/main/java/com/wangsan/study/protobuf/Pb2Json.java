package com.wangsan.study.protobuf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.wangsan.study.rpc.grpc.HelloRequest;

/**
 * created by wangsan on 2020/5/11.
 *
 * @author wangsan
 */
public class Pb2Json {

    public static void main(String[] args) throws JsonProcessingException, InvalidProtocolBufferException {
        JsonFormat.Parser parser = JsonFormat.parser();
        JsonFormat.Printer printer = JsonFormat.printer();

        HelloRequest.Builder builder = HelloRequest.newBuilder();
        HelloRequest helloRequest = builder.setName("test").build();
        String json = printer.print(helloRequest);
        System.out.println(json);

        HelloRequest.Builder builder2 = HelloRequest.newBuilder();
        parser.merge(json, builder2);
        System.out.println(builder2.build());

    }

    public static class Echo {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
