package org.elastos.tools.crosspl.processor

import com.google.auto.service.AutoService
import org.elastos.tools.crosspl.annotation.CrossClass
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

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
        }

        Log.e("===========================================================")
        return true
    }
}

