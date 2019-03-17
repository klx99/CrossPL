package org.elastos.tools.crosspl.processor

import org.elastos.tools.crosspl.annotation.CrossInterface
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
            classInfo.className = classElement.toString()
            classInfo.classSimpleName = classElement.simpleName.toString()

            classElement.enclosedElements.forEach { // for kotlin static class
                if(it.kind == ElementKind.FIELD
                    && it.toString() == "INSTANCE") {
                    classInfo.isStaticClass = true
                }
            }

            var companionElement: Element? = null
            classElement.enclosedElements.forEach {
                if(it.kind == ElementKind.METHOD
                && it.getAnnotation(CrossInterface::class.java) != null) {
                    val methodInfo = CrossMethodInfo.Parse(it as ExecutableElement, classInfo.isStaticClass)
                    classInfo.methodInfo.add(methodInfo);
                } else if (it.kind == ElementKind.CLASS
                && it.toString() == classElement.toString() + ".Companion") { // for kotlin companion object methods
                    companionElement = it
                }
            }
            if(companionElement != null) {
                companionElement!!.enclosedElements.forEach {
                    if(it.kind == ElementKind.METHOD
                    && it.getAnnotation(CrossInterface::class.java) != null) {
                        val methodInfo = CrossMethodInfo.Parse(it as ExecutableElement, true)
                        classInfo.methodInfo.add(methodInfo);
                    }
                }
            }

            Log.w("ClassInfo: ${classInfo}")
            return classInfo
        }

        private fun isExtendsCrossBaseClass(element: Element): Boolean {
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
        return  "ClassInfo{className=${className}," +
                " classSimpleName=${classSimpleName}," +
                " methodInfo=${methodInfo}," +
                " isStaticClass=${isStaticClass}}"
    }

    private lateinit var className: String
    private lateinit var classSimpleName: String
    private var methodInfo = mutableListOf<CrossMethodInfo>()
    private var isStaticClass = false
}
