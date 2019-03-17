package org.elastos.tools.crosspl.processor

import java.lang.RuntimeException
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class CrossMethodInfo {
    companion object {
        fun Parse(methodElement: ExecutableElement, forceStatic: Boolean = false): CrossMethodInfo {
            val methodInfo = CrossMethodInfo()
            methodInfo.methodName = methodElement.simpleName.toString()

            val methodType = methodElement.asType() as ExecutableType;
            methodType.parameterTypes.forEach {
                val type = transferType(it)
                if(type == null) {
                    val msg = "Unsupport param type `${it}` for method ${methodElement}"
                    Log.e(msg)
                    throw RuntimeException(msg)
                }

                methodInfo.paramsType.add(type)
            }

            val type = transferType(methodElement.returnType)
            if(type == null) {
                val msg = "Unsupport return type `${methodElement.returnType}` for method ${methodElement}"
                Log.e(msg)
                throw RuntimeException(msg)
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

        private fun transferType(type: TypeMirror): String? {
            if(supportedTypeKind.contains(type.kind)) {
                return type.toString()
            } else if(type is DeclaredType) {
                val typeElement = type.asElement() as TypeElement
                if(supportedTypeDeclared.contains(type.toString())
                || supportedTypeDeclared.contains(typeElement.superclass.toString())) {
                    return type.toString()
                }
            }

            return null
        }

        private val supportedTypeKind = setOf(
            TypeKind.BOOLEAN,
            TypeKind.INT,
            TypeKind.LONG,
            TypeKind.DOUBLE,
            TypeKind.VOID
        )
        private val supportedTypeDeclared = setOf(
            "java.lang.String",
            "java.nio.ByteBuffer",
            "org.elastos.tools.crosspl.CrossBase"
        )
    }

    override fun toString(): String {
        return  "MethodInfo{methodName=${methodName}," +
                " params=${paramsType}, returnType=${returnType}," +
                " static=${isStatic}, native=${isNative}}"
    }

    lateinit var methodName: String
    var paramsType = mutableListOf<String>()
    lateinit var returnType: String
    var isStatic = false
    var isNative = false
}
