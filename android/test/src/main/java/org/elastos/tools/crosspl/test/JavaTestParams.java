package org.elastos.tools.crosspl.test;

import org.elastos.tools.crosspl.CrossBase;
import org.elastos.tools.crosspl.annotation.CrossClass;
import org.elastos.tools.crosspl.annotation.CrossInterface;
import java.nio.ByteBuffer;

@CrossClass
class JavaTestParams extends CrossBase {
    @CrossInterface
    CrossBase crossMethod(int a,
                          long b,
                          double c,
                          String d,
                          ByteBuffer e,
                          CrossBase f,
                          JavaTestParams g) {
        return null;
    }
}
