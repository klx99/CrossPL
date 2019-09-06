import Foundation

class CrossMethodInfo {
  static func Parse(sourceContent: String, methodName: String, isNative: Bool) -> CrossMethodInfo {
    let methodInfo = CrossMethodInfo()
//    print("CrossMethodInfo.Parse() ============== 0")
    methodInfo.methodName = methodName
    
    let paramsContent = sourceContent.replace(".*\\((.*)\\).*") { "\($0[1])" }
    let paramsLines = paramsContent.components(separatedBy: ",")
    for line in paramsLines {
      if line.isEmpty == true {
        continue
      }
      let typeLines = line.components(separatedBy: ":")
      let type = CrossVariableType.Parse(sourceContent: typeLines[1])
      
      methodInfo.paramsType.append(type)
    }
    
//    print("CrossMethodInfo.Parse() ============== 1 -\(sourceContent)-")
    var returnContent = sourceContent.replace(".*\\)(.*)") { "\($0[1])" }
//    print("CrossMethodInfo.Parse() ============== 2 -\(returnContent)-")
    if returnContent.isEmpty == true {
      returnContent = "Void"
    } else {
      returnContent = returnContent.replacingOccurrences(of: "->", with: "")
    }
    let returnType = CrossVariableType.Parse(sourceContent: returnContent)
    methodInfo.returnType = returnType
    
    methodInfo.isStatic = sourceContent.contains(" static ")
    methodInfo.isNative = isNative
//    print("CrossMethodInfo.Parse() ============== 4")
    return methodInfo
  }
  
  func toString() -> String {
    var dump = String()
    dump += "MethodInfo{methodName=\(methodName!),"
    dump += " params={"
    for t in paramsType {
      dump += t.toString() + ","
    }
    dump += "},"
    dump += " returnType=\(returnType!.toString()),"
    dump += " static=\(isStatic!), native=\(isNative!)}"
    
    return dump
  }

  
  var methodName: String?
  var paramsType = [CrossVariableType]()
  var returnType: CrossVariableType?
  var isStatic: Bool?
  var isNative: Bool?

}
