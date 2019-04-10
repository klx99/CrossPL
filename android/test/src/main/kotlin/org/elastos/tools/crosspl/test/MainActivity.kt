package org.elastos.tools.crosspl.test

import android.app.Activity
import android.os.Bundle
import android.util.Log
import org.elastos.tools.crosspl.Utils
import java.nio.ByteBuffer

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        JavaTestMethods.crossNativeStaticMethod()
        JavaTestMethods().crossNativeMethod()

        Log.i(Utils.TAG, "======================")
        JavaTestParams().crossNativeMethod(true, 0, 0, 0.0, null, null, null, null);
        Log.i(Utils.TAG, "======================")
        val f = byteArrayOf(0, 128.toByte(), 255.toByte())
        val g = StringBuffer("set from platform")
        val h = ByteBuffer.wrap(f.reversedArray())
        val ret = JavaTestParams().crossNativeMethod(true, Int.MAX_VALUE, Long.MAX_VALUE, Double.MAX_VALUE,
            "set from platform",
            f, g, h)
        Log.i(Utils.TAG, "return value: $ret")
        Log.i(Utils.TAG, "out value g: $g")
        Log.i(Utils.TAG, "out value h: $h")
        Log.i(Utils.TAG, "======================")
    }

    private companion object {
        init {
            System.loadLibrary("CrossPLTest")
        }
    }
}
