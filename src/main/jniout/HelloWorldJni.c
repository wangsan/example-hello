#include "jni.h"
#include "com_wangsan_study_jni_HelloWorldJni.h"
#include <stdio.h>

/*
 * Class:     com_wangsan_study_jni_HelloWorldJni
 * Method:    hello
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT void JNICALL Java_com_wangsan_study_jni_HelloWorldJni_hello(JNIEnv *env, jobject obj){
     printf("Hello World!\n");
     return;
}