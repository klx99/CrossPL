package org.elastos.tools.crosspl.annotation

import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@Target(AnnotationTarget.CLASS)
annotation class TestAnnotation

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.elastos.tools.crosspl.annotation.CrossClass")
@SupportedOptions("kapt.kotlin.generated")
class AnnotationProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?,
                         roundEnv: RoundEnvironment)
            : Boolean {
        System.out.println("==================================");
        return true
    }
}

