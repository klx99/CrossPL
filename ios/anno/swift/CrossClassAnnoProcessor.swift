import Foundation

class CrossClassAnnoProcessor {
  static func process(swiftSrcList: [String], proxyDir: URL, bundleId: String) -> Bool {
    var classInfoList = [CrossClassInfo]()
    
    do {
      for swiftSrc in swiftSrcList {
        let swiftContent = try String(contentsOfFile: swiftSrc, encoding: .utf8)
        let contentLines = swiftContent.components(separatedBy: .newlines)
        for idx in contentLines.indices {
          let line = contentLines[idx]
          if(idx >= contentLines.count) {
            continue;
          }
          if(line.contains(Annotation.CrossClass) == false) {
            continue;
          }
          let classIndex = idx + 1
          let classDefine = contentLines[classIndex]
          let className = classDefine.replace(".*class (\\w*).*") { "\($0[1])" }
          print("Found \(Annotation.CrossClass): \(className)")
          
          let classInfo = CrossClassInfo.Parse(sourceFile: swiftSrc, className: className,
                                               sourceLines: contentLines, classIndex: classIndex,
                                               bundleId: bundleId)
          classInfoList.append(classInfo)
        }
      }
    } catch {
      print("CrossClassAnnoProcessor Error info: \(error)")
    }
    
    var headerFileList = [URL]()
    var sourceFileList = [URL]()
    for it in classInfoList {
      let ret = CrossProxyGenerator.Generate(crossProxyDir: proxyDir, classInfo: it)
      if ret == false {
        print("Failed to generate class: \(it.toString())")
        return false
      }
    }
    
    return true
  }
}
