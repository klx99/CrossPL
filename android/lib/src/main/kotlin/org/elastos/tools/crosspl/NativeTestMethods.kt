package org.elastos.tools.crosspl

import org.elastos.tools.crosspl.annotation.CrossClass
import org.elastos.tools.crosspl.annotation.CrossInterface

@CrossClass
open class CrossTestMethods(nativeHandle: Long) : CrossBase(nativeHandle) {
    fun crossInterfaceTest0(): String {
        return "${this.javaClass.name}{}"
    }

    @CrossInterface
    fun crossInterfaceTest1() {
    }

    companion object {
        @CrossInterface
        fun crossInterfaceTest3(a: Long): CrossBase? {
            return null
        }
    }

    @CrossInterface
    private external fun crossInterfaceTest4(className: String): Long
}
