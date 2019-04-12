//
//  JavaTestParams.cpp
//
//  Created by mengxk on 19/03/16.
//  Copyright © 2016 mengxk. All rights reserved.
//

#include "JavaTestParams.hpp"
#include <android/log.h>
#include <inttypes.h>
#include <iomanip>

/***********************************************/
/***** static variables initialize *************/
/***********************************************/


/***********************************************/
/***** static function implement ***************/
/***********************************************/


/***********************************************/
/***** class public function implement  ********/
/***********************************************/
int JavaTestParams::crossNativeMethod(bool a,
                                      int32_t b,
                                      int64_t c,
                                      double d,
                                      const char* e,
                                      const std::span<int8_t>* f,
                                      const std::function<void()>* g,
                                      std::stringstream* h,
                                      std::vector<int8_t>* i)
{
    std::stringstream sstream;

    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s bool a=%d", __PRETTY_FUNCTION__, a);
    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s int32_t b=%d", __PRETTY_FUNCTION__, b);
    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s int64_t c=%" PRId64, __PRETTY_FUNCTION__, c);
    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s double d=%lf", __PRETTY_FUNCTION__, d);
    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s const char* e=%s", __PRETTY_FUNCTION__, e);

    if(f != nullptr) {
        for(int idx = 0; idx < f->size(); idx++) {
            int8_t data = f->data()[idx];
            sstream << std::setw(2) << std::hex << (data & 0xFF) << ' ';
        }
    } else {
            sstream << "nullptr";
    }
    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s const std::span<int8_t>* f=%s", __PRETTY_FUNCTION__, sstream.str().c_str());

    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s std::function<void()>* g=%p", __PRETTY_FUNCTION__, g);

    sstream.str(""); // clear sstream
    if(h != nullptr) {
            sstream << h->str();
    } else {
            sstream << "nullptr";
    }
    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s std::stringstream* h=%s", __PRETTY_FUNCTION__, sstream.str().c_str());

    sstream.str(""); // clear sstream
    if(i != nullptr) {
        for(int idx = 0; idx < i->size(); idx++) {
            int8_t data = i->at(idx);
            sstream << std::setw(2) << std::hex << (data & 0xFF) << ' ';
        }
    } else {
            sstream << "nullptr";
    }
    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s std::vector<int8_t>* i=%s", __PRETTY_FUNCTION__, sstream.str().c_str());

    if(h != nullptr) {
        h->str(""); // clear h
        *h << "set from native native;";
    } else {
        __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s std::stringstream* h=null, ignore to set out value", __PRETTY_FUNCTION__);
    }

    if(i != nullptr) {
        i->clear();
        for(int idx = 0; idx < 256; idx+=5) {
            i->push_back(idx);
        }
    } else {
        __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s std::vector<int8_t>* i=null, ignore to set out value", __PRETTY_FUNCTION__);
    }

    return 0;
}

bool JavaTestParams::crossNativeMethod0(bool a)
{
    return true;
}

int32_t JavaTestParams::crossNativeMethod1(int32_t a)
{
    return 0;
}

int64_t JavaTestParams::crossNativeMethod2(int64_t a)
{
    return 0;
}

double JavaTestParams::crossNativeMethod3(double a)
{
    return 0;
}

const char* JavaTestParams::crossNativeMethod4(const char* a)
{
    return nullptr;
}

std::span<int8_t> JavaTestParams::crossNativeMethod5(const std::span<int8_t>* a)
{
    return std::span<int8_t>(nullptr, 0);
}

std::function<void()> JavaTestParams::crossNativeMethod6(const std::function<void()>* a)
{

}

std::stringstream JavaTestParams::crossNativeMethod7(std::stringstream* a)
{
    return std::stringstream();
}

std::vector<int8_t> JavaTestParams::crossNativeMethod8(std::vector<int8_t>* a)
{
    return std::vector<int8_t>();
}


/***********************************************/
/***** class protected function implement  *****/
/***********************************************/


/***********************************************/
/***** class private function implement  *******/
/***********************************************/

