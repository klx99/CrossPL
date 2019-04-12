#ifndef _JAVA_TEST_PARAMS_HPP_
#define _JAVA_TEST_PARAMS_HPP_

#include <cstdint>
#include <experimental-span.hpp>
#include <span>
#include <sstream>
#include <vector>

class JavaTestParams {
public:
  /*** type define ***/

  /*** static function and variable ***/

  /*** class function and variable ***/
  int crossNativeMethod(bool a,
                        int32_t b,
                        int64_t c,
                        double d,
                        const char* e,
                        const std::span<int8_t>* f,
                        const std::function<void()>* g,
                        std::stringstream* h,
                        std::vector<int8_t>* i);

  bool crossNativeMethod0(bool a);
  int32_t crossNativeMethod1(int32_t a);
  int64_t crossNativeMethod2(int64_t a);
  double crossNativeMethod3(double a);
  const char* crossNativeMethod4(const char* a);
  std::span<int8_t> crossNativeMethod5(const std::span<int8_t>* a);
  std::function<void()> crossNativeMethod6(const std::function<void()>* a);
  std::stringstream crossNativeMethod7(std::stringstream* a);
  std::vector<int8_t> crossNativeMethod8(std::vector<int8_t>* a);

private:
  /*** type define ***/

  /*** static function and variable ***/

  /*** class function and variable ***/

}; // class JavaTestParams

#endif /* _JAVA_TEST_PARAMS_HPP_ */
