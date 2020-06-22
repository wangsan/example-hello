package com.wangsan.study.protobuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.code.CodedConstant;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

public class JpbDatJProtoBufClassTest implements
        com.baidu.bjf.remoting.protobuf.Codec<com.wangsan.study.protobuf.JpbData> {
    private com.google.protobuf.Descriptors.Descriptor descriptor;

    public byte[] encode(com.wangsan.study.protobuf.JpbData t) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CodedOutputStream newInstance = CodedOutputStream.newInstance(baos);
        doWriteTo(t, newInstance);
        newInstance.flush();
        return baos.toByteArray();
    }

    public com.wangsan.study.protobuf.JpbData decode(byte[] bb) throws IOException {
        CodedInputStream input = CodedInputStream.newInstance(bb, 0, bb.length);
        return readFrom(input);
    }

    public int size(com.wangsan.study.protobuf.JpbData t) throws IOException {
        int size = 0;

        String f_1 = null;
        if (!CodedConstant.isNull(t.getName())) {
            f_1 = t.getName();
            size += com.google.protobuf.CodedOutputStream.computeStringSize(1, f_1);

        }

        Integer f_2 = null;
        if (!CodedConstant.isNull(t.getValue())) {
            f_2 = t.getValue();
            size += com.google.protobuf.CodedOutputStream.computeInt32Size(2, f_2.intValue());

        }

        Date f_3 = null;
        if (!CodedConstant.isNull(t.getDate())) {
            f_3 = t.getDate();
            size += com.google.protobuf.CodedOutputStream.computeInt64Size(3, f_3.getTime());

        }

        return size;
    }

    public void doWriteTo(com.wangsan.study.protobuf.JpbData t, CodedOutputStream output)
            throws IOException {

        String f_1 = null;
        if (!CodedConstant.isNull(t.getName())) {
            f_1 = t.getName();
            if (f_1 != null) {
                output.writeString(1, f_1);
            }

        }

        Integer f_2 = null;
        if (!CodedConstant.isNull(t.getValue())) {
            f_2 = t.getValue();
            if (f_2 != null) {
                output.writeInt32(2, f_2.intValue());
            }

        }

        Date f_3 = null;
        if (!CodedConstant.isNull(t.getDate())) {
            f_3 = t.getDate();
            if (f_3 != null) {
                output.writeInt64(3, f_3.getTime());
            }

        }

    }

    public void writeTo(com.wangsan.study.protobuf.JpbData t, CodedOutputStream output)
            throws IOException {
        doWriteTo(t, output);
    }

    public com.wangsan.study.protobuf.JpbData readFrom(CodedInputStream input) throws IOException {
        com.wangsan.study.protobuf.JpbData ret = new com.wangsan.study.protobuf.JpbData();

        try {
            boolean done = false;
            Codec codec = null;
            while (!done) {
                int tag = input.readTag();
                if (tag == 0) {
                    break;
                }

                if (tag == 10) {

                    ret.setName(input.readString())
                    ;

                    continue;
                }

                if (tag == 16) {

                    ret.setValue(input.readInt32())
                    ;

                    continue;
                }

                if (tag == 24) {

                    // TODO change me
//                    ret.setDate(input.readInt64())
                    ;

                    continue;
                }

                input.skipField(tag);
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw e;
        } catch (java.io.IOException e) {
            throw e;
        }

        return ret;

    }

    public com.google.protobuf.Descriptors.Descriptor getDescriptor() throws IOException {
        if (this.descriptor != null) {
            return this.descriptor;
        }
        com.google.protobuf.Descriptors.Descriptor descriptor =
                CodedConstant.getDescriptor(com.wangsan.study.protobuf.JpbData.class);
        return (this.descriptor = descriptor);
    }
}

    