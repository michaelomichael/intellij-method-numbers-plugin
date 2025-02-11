package dev.michaelomichael.intellij.plugins.methodnumbers.services.introspector

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod

class JavaIntrospector : MethodLister {
    override fun findAllMethods(file: PsiFile): List<MethodRef> = file
        .children
        .flatMap { child ->
            when (child) {
                is PsiClass -> getMethodsForClass(child)
                else -> emptyList()
            }
        }

    private fun getMethodsForClass(clazz: PsiClass): List<MethodRef> = clazz
        .children
        .flatMap { child ->
            when (child) {
                is PsiClass -> getMethodsForClass(child)
                is PsiMethod -> listOf(toMethodRef(child))
                else -> emptyList()
            }
        }

    override fun toMethodRef(method: PsiElement) = 
        when (method) {
            is PsiMethod -> MethodRef(
                method.name,
                "${method.containingClass?.qualifiedName ?: ""}#${method.name}(${method.parameterList.parameters.joinToString(",") { it.text }})"
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



