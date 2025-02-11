package dev.michaelomichael.intellij.plugins.methodnumbers.services.introspector

import com.intellij.psi.PsiFile

object IntrospectorFactory {
    fun methodFinderFor(file: PsiFile): MethodLister =
        when (file.fileType.name) {
            "Kotlin" -> KotlinIntrospector()
            "JAVA" -> JavaIntrospector()
            else -> error("Unexpected file type: ${file.fileType.name}")
        }
}