package com.wangsan.study.jni;

public class HelloWorldJniTest {
    public static void main(String[] args) {
        System.load("/Users/wangqingpeng/git/wangsangit/example/src/main/jniout/libhello.jnilib");

        HelloWorldJni helloWorldJni = new HelloWorldJni();
        String result = helloWorldJni.hello("wangsna");
        System.out.println("test again " + result);
    }

}