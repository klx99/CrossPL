package org.elastos.tools.crosspl.test

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        JavaTestMethods.crossNativeStaticMethod()
        JavaTestMethods().crossNativeMethod()
    }

    private companion object {
        init {
            System.loadLibrary("CrossPLTest")
        }
    }
}
