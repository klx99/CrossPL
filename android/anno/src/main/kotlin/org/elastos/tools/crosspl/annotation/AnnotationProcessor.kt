package org.elastos.tools.crosspl.annotation

import com.google.auto.service.AutoService
import org.gradle.internal.impldep.com.fasterxml.jackson.core.PrettyPrinter
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.*
import kotlin.reflect.jvm.jvmName

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(AnnotationProcessor.CROSSPL_ANNOTATION_TYPE)
@SupportedOptions(AnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class AnnotationProcessor : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        const val CROSSPL_ANNOTATION_TYPE = "org.elastos.tools.crosspl.annotation.CrossClass"
    }

    override fun process(annotations: MutableSet<out TypeElement>?,
                         roundEnv: RoundEnvironment)
            : Boolean {
        val annotatedElements = roundEnv.getElementsAnnotatedWith(CrossClass::class.java)

        processingEnv.messager.printMessage(WARNING, "==================================" + annotatedElements);
//        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
//            processingEnv.messager.printMessage(ERROR, "Can't find the target directory for generated Kotlin files.")
//            return false
//        }

//        File(kaptKotlinGeneratedDir, "testGenerated.kt").apply {
//            parentFile.mkdirs()
//            writeText(generatedKtFile.accept(PrettyPrinter(PrettyPrinterConfiguration())))
//        }

        return false
    }
}

