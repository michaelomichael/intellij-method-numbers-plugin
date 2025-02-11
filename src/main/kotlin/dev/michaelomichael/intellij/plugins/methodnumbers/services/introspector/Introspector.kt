package dev.michaelomichael.intellij.plugins.methodnumbers.services.introspector

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

data class MethodRef(val name: String, val key: String)

interface MethodLister {
    fun findAllMethods(file: PsiFile): List<MethodRef>
    
    fun toMethodRef(method: PsiElement): MethodRef
    
    fun isMethod(element: PsiElement): Boolean
    
    fun getMethodStartOffset(element: PsiElement): Int
}