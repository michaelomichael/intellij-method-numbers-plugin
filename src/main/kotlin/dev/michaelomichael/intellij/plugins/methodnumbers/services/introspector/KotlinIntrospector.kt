package dev.michaelomichael.intellij.plugins.methodnumbers.services.introspector

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.childrenOfType
import org.jetbrains.kotlin.asJava.unwrapped
import org.jetbrains.kotlin.idea.base.psi.kotlinFqName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.startOffset

class KotlinIntrospector : MethodLister {
    override fun findAllMethods(file: PsiFile): List<MethodRef> =
        findAllKotlinMethods(file.unwrapped as PsiFile)
        
    private fun findAllKotlinMethods(file: PsiFile): List<MethodRef> =
        file
            .navigationElement
            .children
            .map { it.unwrapped }
            .flatMap { child ->
                when (child) {
                    is KtClassOrObject -> child.findFunctionsRecursively()
                    is KtNamedFunction -> listOf(child.toMethodRef())
                    else -> emptyList()
                }
            }

    private fun KtClassOrObject.findFunctionsRecursively(): List<MethodRef> = children
            .flatMap { child ->
                when (child) {
                    is KtClassBody -> child.findFunctionsRecursively()
                    else -> emptyList()
                }
            }

    private fun KtClassBody.findFunctionsRecursively(): List<MethodRef> = children
        .flatMap { child ->
            when (child) {
                is KtNamedFunction -> listOf(child.toMethodRef())
                is KtClassOrObject -> child.findFunctionsRecursively()
                else -> emptyList()
            }
        }

    override fun toMethodRef(method: PsiElement): MethodRef =
        when {
            method is KtNamedFunction -> method.toMethodRef()
            method.unwrapped is KtNamedFunction -> (method.unwrapped as KtNamedFunction).toMethodRef()
            else -> error("Cannot produce a method ref for $method")
        }

    override fun isMethod(element: PsiElement): Boolean = element is KtNamedFunction

    override fun getMethodStartOffset(element: PsiElement): Int = 
        when (element) {
            is KtNamedFunction -> element.startOffset
            else -> error("PsiElement is not a method: $element")
        }

    private fun KtNamedFunction.toMethodRef(): MethodRef = 
        MethodRef(
            nameAsSafeName.asString(),
            (this.kotlinFqName?.asString() ?: this.toString())
                .plus(getParameters().joinToString(",", "(", ")")),
        )
    
    private fun KtNamedFunction.getParameters(): List<String> = 
        this.childrenOfType<KtParameterList>()
            .flatMap { paramList -> paramList.childrenOfType<KtParameter>() }
            .map { param -> param.text }
}
