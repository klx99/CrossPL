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
  
  func makeProxyDeclare() -> String {
    if self.isNative! {
      return makeNativeFunctionDeclare(cppClassName: nil)
    } else {
      return makePlatformFunctionDeclare(cppClassName: nil)
    }
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
  
  private func makeNativeFunctionDeclare(cppClassName: String?) -> String {
    let className = ((cppClassName != nil) ? "\(cppClassName!)::" : "")
    let returnType = self.returnType!.toObjcString(isConst: false)
    var content = "(\(returnType)) \(className)\(self.methodName!)\(CrossMethodInfo.TmplKeyArguments)"
    
    var arguments = ""
    if isStatic == false {
      arguments += ": (int64_t)nativeHandle"
    }

    for idx in 0..<paramsType.count {
      let type = paramsType[idx].toObjcString()
      arguments += ": (\(type))ocvar\(idx)"
    }
    content = content.replacingOccurrences(of: CrossMethodInfo.TmplKeyArguments, with: arguments)
    
    return content
  }
  
  private func makePlatformFunctionDeclare(cppClassName: String?) -> String {
    let className = ((cppClassName != nil) ? "\(cppClassName!)::" : "")
    var retTypeStr = self.returnType!.toCppString(isConst: false)//.removeSuffix("*")
    if returnType!.type! == .CROSSBASE {
      retTypeStr = "\(retTypeStr)*"
    } else if self.returnType!.isPrimitiveType() == false {
      retTypeStr = "std::shared_ptr<\(retTypeStr)>"
    }
    var content = "\(retTypeStr) \(className)\(self.methodName!)\(CrossMethodInfo.TmplKeyArguments)"

    var arguments = ""
    if isStatic == false {
      arguments += ": (int64_t)platformHandle"
    }
    for idx in 0..<paramsType.count {
      let type = paramsType[idx].toCppString(isConst: true)
      arguments += ": (\(type))var\(idx)"
    }
    content = content.replacingOccurrences(of: CrossMethodInfo.TmplKeyArguments, with: arguments)

    return content
  }


  private static let TmplKeyArguments = "%Arguments%"
  private static let TmplKeyFuncBody = "%FunctionBody%"
  
}
