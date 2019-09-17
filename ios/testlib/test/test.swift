import Foundation

import CrossPL

/* @CrossClass */
@objc open class ExternalTest: CrossBase
{
  /* @CrossNativeInterface */
  public static func externalTestFuncNoArgs() {
    crosspl_Proxy_ExternalTest_externalTestFuncNoArgs();
  }
  
  /* @CrossNativeInterface */
  public func externalTestFunc(a: Bool,
                        b: Int32,
                        c: Int64,
                        d: Double,
                        e: String,
                        f: Data,
                        
                        h: inout String,
                        i: inout Data) -> Void {
    return;
  }
  
  func internalTestFunc(aaa: Int) -> Int {
    return 0;
  }
  
  /* @CrossClass */
  @objc open class InternalTest: CrossBase
  {
    /* @CrossPlatformInterface */
    @objc public func internalTestFunc(_ aaa: Int32) -> Int32 {
      return 0;
    }
    
    func externalTestFunc(aaa: Int) -> Int {
      return 0;
    }
  }

}