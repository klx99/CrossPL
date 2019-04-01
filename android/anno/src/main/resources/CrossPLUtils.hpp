#ifndef _crosspl_CrossPLUtils_HPP_
#define _crosspl_CrossPLUtils_HPP_

#include <jni.h>

namespace crosspl {

class CrossPLUtils {
public:
  /*** type define ***/

  /*** static function and variable ***/
  template <class T>
  static T* CastObject(JNIEnv* jenv, jobject jobj) {
    void* ret = CastObject(jenv, jobj);
    return reinterpret_cast<T*>(ret);
  }

  /*** class function and variable ***/

private:
  /*** type define ***/

  /*** static function and variable ***/
  static void* CastObject(JNIEnv* jenv, jobject jobj);

  /*** class function and variable ***/

}; // class CrossPLUtils
} // namespace crosspl

#endif /* _CROSSPL_crosspl_CrossPLUtils_HPP_ */
