package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod

class JavaIntrospector : Introspector {
    override fun findAllMethods(file: PsiFile): List<MethodKey> = file
        .children
        .flatMap { child ->
            when (child) {
                is PsiClass -> getMethodsForClass(child)
                else -> emptyList()
            }
        }

    private fun getMethodsForClass(clazz: PsiClass): List<MethodKey> = clazz
        .children
        .flatMap { child ->
            when (child) {
                is PsiClass -> getMethodsForClass(child)
                is PsiMethod -> listOf(toMethodKey(child))
                else -> emptyList()
            }
        }

    override fun toMethodKey(method: PsiElement) = 
        when (method) {
            is PsiMethod -> MethodKey(
                method.name,
                getMethodStartOffset(method),
                "${method.containingClass?.qualifiedName ?: ""}#${method.name}(${method.parameterList.parameters.joinToString(",") { it.text }})",
            )
            else -> error("Not a PsiMethod: $method")
        }

    override fun isMethod(element: PsiElement): Boolean = element is PsiMethod

    override fun getMethodStartOffset(element: PsiElement): Int =
        when (element) {
            is PsiMethod -> element.modifierList.textOffset
            else -> error("PsiElement is not a method: $element")
        }
}



