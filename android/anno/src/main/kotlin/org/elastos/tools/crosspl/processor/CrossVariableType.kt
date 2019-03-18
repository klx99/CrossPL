package org.elastos.tools.crosspl.processor

import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

enum class CrossVariableType {
    BOOLEAN,
    INTEGER,
    LONG,
    DOUBLE,
    VOID,
    STRING,
    BYTEBUFFER,
    CROSSBASE;

    companion object {
        fun Parse(type: TypeMirror): CrossVariableType? {
            if(supportedTypeKind.contains(type.kind)) {
                return supportedTypeKind.getValue(type.kind)
            } else if(type is DeclaredType) {
                val typeElement = type.asElement() as TypeElement
                val typeStr = typeElement.toString()
                return supportedTypeDeclared[typeStr]
            }

            return null
        }

        private val supportedTypeKind = mapOf(
            TypeKind.BOOLEAN           to BOOLEAN,
            TypeKind.INT               to INTEGER,
            TypeKind.LONG              to LONG,
            TypeKind.DOUBLE            to DOUBLE,
            TypeKind.VOID              to VOID
        )
        private val supportedTypeDeclared = mapOf(
            "java.lang.String"                    to STRING,
            "java.nio.ByteBuffer"                 to BYTEBUFFER,
            "org.elastos.tools.crosspl.CrossBase" to CROSSBASE
        )
    }

    fun toCppString(isConst: Boolean = false): String {
        val primitiveTypeMap = mapOf(
            BOOLEAN    to "bool",
            INTEGER    to "int",
            LONG       to "int64_t",
            DOUBLE     to "double",
            VOID       to "void"
        )
        val classTypeMap = mapOf(
            STRING     to "std::string",
            BYTEBUFFER to "std::vector<int8_t>",
            CROSSBASE  to "crosspl::CrossBase"
        )

        var cppType = toString(primitiveTypeMap, classTypeMap, isConst)

        return cppType
    }

    fun toJniString(isConst: Boolean = false): String {
        val primitiveTypeMap = mapOf(
            BOOLEAN    to "jboolean",
            INTEGER    to "jint",
            LONG       to "jlong",
            DOUBLE     to "jdouble",
            VOID       to "void"
        )
        val classTypeMap = mapOf(
            STRING     to "jstring",
            BYTEBUFFER to "jbytebuffer",
            CROSSBASE  to "jobject"
        )

        var cppType = toString(primitiveTypeMap, classTypeMap, isConst)

        return cppType
    }

    fun toString(primitiveTypeMap: Map<CrossVariableType, String>,
                 classTypeMap: Map<CrossVariableType, String>,
                 isConst: Boolean = false): String {
        var cppType = primitiveTypeMap[this]
        if(cppType == null) {
            cppType = classTypeMap[this]
        }

        if(isConst
        && ! primitiveTypeMap.contains(this)) {
            cppType = "const ${cppType!!}&"
        }

        return cppType!!
    }
}
