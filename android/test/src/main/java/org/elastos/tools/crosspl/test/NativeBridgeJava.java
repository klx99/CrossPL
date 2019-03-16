package org.elastos.tools.crosspl.test;

import android.util.Log;

import org.elastos.tools.crosspl.Utils;
import org.elastos.tools.crosspl.annotation.NativeInterface;
import org.elastos.tools.crosspl.NativeBase;

public final class NativeBridgeJava {
    private NativeBridgeJava() {}

    public static class Test1 extends NativeBase {
        public Test1() {
            super();
        }

        protected Test1(Long nativeHandle) {
            super(nativeHandle);
        }

        @NativeInterface
        int testRun(int input) {
            Log.i(Utils.TAG, Test1.class.getName() + ".testRun() called. input = " + input);
            return input + 1;
        }
    }
}
