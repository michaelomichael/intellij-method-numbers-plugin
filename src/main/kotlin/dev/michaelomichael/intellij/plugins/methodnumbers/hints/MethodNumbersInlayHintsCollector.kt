package dev.michaelomichael.intellij.plugins.methodnumbers.hints

import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement
import dev.michaelomichael.intellij.plugins.methodnumbers.MyBundle
import dev.michaelomichael.intellij.plugins.methodnumbers.services.MethodCountingService
import dev.michaelomichael.intellij.plugins.methodnumbers.services.introspector.IntrospectorFactory.methodFinderFor

class MethodNumbersInlayHintsCollector : SharedBypassCollector {

    override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
        if (!element.isValid || element.project.isDefault) {
            return
        }

        val introspector = methodFinderFor(element.containingFile)
            
        if (! introspector.isMethod(element)) {
            return
        }
        
        element
            .project
            .service<MethodCountingService>()
            .getMethodNumber(element)
            ?.also { addInlayToMethod(introspector.getMethodStartOffset(element), it, sink) }
            ?: thisLogger().warn("Cannot find a method number for $element")
    }

    private fun addInlayToMethod(offset: Int, methodNumber: Int, sink: InlayTreeSink) {
        sink.addPresentation(
            InlineInlayPosition(offset, relatedToPrevious = false),
            hasBackground = true,
            tooltip = MyBundle.message("hintTooltip")
        ) {
            text("$methodNumber")
        }
    }
}