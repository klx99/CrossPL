//
//  CrossBase.h
//  lib
//
//  Created by mengxk on 2019/9/4.
//  Copyright © 2019 Elastos. All rights reserved.
//



#import "CrossPLFactory.h"

#import "CrossBase.proxy.h"
#import "CrossBase.hpp"

@implementation CrossPLFactory

+ (bool)onLoad
{
  printf("%s %d", __FUNCTION__, __LINE__);
//  void* dylib  = dlopen("libcrosspl.so", RTLD_LAZY | RTLD_GLOBAL);
  
  RegCreateCppObjFunc(createCppObject);
  RegDestroyCppObjFunc(destroyCppObject);
  
  return true;
}

int64_t createCppObject(const char* swiftClassName)
{
  printf("%s %s", __PRETTY_FUNCTION__, swiftClassName);
  void *ptr = nullptr;
  
  if(std::strcmp(swiftClassName, "org.elastos.tools.crosspl.CrossBase") == 0) {
    ptr = new CrossBase();
  }
  
  
  auto cppHandle = reinterpret_cast<int64_t>(ptr);
  return cppHandle;

}

int destroyCppObject(const char* swiftClassName, int64_t cppHandle)
{
  if(cppHandle == 0) {
    return -1;
  }

  printf("%s %s", __PRETTY_FUNCTION__, swiftClassName);
  if(std::strcmp(swiftClassName, "org.elastos.tools.crosspl.CrossBase") == 0) {
    delete reinterpret_cast<CrossBase*>(cppHandle);
    return 0;
  }

  return -1;
}

@end
