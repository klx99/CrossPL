//
//  CrossBase.h
//  lib
//
//  Created by mengxk on 2019/9/4.
//  Copyright © 2019 Elastos. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CrossPLFactory : NSObject
+ (bool)onLoad;

int64_t createCppObject(const char* swiftClassName);
int destroyCppObject(const char* swiftClassName, int64_t cppHandle);

@end
