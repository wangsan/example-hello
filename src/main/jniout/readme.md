1. javac HelloWorldJni.java. maven path is  target.
2. javah -jni -classpath /Users/wangqingpeng/git/wangsangit/example/target/classes com.wangsan.study.jni.HelloWorldJni

3. gcc -dynamiclib -I /Library/Java/JavaVirtualMachines/jdk1.8.0_191.jdk/Contents/Home/include/ HelloWorldJni.c -o libhello.jnilib