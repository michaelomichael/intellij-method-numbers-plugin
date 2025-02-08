package dev.michaelomichael.intellij.plugins.methodnumbers.listeners

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import dev.michaelomichael.intellij.plugins.methodnumbers.services.MethodCountingService

class MethodChangeListener(private val project: Project) : PsiTreeChangeAdapter() {
    init {
        thisLogger().info("Initialised MethodChangeLister for project ${project.name}")
    }

    override fun childAdded(event: PsiTreeChangeEvent) {
        thisLogger().warn("Child addition detected")
        if (event.isMethodChange()) triggerHintUpdate(event)
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
        thisLogger().warn("Child removal detected")
        if (event.isMethodChange()) triggerHintUpdate(event)
    }

    override fun childMoved(event: PsiTreeChangeEvent) {
        thisLogger().warn("Child move detected")
        if (event.isMethodChange()) triggerHintUpdate(event)
    }

    override fun childReplaced(event: PsiTreeChangeEvent) {
        thisLogger().warn("Child replacement detected")
        if (event.isMethodChange()) triggerHintUpdate(event)
    }

    override fun propertyChanged(event: PsiTreeChangeEvent) {
        thisLogger().warn("Property change detected")
        if (event.isMethodChange()) triggerHintUpdate(event)
    }

    private fun PsiTreeChangeEvent.isMethodChange(): Boolean {
        val element = child ?: return false
        thisLogger().debug("Method change detected for element is $element")
        return element is PsiMethod
    }

    private fun triggerHintUpdate(event: PsiTreeChangeEvent) {
        val file = event.file ?: return
        thisLogger().debug("Triggering full file refresh for method numbers due to a method change event. File is: $file")
        val methodCounter = file.project.service<MethodCountingService>()
        methodCounter.refreshCacheForFile(file)
        DaemonCodeAnalyzer.getInstance(project).restart(file)
    }
}
