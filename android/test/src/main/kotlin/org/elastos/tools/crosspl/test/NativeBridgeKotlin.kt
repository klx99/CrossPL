package org.elastos.tools.crosspl.test

import android.util.Log
import org.elastos.tools.crosspl.annotation.NativeInterface
import org.elastos.tools.crosspl.NativeBase

public final class NativeBridgeKotlin private constructor() {
    class Test1 : NativeBase {
        constructor() : super() {
        }

        @NativeInterface
        fun testRun(input: Int) : Int{
            Log.i("CrossLangGenerator", Test1::class.java.name + ".testRun() called. input = " + input);
            return 0;
        }
    }
}
