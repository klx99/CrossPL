import Foundation

/* @CrossClass */
open class CrossBase : CrossBaseProxy {
  public init(className: String, nativeHandle: Int64) {
    CrossBase.initializer
    
    self.className = className
    self.nativeHandle = nativeHandle

    super.init()
    
    if(self.nativeHandle == 0) {
      self.nativeHandle = CrossBase.createNativeObject(swiftClassName: self.className)
    }
  }
  
  deinit {
    CrossBase.destroyNativeObject(swiftClassName: self.className, nativeHandle: self.nativeHandle)
  }
  
  private let className: String
  private var nativeHandle: Int64

  private static let initializer: Void = {
    CrossPLFactory.onLoad()
  }()
  
  /* @CrossNativeInterface */
  private static func createNativeObject(swiftClassName: String) -> Int64{
    return CrossBaseProxy.createNativeObject(swiftClassName)
//    return 0
  }
  
  /* @CrossNativeInterface */
  private static func destroyNativeObject(swiftClassName: String, nativeHandle: Int64) {
    CrossBaseProxy.destroyNativeObject(swiftClassName, nativeHandle)
//    return
  }
}
