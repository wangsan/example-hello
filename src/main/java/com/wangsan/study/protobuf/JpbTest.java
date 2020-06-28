package com.wangsan.study.protobuf;

import java.io.File;
import java.io.IOException;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

/**
 * created by wangsan on 2020/5/5.
 *
 * @author wangsan
 */
public class JpbTest {
    public static void main(String[] args) throws IOException {
        String code = ProtobufIDLGenerator.getIDL(JpbData.class);
        System.out.println(code);

        Codec<JpbData> simpleTypeCodec = ProtobufProxy.create(JpbData.class);

        JpbData stt = new JpbData();
        stt.setName("abc");
        stt.setValue(100);

        // 序列化
        byte[] bb = simpleTypeCodec.encode(stt);
        // 反序列化
        JpbData to = simpleTypeCodec.decode(bb);
        System.out.println(to);

        // null
        JpbData nullDecode = simpleTypeCodec.decode(null);



        File path = new File("/Users/wangqingpeng/git/wangsangit/example/src/main/jprotopath");
        ProtobufProxy.enableCache(false);
        ProtobufProxy.create(JpbData.class, true, path);

        //        ProtobufIDLProxy.create(code, false, path);
    }
}
