package org.elastos.tools.crosspl.test;

import org.elastos.tools.crosspl.annotation.CrossClass;
import org.elastos.tools.crosspl.annotation.CrossInterface;
import org.elastos.tools.crosspl.CrossBase;

@CrossClass
public final class JavaTestMethods extends CrossBase {
    private JavaTestMethods() {}

    String normalMethod() {
        return null;
    }

    @CrossInterface
    void crossMethod() {
    }

    @CrossInterface
    static void crossStaticMethod() {
    }

    @CrossInterface
    private native void crossNativeMethod();

    @CrossInterface
    private static native void crossNativeStaticMethod();

}
