package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.childrenOfType
import org.jetbrains.kotlin.asJava.unwrapped
import org.jetbrains.kotlin.idea.base.psi.getLineNumber
import org.jetbrains.kotlin.idea.base.psi.kotlinFqName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.isPrivate


TODO 2026-07-21:
    """
    I updated the code so that it ignores private methods.

    I've added a setting to enable/disable, with the hopes that it could be a keyboard shortcut.
        It shows up in the Preferences panel (under "Tools > Method Numbers") but when you change
        either of the checkboxes and hit OK, then go back in to the prefs panel, the changes haven't
        been saved.

    I bumped the version number to 0.0.5 already in gradle.properties, but need to update the CHANGELOG.md
    file.
    """
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
                    is KtClassOrObject -> child.takeUnless { it.isPrivate() }?.findFunctionsRecursively()
                    is KtNamedFunction -> child.takeUnless { it.isPrivate() }?.toMethodRef()?.let { listOf(it) }
                    else -> null
                } ?: emptyList()
            }

    private fun KtClassOrObject.findFunctionsRecursively(): List<MethodKey> =
        takeUnless { it.isPrivate() }
            ?.children
            ?.flatMap { child ->
                when (child) {
                    is KtClassBody -> child.findFunctionsRecursively()
                    else -> emptyList()
                }
            }
            ?: emptyList()

    private fun KtClassBody.findFunctionsRecursively(): List<MethodKey> = children
        .flatMap { child ->
            when (child) {
                is KtNamedFunction -> child.takeUnless { it.isPrivate() }?.toMethodRef()?.let { listOf(it) }
                is KtClassOrObject -> child.takeUnless { it.isPrivate() }?.findFunctionsRecursively()
                else -> null
            } ?: emptyList()
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
            is KtNamedFunction -> element
                    .identifyingElement
                    ?.getLineNumber()
                    ?.let { getLineStartOffset(element, it) }
                    ?: error("Failed to get line number for identifying element of Kotlin function [$element]") 
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
