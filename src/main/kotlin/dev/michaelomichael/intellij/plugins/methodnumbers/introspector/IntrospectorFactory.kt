package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.psi.PsiFile
import dev.michaelomichael.intellij.plugins.methodnumbers.introspector.KotestFunSpecIntrospector.Companion.isKotestFunSpec

object IntrospectorFactory {
    fun isFiletypeSupported(file: PsiFile): Boolean =
        when (file.fileType.name) {
            "Kotlin", "JAVA" -> true
            else -> false
        }

    fun introspectorFor(file: PsiFile): Introspector? =
        when (file.fileType.name) {
            "Kotlin" -> if (isKotestFunSpec(file)) KotestFunSpecIntrospector() else KotlinIntrospector()
            "JAVA" -> JavaIntrospector()
            else -> null
        }
}