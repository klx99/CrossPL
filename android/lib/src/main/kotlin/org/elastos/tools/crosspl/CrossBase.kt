package org.elastos.tools.crosspl

import android.util.Log
import org.elastos.tools.crosspl.annotation.CrossClass
import org.elastos.tools.crosspl.annotation.CrossInterface

//@CrossClass
open class CrossBase
    protected constructor(private var nativeHandle: Long = 0) {

    init {
        if(nativeHandle.equals(0)) {
            nativeHandle = createNativeObject(this.javaClass.name)
        }
        Log.i(Utils.TAG, "construct " + toString())
        if(nativeHandle.equals(0)) {
            throw Utils.CrossPLException("Failed to create native object.")
        }
    }

    protected fun finalize() {
        destroyNativeObject(nativeHandle)
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
            System.load("crosspl")
        }
    }
    private external fun createNativeObject(className: String): Long
    private external fun destroyNativeObject(nativeHandle: Long)

}
