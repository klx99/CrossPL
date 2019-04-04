#ifndef _CROSSPL_CROSSBASE_HPP_
#define _CROSSPL_CROSSBASE_HPP_

namespace crosspl {
class CrossBase {
public:
  /*** type define ***/

  /*** static function and variable ***/

  /*** class function and variable ***/
  static uint64_t CreateNativeObject(const char* javaClassName);
  static void DestroyNativeObject(const char* javaClassName, uint64_t nativeHandle);

private:
  /*** type define ***/

  /*** static function and variable ***/
  static std::map<std::string, NativeObjectFactroy> _NativeObjectFactroyMap;

  /*** class function and variable ***/

} // class CrossBase
} // namespace crosspl

#endif /* _CROSSPL_CROSSBASE_HPP_ */
