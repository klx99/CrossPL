//
//  test.cpp
//  lib
//
//  Created by mengxk on 2019/9/5.
//  Copyright © 2019 Elastos. All rights reserved.
//

#include "ExternalTest.hpp"


namespace crosspl {
namespace native {
    
void ExternalTest::externalTestFuncNoArgs()
{
  printf(__PRETTY_FUNCTION__);
}

void ExternalTest::externalTestFunc(bool a,
                      int32_t b,
                      int64_t c,
                      double d,
                      const char* e,
                      const std::span<int8_t>* f,
                      
                      std::stringstream* h,
                      std::vector<int8_t>* i)
{
    printf("%s a=%d,b=%d,c=%lld,d=%f,e=%s,f=%s(%d),h=%s,i=%s(%d)",
           __PRETTY_FUNCTION__, a, b, c, d, e, f->data(), f->size(), h->str().c_str(), i->data(), i->size());
    
    if(h != nullptr) {
      h->str("");
      (*h) << "new string from ExternalTest::externalTestFunc()";
    }
    
    if(i != nullptr) {
      i->clear();
      for(int idx = 0; idx < 256; idx+=5) {
        i->push_back(idx);
      }
    }
}


} //namespace native
} //namespace crosspl
