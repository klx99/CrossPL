import Darwin
import Foundation

func main()
{
  print("CrossPL: anno start...")
  
  if(CommandLine.argc < 3) {
    print("Bad Arguments");
    exit(1)
  }
  
  let swiftSrcDir = NSURL.fileURL(withPath: CommandLine.arguments[1])
  let proxyDir = NSURL.fileURL(withPath: CommandLine.arguments[2])
  let bundleId = CommandLine.argc > 3 ? CommandLine.arguments[3] : "UnknownBundleIdentifier"
  
  if(FileManager.default.fileExists(atPath: swiftSrcDir.path) == false) {
    print("[\(swiftSrcDir.path)] not exists.")
    exit(1)
  }

  let enumerator = FileManager.default.enumerator(atPath: swiftSrcDir.path)
  let filePaths = enumerator?.allObjects as! [String]
  var swiftSrcList = filePaths.filter({$0.contains(".swift")})
  for idx in swiftSrcList.indices {
    swiftSrcList[idx] = swiftSrcDir.appendingPathComponent(swiftSrcList[idx]).path
  }
  
  print("Found swift source file:")
  for src in swiftSrcList {
    print("  \(src)")
  }
  
  print("Make proxy target dir: \(proxyDir.path)")
  do {
    try FileManager.default.createDirectory(at: proxyDir, withIntermediateDirectories: true)
  } catch {
    print("Failed to create proxy target dir: \(proxyDir.path)")
    exit(1)
  }
  
  let ret = CrossClassAnnoProcessor.process(swiftSrcList: swiftSrcList, proxyDir: proxyDir, bundleId: bundleId)
  if(ret == false) {
    print("Failed to process crosspl anno.")
    exit(1)
  }
}

main()
