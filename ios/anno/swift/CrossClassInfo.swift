import Foundation

class CrossClassInfo {
  static func Parse(sourceFile: String, className: String,
                    sourceLines: [String], classIndex: Int,
                    bundleId: String) -> CrossClassInfo {
    let classInfo = CrossClassInfo()
    classInfo.cppInfo.className = className
    classInfo.swiftInfo.className = className
    classInfo.swiftInfo.classPath = bundleId + "." + className
    
    var nativeMethodList = [String]()
    var platformMethodList = [String]()
    var layer = 0;
    for idx in classIndex..<sourceLines.count {
      let line = sourceLines[idx]
      if line.contains("{") {
        layer += 1
      } else if line.contains("}") {
        layer -= 1
        if(layer == 0) {
          break;
        }
      }
      if layer != 1 { // only recode top layer function
        continue
      }
      
      if line.contains(Annotation.CrossNativeInterface) {
        nativeMethodList.append(sourceLines[idx + 1])
      } else if line.contains(Annotation.CrossPlatformInterface) {
        platformMethodList.append(sourceLines[idx + 1])
      }
    }
//    print("nativeMethodList=\(nativeMethodList)")
//    print("platformMethodList=\(platformMethodList)")
    
    let ast = printAst(filePath: sourceFile)
    let classLines = ast.components(separatedBy: .newlines)
    var classLayer = -1
    layer = 0
    for idx in classLines.indices {
      let line = classLines[idx];
      if line.contains("{") {
        layer += 1
      } else if line.contains("}") {
        layer -= 1
      }
      if line.contains(" \(className) ") {
        classLayer = layer
      }
    
      if layer == classLayer
      && line.contains(" func ") {
        let methodName = line.replace(".*func (\\w*)\\(.*") { "\($0[1])" }
        var isCrossInterface = false
        var isNative: Bool?
        if (nativeMethodList.filter() { $0.contains("func \(methodName)(") }.count != 0) {
          isCrossInterface = true
          isNative = true
        } else if (platformMethodList.filter() { $0.contains("func \(methodName)(") }.count != 0) {
          isCrossInterface = true
          isNative = false
        }
       
        if isCrossInterface == true {
          let methodInfo = CrossMethodInfo.Parse(sourceContent: line, methodName: methodName, isNative: isNative!)
          print("~~~~~ \(methodInfo.toString())")
          
          classInfo.methodInfoList.append(methodInfo)
        }
      }
    }
    
    return classInfo
  }
  
  func toString() -> String {
    var dump = String()
    dump += "ClassInfo{cppInfo=\(cppInfo.toString()),"
    dump += " swiftInfo=\(swiftInfo.toString()),"
    dump += " methodInfoList={"
    for it in methodInfoList {
      dump += it.toString() + ","
    }
    dump += "}}"
    
    return dump
  }

  
  private class CppInfo {
    var className: String?
    
    func toString() -> String {
      return  "CppInfo{className=\(className ?? "nil")"
    }
  }
  
  private class SwiftInfo {
    var className: String?
    var classPath: String?
    
    func toString() -> String {
      return "SwiftInfo{className=\(className ?? "nil"), classPath=\(classPath ?? "nil")"
    }
  }

  private static func printAst(filePath: String) -> String {
    let task = Process()
    task.launchPath = "/bin/bash"
    task.arguments = ["-c", "swiftc -print-ast \(filePath) 2>/dev/null"]
    
    let pipe = Pipe()
    task.standardOutput = pipe
    task.launch()
    
    let data = pipe.fileHandleForReading.readDataToEndOfFile()
    let output: String = NSString(data: data, encoding: String.Encoding.utf8.rawValue)! as String
    
    return output
  }
  
  private var cppInfo = CppInfo()
  private var swiftInfo = SwiftInfo()
  private var methodInfoList = [CrossMethodInfo]()
}
