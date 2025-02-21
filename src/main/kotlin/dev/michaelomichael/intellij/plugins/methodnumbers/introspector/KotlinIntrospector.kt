package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.childrenOfType
import org.jetbrains.kotlin.asJava.unwrapped
import org.jetbrains.kotlin.idea.base.psi.getLineNumber
import org.jetbrains.kotlin.idea.base.psi.getLineStartOffset
import org.jetbrains.kotlin.idea.base.psi.kotlinFqName
import org.jetbrains.kotlin.psi.*

class KotlinIntrospector : Introspector {
    override fun findAllMethods(file: PsiFile): List<MethodKey> =
        findAllKotlinMethods(file.unwrapped as PsiFile)
        
    private fun findAllKotlinMethods(file: PsiFile): List<MethodKey> =
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

    private fun KtClassOrObject.findFunctionsRecursively(): List<MethodKey> = children
            .flatMap { child ->
                when (child) {
                    is KtClassBody -> child.findFunctionsRecursively()
                    else -> emptyList()
                }
            }

    private fun KtClassBody.findFunctionsRecursively(): List<MethodKey> = children
        .flatMap { child ->
            when (child) {
                is KtNamedFunction -> listOf(child.toMethodRef())
                is KtClassOrObject -> child.findFunctionsRecursively()
                else -> emptyList()
            }
        }

    override fun toMethodKey(method: PsiElement): MethodKey =
        when {
            method is KtNamedFunction -> method.toMethodRef()
            method.unwrapped is KtNamedFunction -> (method.unwrapped as KtNamedFunction).toMethodRef()
            else -> error("Cannot produce a method ref for $method")
        }

    override fun isMethod(element: PsiElement): Boolean = element is KtNamedFunction

    override fun getMethodStartOffset(element: PsiElement): Int = 
        when (element) {
            is KtNamedFunction -> 
                element
                    .identifyingElement
                    ?.getLineNumber()
                    ?.let { lineNumber ->
                        element.containingFile.getLineStartOffset(lineNumber, true)
                            ?: error("Failed to get line start offset for line #$lineNumber in file ${element.containingFile}") 
                    } ?: error("Failed to get line number for identifying element of Kotlin function [$element]") 
            else -> error("PsiElement is not a method: $element")
        }

    private fun KtNamedFunction.toMethodRef(): MethodKey = 
        MethodKey(
            nameAsSafeName.asString(),
            getMethodStartOffset(this),
            (this.kotlinFqName?.asString() ?: this.toString())
                .plus(getParameters().joinToString(",", "(", ")")),
        )
    
    private fun KtNamedFunction.getParameters(): List<String> = 
        this.childrenOfType<KtParameterList>()
            .flatMap { paramList -> paramList.childrenOfType<KtParameter>() }
            .map { param -> param.text }
}
