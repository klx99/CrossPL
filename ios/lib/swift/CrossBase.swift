//
//  CrossPL.swift
//  lib
//
//  Created by mengxk on 2019/9/4.
//  Copyright © 2019 Elastos. All rights reserved.
//

import Foundation

open class CrossBase : CrossBaseProxy {
  public init(className: String, nativeHandle: UInt64) {
    CrossBase.initializer
    
    self.className = className
    self.nativeHandle = nativeHandle

    super.init()
    
    if(self.nativeHandle == 0) {
      CrossBaseProxy.createNativeObject(self.className)
    }
  }
  
  deinit {
    CrossBaseProxy.destroyNativeObject(self.className, self.nativeHandle)
  }
  
  private let className: String
  private let nativeHandle: UInt64

  private static let initializer: Void = {
    CrossPLFactory.onLoad()
  }()
}
