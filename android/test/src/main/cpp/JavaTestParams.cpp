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
                                      std::stringstream* g,
                                      std::vector<int8_t>* h)
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

    sstream.str(""); // clear sstream
    if(g != nullptr) {
            sstream << g->str();
    } else {
            sstream << "nullptr";
    }
    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s std::stringstream* g=%s", __PRETTY_FUNCTION__, sstream.str().c_str());

    sstream.str(""); // clear sstream
    if(h != nullptr) {
        for(int idx = 0; idx < h->size(); idx++) {
            int8_t data = h->at(idx);
            sstream << std::setw(2) << std::hex << (data & 0xFF) << ' ';
        }
    } else {
            sstream << "nullptr";
    }
    __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s std::vector<int8_t>* h=%s", __PRETTY_FUNCTION__, sstream.str().c_str());

    if(g != nullptr) {
        g->str(""); // clear g
        *g << "set from native;";
    } else {
        __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s std::stringstream* g=null, ignore to set out value", __PRETTY_FUNCTION__);
    }

    if(h != nullptr) {
        h->clear();
        for(int idx = 0; idx < 256; idx+=5) {
            h->push_back(idx);
        }
    } else {
        __android_log_print(ANDROID_LOG_INFO, "crosspl", "%s std::vector<int8_t>* h=null, ignore to set out value", __PRETTY_FUNCTION__);
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

std::stringstream JavaTestParams::crossNativeMethod6(std::stringstream* a)
{
    return std::stringstream();
}

std::vector<int8_t> JavaTestParams::crossNativeMethod7(std::vector<int8_t>* a)
{
    return std::vector<int8_t>();
}


/***********************************************/
/***** class protected function implement  *****/
/***********************************************/


/***********************************************/
/***** class private function implement  *******/
/***********************************************/

