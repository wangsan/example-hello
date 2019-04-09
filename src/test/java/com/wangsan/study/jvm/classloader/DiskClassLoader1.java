package com.wangsan.study.jvm.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * created by wangsan on 2019-03-19 in project of example .
 *
 * @author wangsan
 * @date 2019-03-19
 */
public class DiskClassLoader1 extends ClassLoader {
    private String path;

    public DiskClassLoader1(String path) {
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = getFileName(name);

        File file = new File(path, fileName);
        try {
            FileInputStream is = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = bos.toByteArray();
            is.close();
            bos.close();

            return defineClass(name, data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    private String getFileName(String name) {
        int index = name.lastIndexOf('.');
        if (index == -1) {
            return name + ".class";
        } else {
            return name.substring(index + 1) + ".class";
        }
    }
}
