package com.wangsan.study.protobuf;

import java.util.Arrays;
import java.util.Date;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * created by wangsan on 2020/5/5.
 *
 * @author wangsan
 */
public class StuffTest {
    public static void main(String[] args) {
        StuffData source = new StuffData();
        source.setAge(1);
        source.setName("san");
        source.setBirthday(new Date());

        // proto stuff
        Schema<StuffData> schema = RuntimeSchema.getSchema(StuffData.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] result = ProtostuffIOUtil.toByteArray(source, schema, buffer);

        System.out.println(result.length);
        System.out.println(Arrays.toString(result));

        StuffData to = new StuffData();
        ProtostuffIOUtil.mergeFrom(result, to, schema);
        System.out.println(to);

        // null will make jvm crash
        //ProtostuffIOUtil.toByteArray(null,schema,LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

        // proto buf
        LinkedBuffer buffer2 = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        result = ProtobufIOUtil.toByteArray(source, schema, buffer2);
        System.out.println(result.length);
        System.out.println(Arrays.toString(result));
        to = new StuffData();
        ProtobufIOUtil.mergeFrom(result, to, schema);
        System.out.println(to);
    }
}
