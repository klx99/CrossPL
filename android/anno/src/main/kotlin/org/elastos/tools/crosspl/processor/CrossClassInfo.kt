package org.elastos.tools.crosspl.processor

import org.elastos.tools.crosspl.annotation.CrossInterface
import java.nio.ByteBuffer
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

class CrossClassInfo {
    companion object {
        fun Parse(classElement: Element): CrossClassInfo? {
            if(! isExtendsCrossBaseClass(classElement)) {
                return null
            }

            val classInfo = CrossClassInfo()
            classInfo.cppNamespace = "crosspl"
            classInfo.cppClassName = classElement.simpleName.toString()
            classInfo.javaClassName = classElement.toString()

            var isStaticClass = false
            classElement.enclosedElements.forEach { // for kotlin static class
                if(it.kind == ElementKind.FIELD
                && it.toString() == "INSTANCE") {
                    isStaticClass = true
                }
            }

            var companionElement: Element? = null
            classElement.enclosedElements.forEach {
                if(it.kind == ElementKind.METHOD
                && it.getAnnotation(CrossInterface::class.java) != null) {
                    val methodInfo = CrossMethodInfo.Parse(it as ExecutableElement, isStaticClass)
                    classInfo.methodInfo.add(methodInfo);
                } else if (it.kind == ElementKind.CLASS
                && it.toString() == classElement.toString() + ".Companion") { // for kotlin companion object methods
                    companionElement = it
                }
            }
            if(companionElement != null) {
                classInfo.isKotlinCode = true
                companionElement!!.enclosedElements.forEach {
                    if(it.kind == ElementKind.METHOD
                    && it.getAnnotation(CrossInterface::class.java) != null) {
                        val methodInfo = CrossMethodInfo.Parse(it as ExecutableElement, true)
                        classInfo.methodInfo.add(methodInfo);
                    }
                }
            }

            Log.d("ClassInfo: ${classInfo}")
            return classInfo
        }

        private fun isExtendsCrossBaseClass(element: Element): Boolean {
//            val typeUtils = procEnv.typeUtils
//            val crossBaseType = procEnv.elementUtils.getTypeElement(CrossBaseClass).asType()
            val typeElement = findEnclosingTypeElement(element);
            if(typeElement.superclass.toString() == CrossBaseClass) {
                return true
            }

            Log.e("${element} MUSTBE extends from ${CrossBaseClass}")
            return false
        }

        private fun findEnclosingTypeElement(element: Element): TypeElement {
            var e = element
            while(e !is TypeElement) {
                e = e.enclosingElement;
            }

            return e
        }

        private const val CrossBaseClass = "org.elastos.tools.crosspl.CrossBase"
    }

    override fun toString(): String {
        return  "ClassInfo{cppNamespace=${cppNamespace}," +
                " cppClassName=${cppClassName}," +
                " javaClassName=${javaClassName}," +
                " methodInfo=${methodInfo}}" +
                " isKotlinCode=${isKotlinCode}}"
    }

    lateinit var cppNamespace: String
    lateinit var cppClassName: String
    lateinit var javaClassName: String
    var isKotlinCode: Boolean = false
    var methodInfo = mutableListOf<CrossMethodInfo>()
}
