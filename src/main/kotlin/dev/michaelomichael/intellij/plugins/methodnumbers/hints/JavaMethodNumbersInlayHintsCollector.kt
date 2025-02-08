package dev.michaelomichael.intellij.plugins.methodnumbers.hints

import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import dev.michaelomichael.intellij.plugins.methodnumbers.services.MethodCountingService

class JavaMethodNumbersInlayHintsCollector : SharedBypassCollector {

    override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
        if (!element.isValid || element.project.isDefault || element !is PsiMethod) {
            return
        }

        val methodNumber = element
            .project
            .service<MethodCountingService>()
            .getMethodNumber(element)

        if (methodNumber != null) {
            addInlayToMethod(element, methodNumber, sink)
        } else {
            thisLogger().warn("Cannot find a method number for $element")
        }
    }

    private fun addInlayToMethod(element: PsiMethod, methodNumber: Int, sink: InlayTreeSink) {
        sink.addPresentation(
            InlineInlayPosition(element.modifierList.textOffset, relatedToPrevious = false),
            hasBackground = true
        ) {
            text("$methodNumber")
        }
    }
}