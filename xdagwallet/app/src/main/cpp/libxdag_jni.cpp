#include <jni.h>
#include <pthread.h>
#include <android/log.h>
#include <string>
#include <xdaglib/client/xdagmain.h>
#include <xdaglib/wrapper/xdagwrapper.h>

#include "xdaglib/wrapper/xdagwrapper.h"

#ifndef LOGI(x...)
#endif
#define LOGI(x...) __android_log_print(ANDROID_LOG_INFO,"XdagWallet",x)

pthread_mutex_t g_process_mutex;

st_xdag_app_msg* XdagWalletProcessCallback(const void *call_back_object, st_xdag_event* event){

    switch (event->event_type){

        case en_event_type_pwd:
        {
            LOGI("request user type in password");
        }
        return NULL;

        case en_event_xdag_log_print:
        {
            LOGI("%s",event->app_log_msg);
        }
        return NULL;
    }
    return NULL;
}

extern "C"
JNIEXPORT jint JNICALL Java_com_xdag_wallet_XdagWrapper_XdagInit(
        JNIEnv *env,
        jobject *obj) {

    return 0;
}

extern "C"
JNIEXPORT jint JNICALL Java_com_xdag_wallet_XdagWrapper_XdagUnInit(
        JNIEnv *env,
        jobject *obj) {

    return 0;
}

extern "C"
JNIEXPORT jint JNICALL Java_com_xdag_wallet_XdagWrapper_XdagConnect(
        JNIEnv *env,
        jobject *obj,
        jstring poolAddr) {

    pthread_mutex_init(&g_process_mutex,NULL);

    jboolean isCopy = JNI_TRUE;
    const char* address = env->GetStringUTFChars(poolAddr,&isCopy);
    LOGI("pool address  %s",address);
    xdag_wrapper_init(NULL,XdagWalletProcessCallback);
    LOGI("xdag main start ");
    xdag_main(address);
    return 0;
}

extern "C"
JNIEXPORT jint JNICALL Java_com_xdag_wallet_XdagWrapper_XdagDisConnect(
        JNIEnv *env,
        jobject *obj) {

    return 0;
}


extern "C"
JNIEXPORT jint JNICALL Java_com_xdag_wallet_XdagWrapper_XdagXfer(
        JNIEnv *env,
        jobject *obj,
        jstring address,
        jstring amount) {

    return 0;
}

