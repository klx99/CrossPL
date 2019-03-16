#ifndef _CROSSPL_NATIVEBASE_HPP_
#define _CROSSPL_NATIVEBASE_HPP_

namespace crosspl {
class NativeBase {
public:
  /*** type define ***/
  typeof void* (NativeObjectFactro*)(const std::string& className);

  /*** static function and variable ***/
  static void RegisterNativeObjectFactroy(const std::string& className, NativeObjectFactroy factroy);

  /*** class function and variable ***/
  uint64_t createNativeObject(const std::string& className);
  void destroyNativeObject(uint64_t nativeHandle);

private:
  /*** type define ***/

  /*** static function and variable ***/
  static std::map<std::string, NativeObjectFactroy> _NativeObjectFactroyMap;

  /*** class function and variable ***/

} // class NativeBase
} // namespace crosspl

#endif /* _CROSSPL_NATIVEBASE_HPP_ */
