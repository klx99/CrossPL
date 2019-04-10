package org.elastos.tools.crosspl.processor.generator

import org.elastos.tools.crosspl.processor.*
import java.io.File
import java.nio.ByteBuffer

class CrossProxyGenerator {
    companion object {
        fun Generate(crossProxyDir: File, classInfo: CrossClassInfo): Boolean {
            val proxyHeaderFile = GetHeaderFile(crossProxyDir, classInfo)
            val proxySourceFile = GetSourceFile(crossProxyDir, classInfo)

            var ret = GenerateHeader(proxyHeaderFile, classInfo)
            if(! ret) {
                return ret
            }

            ret = GenerateSource(proxySourceFile, classInfo)
            if(! ret) {
                return ret
            }

            return true
        }

        fun GetSourceFile(crossProxyDir: File, classInfo: CrossClassInfo): File {
            return File(crossProxyDir, classInfo.cppClassName + "Proxy.cpp")
        }

        fun GetHeaderFile(crossProxyDir: File, classInfo: CrossClassInfo): File {
            return File(crossProxyDir, classInfo.cppClassName + "Proxy.hpp")
        }

        private fun GenerateHeader(proxyFile: File, classInfo: CrossClassInfo): Boolean {
            Log.w("Generate: ${proxyFile.absolutePath}")
            val tmpl = CrossTmplUtils.ReadTmplContent(CrossClassProxyHeaderTmpl)
            var content = ConvertCommonInfo(classInfo, tmpl)

            var nativeFuncList = ""
            var platformFuncList = ""
            classInfo.methodInfo.forEach {
                val functionDeclare = GenerateFunctionDeclare(it, null, it.isNative)
                if(it.isNative) {
                    nativeFuncList += "${CrossTmplUtils.TabSpace}static $functionDeclare;\n"
                } else {
                    platformFuncList += "${CrossTmplUtils.TabSpace}static $functionDeclare;\n"
                }
            }
            content = content
                .replace(TmplKeyPlatformFunction, platformFuncList)
                .replace(TmplKeyNativeFunction, nativeFuncList)

            CrossTmplUtils.WriteContent(proxyFile, content)
            return true
        }

        private fun GenerateSource(proxyFile: File, classInfo: CrossClassInfo): Boolean {
            Log.w("Generate: ${proxyFile.absolutePath}")
            val tmpl = CrossTmplUtils.ReadTmplContent(CrossClassProxySourceTmpl)
            var content = ConvertCommonInfo(classInfo, tmpl)

            var nativeFuncList = ""
            var platformFuncList = ""
            var jniNativeMethodList = ""
            var kotlinStaticNativeMethodList = ""
            classInfo.methodInfo.forEach {
                val functionDeclare = GenerateFunctionDeclare(it, classInfo.cppClassName, it.isNative)
                if(it.isNative) {
                    val emptyFunc = "$functionDeclare\n{\n$TmplKeyFuncBody\n}\n"
                    val funcBody = GenerateFunctionBody(it, classInfo, it.isNative)
                    nativeFuncList += emptyFunc.replace(TmplKeyFuncBody, funcBody)

                    val methodContent = GenerateJniNativeMethod(it)
                    if(classInfo.isKotlinCode && it.isStatic) {
                        kotlinStaticNativeMethodList += "${CrossTmplUtils.TabSpace}${CrossTmplUtils.TabSpace}$methodContent,\n"
                    } else {
                        jniNativeMethodList += "${CrossTmplUtils.TabSpace}${CrossTmplUtils.TabSpace}$methodContent,\n"
                    }
                } else {
                    platformFuncList += "$functionDeclare\n{\n}\n"
                }
            }
            content = content
                .replace(TmplKeyPlatformFunction, platformFuncList)
                .replace(TmplKeyNativeFunction, nativeFuncList)
                .replace(TmplKeyJniNativeMethods, jniNativeMethodList)
                .replace(TmplKeyKotlinStaticNativeMethods, kotlinStaticNativeMethodList)
                .replace(TmplKeyJniJavaClass, classInfo.javaClassName.replace(".", "/"))

            CrossTmplUtils.WriteContent(proxyFile, content)
            return true
        }

        private fun ConvertCommonInfo(classInfo: CrossClassInfo, tmpl: String): String {
            val content = tmpl
                .replace(TmplKeyNamespace, classInfo.cppNamespace)
                .replace(TmplKeyClassName, classInfo.cppClassName)

            return content
        }

        private fun GenerateFunctionDeclare(methodInfo: CrossMethodInfo,
                                            className: String?,
                                            isJniFunc: Boolean): String {
            return if(isJniFunc) {
                GenerateJniFunctionDeclare(methodInfo, className)
            } else {
                GenerateCppFunctionDeclare(methodInfo, className)
            }
        }

        private fun GenerateJniFunctionDeclare(methodInfo: CrossMethodInfo,
                                               cppClassName: String?): String {
            var className = (if(cppClassName != null) "${cppClassName}Proxy::" else "")
            val returnType = methodInfo.returnType.toJniString(false)
            var content = "$returnType $className${methodInfo.methodName}($TmplKeyArguments)"

            var arguments = "JNIEnv* jenv"
            if(methodInfo.isStatic) {
                arguments += ", jclass jtype"
            } else {
                arguments += ", jobject jobj"
            }
            for(idx in methodInfo.paramsType.indices) {
                val type = methodInfo.paramsType[idx].toJniString()
                arguments += ", ${type} jvar$idx"
            }
//            arguments = arguments.removeSuffix(", ")
            content = content.replace(TmplKeyArguments, arguments)

            return content
        }

        private fun GenerateCppFunctionDeclare(methodInfo: CrossMethodInfo,
                                               cppClassName: String?): String {
            var className = (if(cppClassName != null) "${cppClassName}Proxy::" else "")
            val returnType = methodInfo.returnType.toCppString(false)
            var content = "$returnType $className${methodInfo.methodName}($TmplKeyArguments)"

//            var arguments = "uint64_t platformHandle"
            var arguments = ""
            for(idx in methodInfo.paramsType.indices) {
                val type = methodInfo.paramsType[idx].toCppString(true)
                arguments += "${type} var$idx, "
            }
            arguments = arguments.removeSuffix(", ")
            content = content.replace(TmplKeyArguments, arguments)

            return content
        }

        private fun GenerateFunctionBody(methodInfo: CrossMethodInfo,
                                         classInfo: CrossClassInfo,
                                         isNativeFunc: Boolean): String {
            return if(isNativeFunc) {
                GenerateNativeFunctionBody(methodInfo, classInfo)
            } else {
                GeneratePlatformFunctionBody(methodInfo, classInfo)
            }
        }

        private fun GenerateNativeFunctionBody(methodInfo: CrossMethodInfo,
                                               classInfo: CrossClassInfo): String {
            var prefixContent = ""
            for(idx in methodInfo.paramsType.indices) {
                var type = methodInfo.paramsType[idx]
                val isPrimitiveType = type.isPrimitiveType()
                if(isPrimitiveType) {
                    prefixContent += "${CrossTmplUtils.TabSpace}${type.toCppString()} var$idx = jvar$idx;\n"
                } else if(type != CrossVariableType.CROSSBASE) {
                    prefixContent += "${CrossTmplUtils.TabSpace}auto var$idx = CrossPLUtils::SafeCast${type.toString()}(jenv, jvar$idx);\n"
                } else {
                    prefixContent += "${CrossTmplUtils.TabSpace}auto var$idx = CrossPLUtils::SafeCastCrossObject<${classInfo.cppClassName}>(jenv, jvar$idx);\n"
                }
            }
            prefixContent += "\n"

            var funcContent: String
            if(! methodInfo.isStatic) {
                prefixContent += "${CrossTmplUtils.TabSpace}auto obj = CrossPLUtils::SafeCastCrossObject<${classInfo.cppClassName}>(jenv, jobj);\n"
                funcContent = "obj->"
            } else {
                var cppClassName = classInfo.cppClassName
                funcContent = "$cppClassName::"
            }

            var argusContent = ""
            for(idx in methodInfo.paramsType.indices) {
                var type = methodInfo.paramsType[idx]
                val isPrimitiveType = type.isPrimitiveType()
                if(isPrimitiveType) {
                    argusContent += "var$idx, "
                } else if(type == CrossVariableType.STRING) {
                    argusContent += "var$idx.get(), "
                } else {
                    argusContent += "var$idx.get(), "
                }
            }
            argusContent = argusContent.removeSuffix(", ")

            var retContent = ""
            var suffixContent = "\n"
            val retType = methodInfo.returnType
            if(retType != CrossVariableType.VOID) {
                val cppType = retType.toCppString(false)
                retContent = "$cppType ret = "

                val isPrimitiveType = retType.isPrimitiveType()
                if(isPrimitiveType) {
                    suffixContent += "${CrossTmplUtils.TabSpace}${retType.toJniString()} jret = ret;\n"
                    suffixContent += "${CrossTmplUtils.TabSpace}return jret;"
                } else if(retType != CrossVariableType.CROSSBASE) {
                    suffixContent += "${CrossTmplUtils.TabSpace}auto jret = CrossPLUtils::SafeCast${retType.toString()}(jenv, ret);\n"
                    suffixContent += "${CrossTmplUtils.TabSpace}return jret.get();"
                } else {
                    suffixContent += "${CrossTmplUtils.TabSpace}auto jret = CrossPLUtils::SafeCastCrossObject<${classInfo.cppClassName}>(jenv, ret);\n"
                    suffixContent += "${CrossTmplUtils.TabSpace}return jret.get();"
                }
            }
            var content = "$prefixContent"
            content += "${CrossTmplUtils.TabSpace}$retContent$funcContent${methodInfo.methodName}($argusContent);\n"
            content += "$suffixContent"

            return content


            return ""
        }

        private fun GeneratePlatformFunctionBody(methodInfo: CrossMethodInfo,
                                                 classInfo: CrossClassInfo): String {

            return ""
        }

        private fun GenerateJniNativeMethod(methodInfo: CrossMethodInfo): String {
            var funcType = "("
            methodInfo.paramsType.forEach {
                funcType += it.toJavaChar()
            }
            funcType += ")"
            funcType += methodInfo.returnType.toJavaChar()

            val methodContent = "{\"${methodInfo.methodName}\", \"$funcType\", (void*)${methodInfo.methodName}}"

            return methodContent
        }

        private const val CrossClassProxyHeaderTmpl = "/CrossPLClassProxy.hpp.tmpl"
        private const val CrossClassProxySourceTmpl = "/CrossPLClassProxy.cpp.tmpl"

        private const val TmplKeyNamespace = "%Namespace%"
        private const val TmplKeyClassName = "%ClassName%"
        private const val TmplKeyPlatformFunction = "%PlatformFunction%"
        private const val TmplKeyNativeFunction = "%NativeFunction%"
        private const val TmplKeyArguments = "%Arguments%"
        private const val TmplKeyFuncBody = "%FunctionBody%"

        private const val TmplKeyJniJavaClass = "%JniJavaClass%"
        private const val TmplKeyJniNativeMethods = "%JniNativeMethods%"
        private const val TmplKeyKotlinStaticNativeMethods = "%KotlinStaticNativeMethods%"
    }
}
