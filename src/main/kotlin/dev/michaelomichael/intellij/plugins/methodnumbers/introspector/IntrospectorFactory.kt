package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.psi.PsiFile

object IntrospectorFactory {
    fun introspectorFor(file: PsiFile): Introspector =
        when (file.fileType.name) {
            "Kotlin" -> KotlinIntrospector()
            "JAVA" -> JavaIntrospector()
            else -> error("Unexpected file type: ${file.fileType.name}")
        }
}