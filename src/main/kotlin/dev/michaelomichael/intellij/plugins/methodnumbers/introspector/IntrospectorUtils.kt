package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.base.psi.getLineStartOffset

fun getLineStartOffset(element: PsiElement, lineNumber: Int): Int = 
    element
        .containingFile
        .getLineStartOffset(lineNumber, true)
        ?: error("Failed to get line start offset for line #$lineNumber in file ${element.containingFile}")