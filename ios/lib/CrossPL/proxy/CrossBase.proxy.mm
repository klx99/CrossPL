//
//  CrossBase.h
//  lib
//
//  Created by mengxk on 2019/9/4.
//  Copyright © 2019 Elastos. All rights reserved.
//

#import "CrossBase.proxy.h"
#import "CrossBase.hpp"

@implementation CrossBaseProxy

+ (int)registerNativeMethods
{
  return 0;
}

+ (int64_t)createNativeObject:(NSString*)swiftClassName
{
  const char *swiftClassNamePtr = [swiftClassName UTF8String];

  int64_t nativeHandle = CrossBase::CreateNativeObject(swiftClassNamePtr);

  return nativeHandle;
//  return 0;
}

+ (void)destroyNativeObject: (NSString*)swiftClassName: (int64_t)nativeHandle
{
  const char *swiftClassNamePtr = [swiftClassName UTF8String];
  
  CrossBase::DestroyNativeObject(swiftClassNamePtr, nativeHandle);
}

@end
