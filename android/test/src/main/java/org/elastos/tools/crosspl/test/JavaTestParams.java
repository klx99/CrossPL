package org.elastos.tools.crosspl.test;

import org.elastos.tools.crosspl.CrossBase;
import org.elastos.tools.crosspl.annotation.CrossClass;
import org.elastos.tools.crosspl.annotation.CrossInterface;

import java.io.ByteArrayOutputStream;
import java.util.List;

@CrossClass
class JavaTestParams extends CrossBase {
    @CrossInterface
    int crossMethod(boolean a,
                    int b,
                    long c,
                    double d,
                    String e,
                    byte f[],
                    StringBuffer g,
                    ByteArrayOutputStream h) {
        return -1;
    }

    CrossBase crossMethodEx(boolean a,
                            int b,
                            long c,
                            double d,
                            String e,
                            byte f[],
                            StringBuffer g,
                            ByteArrayOutputStream h,
                            CrossBase i) {
        return null;
    }

    @CrossInterface
    native int crossNativeMethod(boolean a,
                                 int b,
                                 long c,
                                 double d,
                                 String e,
                                 byte[] f,
                                 StringBuffer g,
                                 ByteArrayOutputStream h);

    @CrossInterface
    native boolean crossNativeMethod0(boolean a);
    @CrossInterface
    native int crossNativeMethod1(int a);
    @CrossInterface
    native long crossNativeMethod2(long a);
    @CrossInterface
    native double crossNativeMethod3(double a);
    @CrossInterface
    native String crossNativeMethod4(String a);
    @CrossInterface
    native byte[] crossNativeMethod5(byte[] a);
    @CrossInterface
    native StringBuffer crossNativeMethod6(StringBuffer a);
    @CrossInterface
    native ByteArrayOutputStream crossNativeMethod7(ByteArrayOutputStream a);

    native CrossBase crossNativeMethodEx(boolean a,
                                         int b,
                                         long c,
                                         double d,
                                         String e,
                                         byte f[],
                                         StringBuffer g,
                                         ByteArrayOutputStream h,
                                         CrossBase i);
}
