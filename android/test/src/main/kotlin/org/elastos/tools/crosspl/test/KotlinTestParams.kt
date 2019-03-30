package org.elastos.tools.crosspl.test

import org.elastos.tools.crosspl.CrossBase
import org.elastos.tools.crosspl.annotation.CrossClass
import org.elastos.tools.crosspl.annotation.CrossInterface
import java.nio.ByteBuffer

//@CrossClass
open class KotlinTestParams(nativeHandle: Long) : CrossBase(nativeHandle) {
    @CrossInterface
    fun crossMethod(a: Boolean,
                    b: Int,
                    c: Long,
                    d: Double,
                    e: String,
                    f: ByteBuffer,
                    g: CrossBase
    ): CrossBase? {
        return null
    }

    @CrossInterface
    external fun crossNativeMethod(a: Boolean,
                          b: Int,
                          c: Long,
                          d: Double,
                          e: String,
                          f: ByteBuffer,
                          g: CrossBase
    ): CrossBase?
}
