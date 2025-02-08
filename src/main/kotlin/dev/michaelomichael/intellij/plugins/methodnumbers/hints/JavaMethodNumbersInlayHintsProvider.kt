package dev.michaelomichael.intellij.plugins.methodnumbers.hints

import com.intellij.codeInsight.hints.declarative.InlayHintsCollector
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class JavaMethodNumbersInlayHintsProvider : InlayHintsProvider {
    override fun createCollector(file: PsiFile, editor: Editor): InlayHintsCollector {
        return JavaMethodNumbersInlayHintsCollector()
    }
}