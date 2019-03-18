package org.elastos.tools.crosspl.processor

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.ExecutableType

class CrossMethodInfo {
    companion object {
        fun Parse(methodElement: ExecutableElement, forceStatic: Boolean = false): CrossMethodInfo {
            val methodInfo = CrossMethodInfo()
            methodInfo.methodName = methodElement.simpleName.toString()

            val methodType = methodElement.asType() as ExecutableType;
            methodType.parameterTypes.forEach {
                val type = CrossVariableType.Parse(it)
                if(type == null) {
                    val msg = "Unsupport param type `${it}` for method ${methodElement}"
                    Log.e(msg)
                    throw CrossPLException(msg)
                }

                methodInfo.paramsType.add(type)
            }

            val type = CrossVariableType.Parse(methodElement.returnType)
            if(type == null) {
                val msg = "Unsupport return type `${methodElement.returnType}` for method ${methodElement}"
                Log.e(msg)
                throw CrossPLException(msg)
            }
            methodInfo.returnType = type

            methodInfo.isStatic = forceStatic
            if(methodElement.modifiers.contains(Modifier.STATIC)) {
                methodInfo.isStatic = true
            }
            if(methodElement.modifiers.contains(Modifier.NATIVE)) {
                methodInfo.isNative = true
            }

            return methodInfo
        }
    }

    override fun toString(): String {
        return  "MethodInfo{methodName=${methodName}," +
                " params=${paramsType}, returnType=${returnType}," +
                " static=${isStatic}, native=${isNative}}"
    }

    lateinit var methodName: String
    var paramsType = mutableListOf<CrossVariableType>()
    lateinit var returnType: CrossVariableType
    var isStatic = false
    var isNative = false
}
