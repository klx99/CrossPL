#include "CrossPLUtils.hpp"

#include <android/log.h>
#include <vector>

namespace crosspl {

/***********************************************/
/***** static variables initialize *************/
/***********************************************/
//const char* CrossPLUtils::JavaClassNameBoolean      = "java/lang/Boolean";
//const char* CrossPLUtils::JavaClassNameInteger      = "java/lang/Integer";
//const char* CrossPLUtils::JavaClassNameLong         = "java/lang/Long";
//const char* CrossPLUtils::JavaClassNameDouble       = "java/lang/Double";
const char* CrossPLUtils::JavaClassNameString       = "java/lang/String";
const char* CrossPLUtils::JavaClassNameByteArray    = "[B";
const char* CrossPLUtils::JavaClassNameStringBuffer = "java/lang/StringBuffer";
const char* CrossPLUtils::JavaClassNameByteBuffer   = "java/nio/ByteBuffer";

std::map<const char*, jclass> CrossPLUtils::sJavaClassCache {};


/***********************************************/
/***** static function implement ***************/
/***********************************************/
void CrossPLUtils::EnsureRunOnThread(std::thread::id threadId)
{
    std::thread::id currThreadId = std::this_thread::get_id();
    if(currThreadId != threadId) {
        __android_log_print(ANDROID_LOG_FATAL, "crosspl", "Running on incorrect thread!!!");
        throw std::runtime_error("Running on incorrect thread");
    }
}

jclass CrossPLUtils::FindJavaClass(JNIEnv* jenv, const char* className)
{
    // CRASH if not found
    auto found = sJavaClassCache.find(className);
    if(found == sJavaClassCache.end()) {
        jclass clazz = jenv->FindClass(className);
        sJavaClassCache[className] = (jclass) jenv->NewGlobalRef(clazz);
        jenv->DeleteLocalRef(clazz);
    }


    return sJavaClassCache.at(className);
}

std::shared_ptr<const char> CrossPLUtils::SafeCastString(JNIEnv* jenv, jstring jdata)
{
    std::shared_ptr<const char> ret;
    std::thread::id threadId = std::this_thread::get_id();

    if(jdata == nullptr) {
        return ret; // nullptr
    }

    auto creater = [=]() -> const char* {
        EnsureRunOnThread(threadId);
        const char* ptr = jenv->GetStringUTFChars(jdata, nullptr);
        return ptr;
    };
    auto deleter = [=](const char* ptr) -> void {
        EnsureRunOnThread(threadId);
        jenv->ReleaseStringUTFChars(jdata, ptr);
    };

    ret = std::shared_ptr<const char>(creater(), deleter);

    return ret;
}

std::shared_ptr<_jstring> CrossPLUtils::SafeCastString(JNIEnv* jenv, const char* data)
{
    std::shared_ptr<_jstring> ret;
    std::thread::id threadId = std::this_thread::get_id();

    if (data == nullptr) {
        return ret; // nullptr
    }

    auto creater = [=]() -> jstring {
        EnsureRunOnThread(threadId);
        jstring ptr = jenv->NewStringUTF(data);
        return ptr;
    };

    auto deleter = [=](jstring ptr) -> void {
        EnsureRunOnThread(threadId);
        jenv->DeleteLocalRef(ptr);
    };

    ret = std::shared_ptr<_jstring>(creater(), deleter);

    return ret;
}

std::shared_ptr<std::span<int8_t>> CrossPLUtils::SafeCastByteArray(JNIEnv* jenv, jbyteArray jdata)
{
    std::shared_ptr<std::span<int8_t>> ret;
    std::thread::id threadId = std::this_thread::get_id();

    if(jdata == nullptr) {
        return ret; // nullptr
    }

    auto creater = [=]() -> std::span<int8_t>* {
        EnsureRunOnThread(threadId);
        jbyte* arrayPtr = jenv->GetByteArrayElements(jdata, nullptr);
        jsize arrayLen = jenv->GetArrayLength(jdata);
        auto retPtr = new std::span<int8_t>(arrayPtr, arrayLen);
        return retPtr;
    };
    auto deleter = [=](std::span<int8_t>* ptr) -> void {
        EnsureRunOnThread(threadId);
        jenv->ReleaseByteArrayElements(jdata, ptr->data(), JNI_ABORT);
        delete ptr;
    };

    ret = std::shared_ptr<std::span<int8_t>>(creater(), deleter);

    return ret;
}

std::shared_ptr<_jbyteArray> CrossPLUtils::SafeCastByteArray(JNIEnv* jenv, const std::span<int8_t>& data)
{
    std::shared_ptr<_jbyteArray> ret;
    std::thread::id threadId = std::this_thread::get_id();

    if(data.size() <= 0) {
        return ret; // nullptr
    }

    auto creater = [=]() -> jbyteArray {
        EnsureRunOnThread(threadId);
        jbyteArray jdata = jenv->NewByteArray(data.size());
        jenv->SetByteArrayRegion(jdata, 0, data.size(), data.data());
        return jdata;
    };
    auto deleter = [=](jbyteArray ptr) -> void {
        EnsureRunOnThread(threadId);
        jenv->DeleteLocalRef(ptr);
    };

    ret = std::shared_ptr<_jbyteArray>(creater(), deleter);

    return ret;
}

std::shared_ptr<std::stringstream> CrossPLUtils::SafeCastStringBuffer(JNIEnv* jenv, jobject jdata)
{
    std::shared_ptr<std::stringstream> ret;
    std::thread::id threadId = std::this_thread::get_id();

    if(jdata == nullptr) {
        return ret; // nullptr
    }

    auto creater = [=]() -> std::stringstream* {
        EnsureRunOnThread(threadId);

        jclass jclazz = FindJavaClass(jenv, JavaClassNameStringBuffer);
        jmethodID jmethod = jenv->GetMethodID(jclazz, "toString", "()Ljava/lang/String;");
        jstring jstr = static_cast<jstring>(jenv->CallObjectMethod(jdata, jmethod));

        auto str = SafeCastString(jenv, jstr);
        auto retPtr = new std::stringstream(str.get());

        return retPtr;
    };
    auto deleter = [=](std::stringstream* ptr) -> void {
        EnsureRunOnThread(threadId);
        delete ptr;
    };

    ret = std::shared_ptr<std::stringstream>(creater(), deleter);

    return ret;
}

std::shared_ptr<_jobject> CrossPLUtils::SafeCastStringBuffer(JNIEnv* jenv, const std::stringstream& data)
{
    std::shared_ptr<_jobject> ret;
    std::thread::id threadId = std::this_thread::get_id();

    auto creater = [&]() -> jobject {
        EnsureRunOnThread(threadId);

        jclass jclazz = FindJavaClass(jenv, JavaClassNameStringBuffer);

        jmethodID jconstructor  = jenv->GetMethodID(jclazz, "<init>", "(Ljava/lang/String;)V");

        auto jstrPtr = SafeCastString(jenv, data.str().c_str());
        jobject jobj = jenv->NewObject(jclazz, jconstructor, jstrPtr.get());

        return jobj;
    };
    auto deleter = [=](jobject ptr) -> void {
        EnsureRunOnThread(threadId);
        jenv->DeleteLocalRef(ptr);
    };

    ret = std::shared_ptr<_jobject>(creater(), deleter);

    return ret;
}

std::shared_ptr<std::vector<int8_t>> CrossPLUtils::SafeCastByteBuffer(JNIEnv* jenv, jobject jdata)
{
    std::shared_ptr<std::vector<int8_t>> ret;
    std::thread::id threadId = std::this_thread::get_id();

    if(jdata == nullptr) {
        return ret; // nullptr
    }

    auto creater = [=]() -> std::vector<int8_t>* {
        EnsureRunOnThread(threadId);

        jclass jclazz = jenv->GetObjectClass(jdata);
        jmethodID jmethod = jenv->GetMethodID(jclazz, "array", "()[B");
        jbyteArray jbytes = static_cast<jbyteArray>(jenv->CallObjectMethod(jdata, jmethod));

        auto bytes = SafeCastByteArray(jenv, jbytes);
        auto retPtr = new std::vector<int8_t>(bytes->data(), bytes->data() + bytes->size());

        return retPtr;
    };
    auto deleter = [=](std::vector<int8_t>* ptr) -> void {
        EnsureRunOnThread(threadId);
        delete ptr;
    };

    ret = std::shared_ptr<std::vector<int8_t>>(creater(), deleter);

    return ret;
}

std::shared_ptr<_jobject> CrossPLUtils::SafeCastByteBuffer(JNIEnv* jenv, const std::vector<int8_t>& data)
{
    std::shared_ptr<_jobject> ret;
    std::thread::id threadId = std::this_thread::get_id();

    auto creater = [&]() -> jobject {
        EnsureRunOnThread(threadId);

        jclass jclazz = FindJavaClass(jenv, JavaClassNameByteBuffer);

        jmethodID jmethod  = jenv->GetMethodID(jclazz, "wrap", "([B)Ljava/nio/ByteBuffer;");

        auto spanData = std::span<int8_t>(const_cast<int8_t*>(data.data()), data.size());
        auto jbytesPtr = SafeCastByteArray(jenv, spanData);
        jobject jobj = jenv->CallStaticObjectMethod(jclazz, jmethod, jbytesPtr.get());

        return jobj;
    };
    auto deleter = [=](jobject ptr) -> void {
        EnsureRunOnThread(threadId);
        jenv->DeleteLocalRef(ptr);
    };

    ret = std::shared_ptr<_jobject>(creater(), deleter);

    return ret;
}

void* CrossPLUtils::SafeCastCrossObject(JNIEnv* jenv, jobject jdata)
{
    jclass jclazz = jenv->GetObjectClass(jdata);

    jfieldID jfield = jenv->GetFieldID(jclazz, "nativeHandle", "J");

    jlong jnativeHandle = jenv->GetLongField(jdata, jfield);

    return reinterpret_cast<void*>(jnativeHandle);
}


/***********************************************/
/***** class public function implement  ********/
/***********************************************/


/***********************************************/
/***** class protected function implement  *****/
/***********************************************/


/***********************************************/
/***** class private function implement  *******/
/***********************************************/


} // namespace crosspl
