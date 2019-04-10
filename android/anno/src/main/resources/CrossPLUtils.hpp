#ifndef _CROSSPL_CrossPLUtils_HPP_
#define _CROSSPL_CrossPLUtils_HPP_

#include <jni.h>
#include <map>
#include <memory>
#include <string>
#include <sstream>
#include <thread>
#include <vector>

#include <experimental-span.hpp>

namespace crosspl {

    class CrossPLUtils {
    public:
        /*** type define ***/

        /*** static function and variable ***/
        static std::shared_ptr<const char> SafeCastString(JNIEnv* jenv, jstring jdata);
        static std::shared_ptr<std::span<int8_t>> SafeCastByteArray(JNIEnv* jenv, jbyteArray jdata);
        static std::shared_ptr<std::stringstream> SafeCastStringBuffer(JNIEnv* jenv, jobject jdata);
        static std::shared_ptr<std::vector<int8_t>> SafeCastByteBuffer(JNIEnv* jenv, jobject jdata);

        static std::shared_ptr<_jstring> SafeCastString(JNIEnv* jenv, const char* data);
        static std::shared_ptr<_jbyteArray> SafeCastByteArray(JNIEnv* jenv, const std::span<int8_t>* data);
        static std::shared_ptr<_jobject> SafeCastStringBuffer(JNIEnv* jenv, const std::stringstream* data);
        static std::shared_ptr<_jobject> SafeCastByteBuffer(JNIEnv* jenv, const std::vector<int8_t>* data);

        static int SafeCopyStringBuffer(JNIEnv* jenv, jobject jcopyTo, const std::stringstream* data);
        static int SafeCopyByteBuffer(JNIEnv* jenv, jobject jcopyTo, const std::vector<int8_t>* data);

        template <class T>
        static T* SafeCastCrossObject(JNIEnv* jenv, jobject jdata) {
          void* ret = SafeCastCrossObject(jenv, jdata);
          return reinterpret_cast<T*>(ret);
        }

        /*** class function and variable ***/

    private:
        /*** type define ***/

        /*** static function and variable ***/
        static void EnsureRunOnThread(std::thread::id threadId);
        static jclass FindJavaClass(JNIEnv* jenv, const char* className);

        static void* SafeCastCrossObject(JNIEnv* jenv, jobject jdata);

//        static const char* JavaClassNameBoolean;
//        static const char* JavaClassNameInteger;
//        static const char* JavaClassNameLong;
//        static const char* JavaClassNameDouble;
        static const char* JavaClassNameString;
        static const char* JavaClassNameByteArray;
        static const char* JavaClassNameStringBuffer;
        static const char* JavaClassNameByteArrayOutputStream;
        static std::map<const char*, jclass> sJavaClassCache;

        /*** class function and variable ***/

    }; // class CrossPLUtils
} // namespace crosspl

#endif /* _CROSSPL_CrossPLUtils_HPP_ */

