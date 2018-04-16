#include <jni.h>
#include <pthread.h>
#include <android/log.h>
#include <string>
#include <xdaglib/client/xdagmain.h>
#include <xdaglib/wrapper/xdagwrapper.h>
#include "xdaglib/wrapper/xdagwrapper.h"

#define CLAZZ_XDAG_EVENT "com/xdag/wallet/XdagEvent"
#define CLAZZ_XDAG_WRAPPER "com/xdag/wallet/XdagWrapper"

/*
 * global jvm
 * */
_JavaVM *gJvm;

/**
 * mapping class of java layer
 * */
static jclass gclazzXdagEvent = NULL;
static jclass gclazzXdagWrapper = NULL;
static jmethodID gNewXdagEventMethod = NULL;
static jmethodID gProcessNativeMethod = NULL;

#ifndef LOGI(x...)
#endif
#define LOGI(x...) __android_log_print(ANDROID_LOG_INFO,"XdagWallet",x)

pthread_mutex_t g_process_mutex;

void invokeJavaCallBack(st_xdag_event *event);

extern "C"
JNIEXPORT jint JNICALL  JNI_OnLoad(JavaVM *ajvm, void *reserved)
{
    LOGI(" library load on call jni ");
    jclass tmpClazz;
    jmethodID tmpMethodID;
    jint result = -1;
    JNIEnv *currentEnv = NULL;

    gJvm = ajvm;
    /**
     * get JNIEnv of current thread
     * */
    if (gJvm->GetEnv((void **) &currentEnv, JNI_VERSION_1_4)) {
        LOGI(" on jni load get current env failed ");
        return result;
    }

    /**
     * mapping java class to c++ class
     * */
    tmpClazz = currentEnv->FindClass(CLAZZ_XDAG_EVENT);
    if(tmpClazz == NULL){
        LOGI(" can not find class  %s" ,CLAZZ_XDAG_EVENT);
        return result;
    }
    gclazzXdagEvent = (jclass)currentEnv->NewGlobalRef(tmpClazz);

    tmpClazz = currentEnv->FindClass(CLAZZ_XDAG_WRAPPER);
    if(tmpClazz == NULL){
        LOGI(" can not find class  %s" ,CLAZZ_XDAG_WRAPPER);
        return result;
    }
    gclazzXdagWrapper = (jclass)currentEnv->NewGlobalRef(tmpClazz);

    tmpMethodID = currentEnv->GetMethodID(gclazzXdagEvent,"<init>","(ILjava/lang/String;)V");
    if(tmpMethodID == NULL){
        LOGI(" can not find method id EventInfo 111");
        return result;
    }
    gNewXdagEventMethod = tmpMethodID;

    tmpMethodID = currentEnv->GetStaticMethodID(gclazzXdagWrapper,"nativeCallbackFunc","(Lcom/xdag/wallet/XdagEvent;)V");
    if(tmpMethodID == NULL){
        LOGI(" can not find method id nativeCallbackFunc");
        return result;
    }
    gProcessNativeMethod = tmpMethodID;

    return JNI_VERSION_1_4;
}

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

        case en_event_set_pwd:
        {
            LOGI("xdag request user type in password");

        }
        default:
            invokeJavaCallBack(event);
            break;
    }
    return NULL;
}

void invokeJavaCallBack(st_xdag_event *event) {
    bool isAttacked = false;
    JNIEnv *currentEnv;

    int status =gJvm->GetEnv((void **) &currentEnv, JNI_VERSION_1_4);
    if(status < 0){
        status = gJvm->AttachCurrentThread(&currentEnv, NULL);

        if(status < 0) {
            LOGI("event_callback_func: failed to attach current thread");
            return;
        }
        isAttacked = true;
    }

    if(NULL == gclazzXdagEvent || NULL == gclazzXdagWrapper){
        LOGI(" class %s and %s has not mapped yet",gclazzXdagEvent,gclazzXdagWrapper);
        gJvm->DetachCurrentThread();
        return;
    }

    /**
     * 找到EventInfo的构造方法，并实例化对象
     * */
    if(NULL == gNewXdagEventMethod){
        LOGI(" can not find construct function of EventInfo ");
        gJvm->DetachCurrentThread();
        return;
    }

    /**
     * 将C++当中的类类型转成java当中引用的类型,int类型是基本类型，不用转换
     * */
    jint jmsgNo = event->msgNo;

    jstring jmsgData = currentEnv->NewStringUTF(eventInfo->msgData.c_str());
    jobject jeventInfo = currentEnv->NewObject(gclazzXdagEvent,gNewXdagEventMethod,jmsgNo,jmsgData);
    if(NULL == jeventInfo){
        LOGI(" can construct object of EventInfo ");
        gJvm->DetachCurrentThread();
        return;
    }

    /**
     * 回调java当中的方法，把对象当做参数进行传递
     * */
    if(NULL == gProcessNativeMethod){
        LOGI("can not find callback function of JNIProcessor");
        gJvm->DetachCurrentThread();
        return;
    }

    currentEnv->CallStaticVoidMethod(gclazzXdagWrapper,gProcessNativeMethod,jeventInfo);

//error:
    /**
     * 回调完成后一定要释放JNI环境
     * */
    if(isAttacked){
        gJvm->DetachCurrentThread();
    }
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

