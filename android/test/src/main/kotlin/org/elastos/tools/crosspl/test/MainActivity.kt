package org.elastos.tools.crosspl.test

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var testJava1 = NativeBridgeJava.Test1()
//
//        var testJava2 = NativeBridgeJava.Test1(111)
//
//        var testKotlin1 = NativeBridgeKotlin.Test1()
//
//        var testKotlin2 = NativeBridgeKotlin.Test1(111)
    }

    private companion object {
        init {
            System.loadLibrary("CrossPLTest")
        }
    }
}
