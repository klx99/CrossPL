#include "CrossPLUtils.hpp"

namespace crosspl {

/***********************************************/
/***** static variables initialize *************/
/***********************************************/


/***********************************************/
/***** static function implement ***************/
/***********************************************/
void* CrossPLUtils::CastObject(JNIEnv* jenv, jobject jobj)
{
    jclass jclazz = jenv->GetObjectClass(jobj);

    jfieldID jfield = jenv->GetFieldID(jclazz, "nativeHandle", "J");

    jlong jnativeHandle = jenv->GetLongField(jobj, jfield);

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
