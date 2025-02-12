package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

data class MethodKey(val name: String, val offset: Int, val key: String)

interface Introspector {
    fun findAllMethods(file: PsiFile): List<MethodKey>
    
    fun toMethodKey(method: PsiElement): MethodKey
    
    fun isMethod(element: PsiElement): Boolean
    
    fun getMethodStartOffset(element: PsiElement): Int
}