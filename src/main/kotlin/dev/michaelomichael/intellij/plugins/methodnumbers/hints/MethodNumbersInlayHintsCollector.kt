package dev.michaelomichael.intellij.plugins.methodnumbers.hints

import com.intellij.codeInsight.hints.declarative.*
import com.intellij.codeInsight.hints.declarative.HintFontSize.ABitSmallerThanInEditor
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement
import dev.michaelomichael.intellij.plugins.methodnumbers.MyBundle
import dev.michaelomichael.intellij.plugins.methodnumbers.introspector.IntrospectorFactory
import dev.michaelomichael.intellij.plugins.methodnumbers.services.MethodCountingService
import dev.michaelomichael.intellij.plugins.methodnumbers.introspector.IntrospectorFactory.introspectorFor
import dev.michaelomichael.intellij.plugins.methodnumbers.services.MethodCountingService.MethodIndexDetails

class MethodNumbersInlayHintsCollector : SharedBypassCollector {

    override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
        if (!element.isValid || element.project.isDefault || !IntrospectorFactory.isFiletypeSupported(element.containingFile)) {
            return
        }

        val introspector = introspectorFor(element.containingFile) ?: return
            
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

    private fun addInlayToMethod(offset: Int, methodIndexDetails: MethodIndexDetails, sink: InlayTreeSink) {
        sink.addPresentation(
            InlineInlayPosition(offset, relatedToPrevious = false),
            hintFormat = HintFormat.default.withFontSize(ABitSmallerThanInEditor),
            tooltip = MyBundle.message("hintTooltip", methodIndexDetails.methodIndexInFile+1, methodIndexDetails.numMethodsInFile)
        ) {
            text("${methodIndexDetails.methodIndexInFile+1}")
        }
    }
}