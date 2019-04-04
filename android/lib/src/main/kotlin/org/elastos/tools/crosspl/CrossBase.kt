package org.elastos.tools.crosspl

import android.util.Log
import org.elastos.tools.crosspl.annotation.CrossClass
import org.elastos.tools.crosspl.annotation.CrossInterface

@CrossClass
open class CrossBase
    protected constructor(private var nativeHandle: Long = 0) {

    init {
        if(nativeHandle.equals(0)) {
            nativeHandle = CreateNativeObject(this.javaClass.name)
        }
        Log.i(Utils.TAG, "construct " + toString())
        if(nativeHandle.equals(0)) {
            throw Utils.CrossPLException("Failed to create native object.")
        }
    }

    protected fun finalize() {
        DestroyNativeObject(this.javaClass.name, nativeHandle)
        Log.i(Utils.TAG, "deconstruct " + toString())
    }

    @CrossInterface
    override fun toString(): String {
        return "${this.javaClass.name}{nativeHandle=${nativeHandle}}"
    }
//
//    private var nativeHandle: Long = 0
    private companion object {
        init {
            System.loadLibrary("CrossPLTest")
        }
    }
    private external fun CreateNativeObject(className: String): Long
    private external fun DestroyNativeObject(className: String, nativeHandle: Long)

}
