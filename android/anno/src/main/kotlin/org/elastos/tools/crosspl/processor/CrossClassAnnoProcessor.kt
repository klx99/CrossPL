package org.elastos.tools.crosspl.processor

import com.google.auto.service.AutoService
import org.elastos.tools.crosspl.annotation.CrossClass
import java.io.File
import java.nio.file.Paths
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class CrossClassAnnoProcessor : AbstractProcessor() {
    private companion object {
        val OptionGenerateKotlinCode = "generate.kotlin.code"
        val OptionKaptKotlinGenerated = "kapt.kotlin.generated"
    }

    override fun init(procEnv: ProcessingEnvironment ) {
        super.init(procEnv)
        Log.setEnv(procEnv)
    }

    override fun getSupportedOptions(): Set<String> {
        return setOf(OptionKaptKotlinGenerated, OptionGenerateKotlinCode)
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(CrossClass::class.java.name)
    }

    override fun process(annotations: MutableSet<out TypeElement>?,
                         roundEnv: RoundEnvironment)
            : Boolean {
        val annotatedElements = roundEnv.getElementsAnnotatedWith(CrossClass::class.java)
        if(annotatedElements.isEmpty()) {
            return false
        }

        val classInfoList = mutableListOf<CrossClassInfo>()
        for (element in annotatedElements) {
            val classInfo = CrossClassInfo.Parse(element)
            classInfoList.add(classInfo!!)
            Log.w("Found CrossPL Class: ${classInfo.cppClassName}")
        }

        val crossPLDir = getCrossPLDir()
        if(crossPLDir == null) {
            val msg = "Failed to find crosspl  directory."
            Log.e(msg)
            throw CrossPLException(msg)
        }
        val crossProxyDir = getCrossProxyDir()

        val headerFileList = mutableListOf<File>()
        val sourceFileList = mutableListOf<File>()
        classInfoList.forEach {
            val ret = CrossProxyGenerator.Generate(crossProxyDir!!, it)
            if(! ret) {
                val msg = "Failed to generate class: ${it}"
                Log.e(msg)
                throw CrossPLException(msg)
            }

            val proxyHeaderFile = CrossProxyGenerator.GetHeaderFile(crossProxyDir, it)
            val proxySourceFile = CrossProxyGenerator.GetSourceFile(crossProxyDir, it)
            headerFileList.add(proxyHeaderFile)
            sourceFileList.add(proxySourceFile)
        }

        val ret = CrossCMakeFileGenerator.Generate(crossPLDir, headerFileList, sourceFileList)
        if(! ret) {
            val msg = "Failed to generate CMakeLists.txt."
            Log.e(msg)
            throw CrossPLException(msg)
        }


//        Log.e("===========================================================")
        return true
    }

    private fun getCrossPLDir(): File? {
        if(::crossplDir.isInitialized) {
            return crossplDir
        }

        val fileObject = processingEnv.filer.createResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            "dump")
        var path = Paths.get(fileObject.toUri())
        while(path != null
            && path.fileName != null
            && path.fileName.toString() != "tmp") {
            path = path.parent
        }
        if(path == null || path.fileName == null) {
            Log.e("Failed to find build directory.")
            return null
        }

        path = path.resolve("CrossPL")

        crossplDir =  path.toFile()
        crossplDir.mkdirs()

        return crossplDir
    }

    private fun getCrossProxyDir(): File? {
        val crossplDir: File? = getCrossPLDir() ?: return null

        val proxyDir = File(crossplDir, "proxy")
        proxyDir.mkdirs()

        return proxyDir
    }

    private lateinit var crossplDir: File
}

