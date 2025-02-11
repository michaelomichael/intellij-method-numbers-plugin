package dev.michaelomichael.intellij.plugins.methodnumbers.hints

import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class MethodNumbersInlayHintsProvider : InlayHintsProvider {
    override fun createCollector(file: PsiFile, editor: Editor) = MethodNumbersInlayHintsCollector()
}