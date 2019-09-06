import Foundation

class CrossVariableType {
  enum `Type` {
    case BOOLEAN
    case INT32
    case INT64
    case DOUBLE
    case VOID
    
    case STRING
    case BYTEARRAY
    case FUNCTION
    
    case STRINGBUFFER
    case BYTEBUFFER
    
    case CROSSBASE
  }
  
  static func Parse(sourceContent: String) -> CrossVariableType {
    let supportedTypeDeclared = ["Bool": Type.BOOLEAN,
                                 "Int32": Type.INT32,
                                 "Int64": Type.INT64,
                                 "Double": Type.DOUBLE,
                                 "Void": Type.VOID,
                                 
                                 "String": Type.STRING,
                                 "Data": Type.BYTEARRAY,
                                 "Function": Type.FUNCTION,
                                 
                                 "inout String": Type.STRINGBUFFER,
                                 "inout Data": Type.BYTEBUFFER,
                                 
                                 "CrossBase": Type.CROSSBASE]
    
    let trimContent = sourceContent.trimmingCharacters(in: .whitespacesAndNewlines)
   
    let varType = CrossVariableType()
    varType.type = supportedTypeDeclared[trimContent]
    if varType.type == nil {
      print("CrossVariableType.Parse() Unsupported variable type: \(trimContent)")
      exit(1)
    }
    
//    print("=================CrossVariableType.Parse() trimContent=\(varType.toString())")

    return varType
  }
  
  func toString() -> String {
    return "\(type!)"
  }
  
  var type: Type?
}
