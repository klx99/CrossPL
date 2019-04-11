package org.elastos.tools.crosspl.test;

import org.elastos.tools.crosspl.CrossBase;
import org.elastos.tools.crosspl.annotation.CrossClass;
import org.elastos.tools.crosspl.annotation.CrossInterface;

public class JavaTestInnerClass {

    @CrossClass
    public static class TestInnerClass extends CrossBase {
        public String normalMethod() {
            return null;
        }

        @CrossInterface
        public void crossMethod() {
        }

        @CrossInterface
        public static void crossStaticMethod() {
        }

        @CrossInterface
        public native void crossNativeMethod();

        @CrossInterface
        public static native void crossNativeStaticMethod();

    }
}
