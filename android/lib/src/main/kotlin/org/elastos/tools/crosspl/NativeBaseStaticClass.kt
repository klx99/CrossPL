package org.elastos.tools.crosspl

import org.elastos.tools.crosspl.annotation.CrossClass
import org.elastos.tools.crosspl.annotation.CrossInterface
import java.nio.ByteBuffer

@CrossClass
object CrossTestStaticClass : CrossBase(0) {
    fun crossInterfaceTest0(): String {
        return "${this.javaClass.name}{}"
    }

    @CrossInterface
    fun crossInterfaceTest1() {
    }

    @CrossInterface
    private external fun crossInterfaceTest2(className: String): Long
}
