package org.elastos.tools.crosspl.test;

import org.elastos.tools.crosspl.CrossBase;
import org.elastos.tools.crosspl.annotation.CrossClass;
import org.elastos.tools.crosspl.annotation.CrossInterface;

@CrossClass
class JavaTestStaticClass extends CrossBase {
    String normalMethod() {
        return null;
    }

    @CrossInterface
    void crossMethod() {
    }

    @CrossInterface
    private native void crossNativeMethod();
}
