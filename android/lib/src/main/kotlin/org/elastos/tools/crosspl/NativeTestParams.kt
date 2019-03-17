package org.elastos.tools.crosspl

import org.elastos.tools.crosspl.annotation.CrossClass
import org.elastos.tools.crosspl.annotation.CrossInterface
import java.nio.ByteBuffer

@CrossClass
open class CrossTestParams(nativeHandle: Long) : CrossBase(nativeHandle) {
    fun crossInterfaceTest0(): String {
        return "${this.javaClass.name}{}"
    }

    @CrossInterface
    fun crossInterfaceTest1() {
    }

    @CrossInterface
    fun crossInterfaceTest2(a: Int,
                            b: Long,
                            c: Double,
                            d: String,
                            e: ByteBuffer,
                            f: CrossBase,
                            g: CrossTestParams): CrossBase? {
        return null
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
