//
//  CrossBase.h
//  lib
//
//  Created by mengxk on 2019/9/4.
//  Copyright © 2019 Elastos. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CrossBaseProxy : NSObject
+ (int)registerNativeMethods;

+ (Int64)createNativeObject:(NSString*)swiftClassName;
+ (void)destroyNativeObject: (NSString*)swiftClassName: (Int64)nativeHandle;


@end
