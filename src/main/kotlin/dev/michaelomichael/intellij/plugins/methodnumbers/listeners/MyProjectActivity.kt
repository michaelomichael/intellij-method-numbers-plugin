package dev.michaelomichael.intellij.plugins.methodnumbers.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.psi.PsiManager

class MyProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        PsiManager.getInstance(project).addPsiTreeChangeListener(MethodChangeListener(project), project)
    }
}