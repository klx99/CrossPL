package org.elastos.tools.crosspl

open class CrossTestBase(nativeHandle: Long) : CrossBase(nativeHandle) {
    override fun toString(): String {
        return "${this.javaClass.name}"
    }
}
