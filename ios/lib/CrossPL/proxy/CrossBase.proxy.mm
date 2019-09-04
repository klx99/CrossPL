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

+ (UInt64)createNativeObject:(NSString*)swiftClassName
{
    const char *swiftClassNamePtr = [swiftClassName UTF8String];
  
    UInt64 nativeHandle = CrossBase::CreateNativeObject(swiftClassNamePtr);
  
    return nativeHandle;
}

+ (void)destroyNativeObject: (NSString*)swiftClassName: (UInt64)nativeHandle
{
  const char *swiftClassNamePtr = [swiftClassName UTF8String];
  
  CrossBase::DestroyNativeObject(swiftClassNamePtr, nativeHandle);
}

@end
