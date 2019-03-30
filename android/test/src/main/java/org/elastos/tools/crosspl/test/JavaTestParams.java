package org.elastos.tools.crosspl.test;

import org.elastos.tools.crosspl.CrossBase;
import org.elastos.tools.crosspl.annotation.CrossClass;
import org.elastos.tools.crosspl.annotation.CrossInterface;
import java.nio.ByteBuffer;

//@CrossClass
class JavaTestParams extends CrossBase {
    @CrossInterface
    CrossBase crossMethod(boolean a,
                          int b,
                          long c,
                          double d,
                          String e,
                          ByteBuffer f,
                          CrossBase g) {
        return null;
    }

    @CrossInterface
    native CrossBase crossNativeMethod(boolean a,
                                       int b,
                                       long c,
                                       double d,
                                       String e,
                                       ByteBuffer f,
                                       CrossBase g);
}
