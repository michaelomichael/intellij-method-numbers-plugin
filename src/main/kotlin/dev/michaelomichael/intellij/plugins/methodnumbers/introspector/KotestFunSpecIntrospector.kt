package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.parentsOfType
import org.jetbrains.kotlin.asJava.unwrapped
import org.jetbrains.kotlin.idea.base.psi.getLineNumber
import org.jetbrains.kotlin.idea.base.psi.kotlinFqName
import org.jetbrains.kotlin.idea.structuralsearch.visitor.KotlinRecursiveElementWalkingVisitor
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getSuperNames

class KotestFunSpecIntrospector : Introspector {
    private val kotestTestMethodNames = setOf("test", "xtest")

    override fun findAllMethods(file: PsiFile): List<MethodKey> =
        findAllKotestTests(file.unwrapped as PsiFile)

    override fun toMethodKey(method: PsiElement): MethodKey =
        when {
            method is KtCallExpression -> method.toMethodRef()
            method.unwrapped is KtCallExpression -> (method.unwrapped as KtCallExpression).toMethodRef()
            else -> error("Cannot produce a method ref for $method")
        }

    override fun isMethod(element: PsiElement): Boolean =
        element is KtCallExpression && element.isKotestTestMethodCall

    override fun getMethodStartOffset(element: PsiElement): Int =
        when (element) {
            is KtCallExpression -> element
                .getLineNumber()
                .let { getLineStartOffset(element, it) }

            else -> error("PsiElement is not a Kotest method call: $element")
        }

    private fun findAllKotestTests(file: PsiFile): List<MethodKey> =
        KotestVisitor()
            .also { file.accept(it) }
            .testMethodKeys

    private fun KtCallExpression.toMethodRef(): MethodKey =
        MethodKey(
            name = this.testOrContextName,
            offset = getMethodStartOffset(this),
            key = "${this.parentClassFqName}.${this.contextTrail}"
        )

    private val KtCallExpression.contextTrail: String
        get() = this.parentsOfType<KtCallExpression>()
            .toList()
            .reversed()
            .filter { it.valueArgumentList != null }
            .joinToString(".") { it.valueArgumentList!!.text }

    private val PsiElement.parentClassFqName
        get() = this.parentsOfType<KtClassOrObject>().single().kotlinFqName

    private val KtCallExpression.isKotestTestMethodCall: Boolean
        get() = targetMethodName in kotestTestMethodNames

    private val KtCallExpression.testOrContextName: String
        get() = this.childrenOfType<KtValueArgumentList>().single().text

    private val KtCallExpression.targetMethodName: String
        get() = this.childrenOfType<KtReferenceExpression>().single().text

    private inner class KotestVisitor : KotlinRecursiveElementWalkingVisitor() {
        val testMethodKeys = mutableListOf<MethodKey>()

        override fun visitCallExpression(expression: KtCallExpression) {
            super.visitCallExpression(expression)

            if (expression.isKotestTestMethodCall) {
                testMethodKeys.add(expression.toMethodRef())
            }
        }
    }

    companion object {
        fun isKotestFunSpec(file: PsiFile): Boolean =
            file.navigationElement.children
                .map { it.unwrapped }
                .filterIsInstance<KtClassOrObject>()
                .flatMap { it.getSuperNames() }
                .any { it == "FunSpec" }
    }
}

