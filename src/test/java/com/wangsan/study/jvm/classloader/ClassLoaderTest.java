package com.wangsan.study.jvm.classloader;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import com.wangsan.study.jvm.classloader.encode.DeClassLoader;
import com.wangsan.study.jvm.classloader.encode.FileUtils;

/**
 * created by wangsan on 2019-03-19 in project of example .
 *
 * @author wangsan
 * @date 2019-03-19
 */
public class ClassLoaderTest {

    @Test
    public void testClassLoaderEnv() {
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));
    }

    @Test
    public void testLoadFrom() {
        System.out.println(ClassLoaderTest.class.getClassLoader());
        System.out.println(ClassLoaderTest.class.getClassLoader().getParent());
        System.out.println(ClassLoaderTest.class.getClassLoader().getParent().getParent()); // null
        Assert.assertNull("ext get parent is null!", ClassLoaderTest.class.getClassLoader().getParent().getParent());
        System.out.println(Integer.class.getClassLoader()); // null
    }

    @Test
    public void testDiskClassLoader() {
        String path = "/Users/wangqingpeng/git/wangsangit/example/src/test/classloadertest";
        DiskClassLoader diskLoader = new DiskClassLoader(path);
        try {
            //加载class文件
            Class c = diskLoader.loadClass("com.wangsan.study.jvm.classloader.Test");

            if (c != null) {
                try {
                    Object obj = c.newInstance();
                    Method method = c.getDeclaredMethod("say", null);
                    //通过反射调用Test类的say方法
                    method.invoke(obj, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // https://blog.csdn.net/briblue/article/details/54973413
    @Test
    public void encodeClass() {
        String path = "/Users/wangqingpeng/git/wangsangit/example/src/test/classloadertest";
        FileUtils.test(path + "/Test.class");
    }

    @Test
    public void testDeClassLoader() {
        String path = "/Users/wangqingpeng/git/wangsangit/example/src/test/classloadertesten";
        DeClassLoader deClassLoader = new DeClassLoader(path);
        try {
            //加载class文件
            Class c = deClassLoader.loadClass("com.wangsan.study.jvm.classloader.Test");

            if (c != null) {
                try {
                    Object obj = c.newInstance();
                    Method method = c.getDeclaredMethod("say", null);
                    //通过反射调用Test类的say方法
                    method.invoke(obj, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // ContextClassLoader与线程相关，可以获取和设置，可以绕过双亲委托的机制
    @Test
    public void testContextClassLoader() {
        String path = "/Users/wangqingpeng/git/wangsangit/example/src/test/classloadercontext/one";
        DiskClassLoader1 diskLoader1 = new DiskClassLoader1(path);
        Class cls1 = null;
        try {
            //加载class文件
            cls1 = diskLoader1.loadClass("com.wangsan.study.jvm.classloader.context.SpeakTest");
            System.out.println(cls1.getClassLoader().toString());
            if (cls1 != null) {
                try {
                    Object obj = cls1.newInstance();
                    //SpeakTest1 speak = (SpeakTest1) obj;
                    //speak.speak();
                    Method method = cls1.getDeclaredMethod("speak", null);
                    //通过反射调用Test类的speak方法
                    method.invoke(obj, null);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String path2 = "/Users/wangqingpeng/git/wangsangit/example/src/test/classloadercontext/two";
        DiskClassLoader diskLoader = new DiskClassLoader(path2);

        Thread.currentThread().setContextClassLoader(diskLoader1);
        Thread.currentThread().setContextClassLoader(diskLoader);
        System.out.println("Thread " + Thread.currentThread().getName() + " classloader: " + Thread.currentThread()
                                                                                                     .getContextClassLoader()
                                                                                                     .toString());
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println(
                        "Thread " + Thread.currentThread().getName() + " classloader: " + Thread.currentThread()
                                                                                                  .getContextClassLoader()
                                                                                                  .toString());

                try {
                    //加载class文件
                    //	Thread.currentThread().setContextClassLoader(diskLoader);
                    //Class c = diskLoader.loadClass("com.frank.test.SpeakTest");
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    Class c = cl.loadClass("com.wangsan.study.jvm.classloader.context.SpeakTest");
                    // Class c = Class.forName("com.frank.test.SpeakTest");
                    System.out.println(c.getClassLoader().toString());
                    if (c != null) {
                        try {
                            Object obj = c.newInstance();
                            //SpeakTest1 speak = (SpeakTest1) obj;
                            //speak.speak();
                            Method method = c.getDeclaredMethod("speak", null);
                            //通过反射调用Test类的say方法
                            method.invoke(obj, null);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
