package org.elastos.tools.crosspl.test

import android.app.Activity
import android.os.Bundle
import android.util.Log
import org.elastos.tools.crosspl.Utils
import java.io.ByteArrayOutputStream

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
        JavaTestParams().crossNativeMethod(true, 0, 0, 0.0, null, null, null, null, null);
        Log.i(Utils.TAG, "======================")
        val f = byteArrayOf(0, 128.toByte(), 255.toByte())
        val g = Runnable { Log.d(Utils.TAG, "Runnable callback") }
        val h = StringBuffer("set from platform")
        val i = ByteArrayOutputStream()
        i.write(f.reversedArray())
        val ret = JavaTestParams().crossNativeMethod(true, Int.MAX_VALUE, Long.MAX_VALUE, Double.MAX_VALUE,
            "set from platform",
            f, g, h, i)
        Log.i(Utils.TAG, "return value: $ret")
        Log.i(Utils.TAG, "out value g: $g")

        val sb = StringBuffer()

        for(b in i.toByteArray()) {
            sb.append(String.format("%02x ", b))
        }
        Log.i(Utils.TAG, "out value h: $sb")
        Log.i(Utils.TAG, "======================")
    }

    private companion object {
        init {
            System.loadLibrary("CrossPLTest")
        }
    }
}
