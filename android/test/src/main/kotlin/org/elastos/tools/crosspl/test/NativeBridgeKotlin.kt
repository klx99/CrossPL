package org.elastos.tools.crosspl.test

import android.util.Log
import org.elastos.tools.crosspl.annotation.CrossInterface
import org.elastos.tools.crosspl.CrossBase
import org.elastos.tools.crosspl.Utils
import org.elastos.tools.crosspl.annotation.CrossClass

@CrossClass
open class NativeBridgeKotlin private constructor() {
    open class Test1 : CrossBase {
        constructor() : super()
        constructor(nativeHandle: Long) : super(nativeHandle)

        @CrossInterface
        fun testRun(input: Int) : Int{
            Log.i(Utils.TAG, Test1::class.java.name + ".testRun() called. input = " + input);
            return 0;
        }
    }
}
