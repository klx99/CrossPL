//
//  SwiftTestParams.swift
//  test
//
//  Created by mengxk on 2019/9/4.
//  Copyright © 2019 Elastos. All rights reserved.
//

import CrossPL

open class SwiftTestParams : CrossBase {
  init() {
    let className = type(of: self)
    super.init(className: "\(Bundle.main.bundleIdentifier).\(className)", nativeHandle: 0)
  }
  
}
