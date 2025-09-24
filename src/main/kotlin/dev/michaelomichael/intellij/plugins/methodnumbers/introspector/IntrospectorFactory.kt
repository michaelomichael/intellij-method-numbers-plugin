package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.openapi.components.service
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiFile
import dev.michaelomichael.intellij.plugins.methodnumbers.introspector.KotestFunSpecIntrospector.Companion.isKotestFunSpec
import dev.michaelomichael.intellij.plugins.methodnumbers.settings.AppSettings

object IntrospectorFactory {
    fun isFiletypeSupported(file: PsiFile): Boolean =
        isTestModeRestrictionSatisfied(file) &&
                when (file.fileType.name) {
                    "Kotlin", "JAVA" -> true
                    else -> false
                }

    fun introspectorFor(file: PsiFile): Introspector {
        if (!isTestModeRestrictionSatisfied(file)) {
            error("File '$file' is not a candidate for introspection because it is not a test file")
        }

        return when (file.fileType.name) {
            "Kotlin" -> if (isKotestFunSpec(file)) KotestFunSpecIntrospector() else KotlinIntrospector()
            "JAVA" -> JavaIntrospector()
            else -> error("File '$file' has file type '${file.fileType.name} and so is not supported")
        }
    }

    private fun isTestModeRestrictionSatisfied(file: PsiFile) =
        (!isApplyToTestFilesOnlyMode() || isTestScope(file))

    private fun isApplyToTestFilesOnlyMode() = service<AppSettings>().state?.applyOnlyToTests ?: false

    private fun isTestScope(file: PsiFile): Boolean =
            ProjectRootManager.getInstance(file.project)
                .fileIndex.isInTestSourceContent(file.virtualFile)
}