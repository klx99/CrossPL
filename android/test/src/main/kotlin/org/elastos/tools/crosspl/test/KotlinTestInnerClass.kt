package org.elastos.tools.crosspl.test

import org.elastos.tools.crosspl.CrossBase
import org.elastos.tools.crosspl.annotation.CrossClass
import org.elastos.tools.crosspl.annotation.CrossInterface

open class KotlinTestInnerClass {

    @CrossClass
    open class TestInnerClass(nativeHandle: Long) : CrossBase(nativeHandle) {
        fun normalMethod(): String {
            return "${this.javaClass.name}{}"
        }

        @CrossInterface
        fun crossMethod() {
        }

        companion object {
            @CrossInterface
            fun crossStaticMethod() {
            }

            @CrossInterface
            private external fun crossNativeStaticMethod()
        }

        @CrossInterface
        private external fun crossNativeMethod()
    }
}
