import Foundation

/* @CrossClass */
open class CrossBase : NSObject {
  public init(className: String, nativeHandle: Int64) {
    CrossBase.initializer
    
    self.className = className
    self.nativeHandle = nativeHandle

    if(self.nativeHandle == 0) {
      self.nativeHandle = CrossBase.CreateNativeObject(swiftClassName: self.className)
    }
  }
  
  deinit {
    CrossBase.DestroyNativeObject(swiftClassName: self.className, nativeHandle: self.nativeHandle)
  }
  
  func bind() {
    bindPlatformHandle(thisObj: self)
  }
  
  func unbind() {
    unbindPlatformHandle(thisObj: self)
  }
  
  private let className: String
  private var nativeHandle: Int64

  private static let initializer: Void = {
    CrossPLFactory.onLoad()
  }()
  
  /* @CrossNativeInterface */
  private static func CreateNativeObject(swiftClassName: String) -> Int64{
    return CrossBaseProxy.createNativeObject(swiftClassName)
//    return 0
  }
  
  /* @CrossNativeInterface */
  private static func DestroyNativeObject(swiftClassName: String, nativeHandle: Int64) {
    CrossBaseProxy.destroyNativeObject(swiftClassName, nativeHandle)
//    return
  }
  
  /* @CrossNativeInterface */
  private func bindPlatformHandle(thisObj: CrossBase) {
    CrossBaseProxy.bindPlatformHandle(nativeHandle, thisObj)
  }
  
  /* @CrossNativeInterface */
  private func unbindPlatformHandle(thisObj: CrossBase) {
    CrossBaseProxy.unbindPlatformHandle(nativeHandle, thisObj)
  }

}
