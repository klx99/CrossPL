package org.elastos.tools.crosspl.test;

import android.util.Log;

import org.elastos.tools.crosspl.annotation.NativeInterface;
import org.elastos.tools.crosspl.NativeBase;

public final class NativeBridgeJava {
    private NativeBridgeJava() {}

    public class Test1 extends NativeBase {
        public Test1() {
        }

        @NativeInterface
        int testRun(int input) {
            Log.i("CrossLangGenerator", Test1.class.getName() + ".testRun() called. input = " + input);
            return input + 1;
        }
    }
}
