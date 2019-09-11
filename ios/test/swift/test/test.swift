import Foundation

/* @CrossClass */
open class ExternalTest
{
  /* @CrossNativeInterface */
  public static func externalTestFuncNoArgs() {
    return;
  }
  
  /* @CrossNativeInterface */
  func externalTestFunc(a: Bool,
                        b: Int32,
                        c: Int64,
                        d: Double,
                        e: String,
                        f: Data,
                        
                        h: inout String,
                        i: inout Data,
    
                        j: CrossBase) -> Void {
    return;
  }
  
  func internalTestFunc(aaa: Int) -> Int {
    return 0;
  }
  
  /* @CrossClass */
  open class InternalTest
  {
    /* @CrossPlatformInterface */
    func internalTestFunc(aaa: Int32) -> Int32 {
      return 0;
    }
    
    func externalTestFunc(aaa: Int) -> Int {
      return 0;
    }
  }

}
