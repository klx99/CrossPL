package org.elastos.tools.crosspl.test

import org.elastos.tools.crosspl.CrossBase
import org.elastos.tools.crosspl.annotation.CrossClass
import org.elastos.tools.crosspl.annotation.CrossInterface

@CrossClass
object KotlinTestStaticClass : CrossBase(0) {
    fun normalMethod(): String {
        return "${this.javaClass.name}{}"
    }

    @CrossInterface
    fun crossMethod() {
    }

    @CrossInterface
    private external fun crossNativeMethod()
}
