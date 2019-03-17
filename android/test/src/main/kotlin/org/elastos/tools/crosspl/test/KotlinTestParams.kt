package org.elastos.tools.crosspl.test

import org.elastos.tools.crosspl.CrossBase
import org.elastos.tools.crosspl.annotation.CrossClass
import org.elastos.tools.crosspl.annotation.CrossInterface
import java.nio.ByteBuffer

@CrossClass
open class KotlinTestParams(nativeHandle: Long) : CrossBase(nativeHandle) {
    @CrossInterface
    fun crossMethod(a: Int,
                    b: Long,
                    c: Double,
                    d: String,
                    e: ByteBuffer,
                    f: CrossBase,
                    g: KotlinTestParams
    ): CrossBase? {
        return null
    }
}
