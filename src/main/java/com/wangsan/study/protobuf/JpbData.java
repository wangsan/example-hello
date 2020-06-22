package com.wangsan.study.protobuf;

import java.lang.reflect.Field;
import java.util.Date;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

import lombok.Data;

@Data
@ProtobufClass
public class JpbData {

    //    @Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
    private String name;

    //    @Protobuf(fieldType = FieldType.INT32, order = 2, required = false)
    private int value;

    /**
     * !important 支持date,since jprotobuf 2.4.2
     *
     * @see com.baidu.bjf.remoting.protobuf.code.TemplateCodeGenerator#getSetToField(String, Field, Class, String, boolean, boolean, boolean)
     */

    @Protobuf(fieldType = FieldType.DATE)
    private Date date;

}