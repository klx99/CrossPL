#ifndef _CROSSPL_CROSSBASE_HPP_
#define _CROSSPL_CROSSBASE_HPP_

namespace crosspl {
class CrossBase {
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

} // class CrossBase
} // namespace crosspl

#endif /* _CROSSPL_CROSSBASE_HPP_ */
