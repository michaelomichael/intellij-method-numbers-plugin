package dev.michaelomichael.intellij.plugins.methodnumbers.services

import com.intellij.openapi.components.service
import com.intellij.psi.util.elementsAtOffsetUp
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import dev.michaelomichael.intellij.plugins.methodnumbers.services.MethodCountingService.MethodIndexDetails
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile

class MethodCountingServiceKotestTest : BasePlatformTestCase() {
    fun `test correct number for method in main class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/KotestFunSpecExample.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)

        val methodA = kotlinFile.findCallExpression("""test("Top-level test")""")
        val methodB = kotlinFile.findCallExpression("""xtest("Top-level disabled test")""")

        assertMethodNumberEquals(0, 14, underTest.getMethodNumber(methodA))
        assertMethodNumberEquals(1, 14, underTest.getMethodNumber(methodB))
    }

    fun `test correct number for tests in top-level context`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/KotestFunSpecExample.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)

        val methodA = kotlinFile.findCallExpression("""test("Nested test")""")
        val methodB = kotlinFile.findCallExpression("""xtest("Nested disabled test")""")

        assertMethodNumberEquals(2, 14, underTest.getMethodNumber(methodA))
        assertMethodNumberEquals(3, 14, underTest.getMethodNumber(methodB))
    }

    fun `test correct number for tests in nested context`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/KotestFunSpecExample.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)

        val methodA = kotlinFile.findCallExpression("""test("Nested nested context test")""")
        val methodB = kotlinFile.findCallExpression("""xtest("Nested nested context disabled test")""")

        assertMethodNumberEquals(4, 14, underTest.getMethodNumber(methodA))
        assertMethodNumberEquals(5, 14, underTest.getMethodNumber(methodB))
    }

    fun `test correct number for tests in a forEach loop`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/KotestFunSpecExample.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)

        val methodA = kotlinFile.findCallExpression("""test("Test in loop iteration""")
        val methodB = kotlinFile.findCallExpression("""xtest("Disabled test in loop iteration""")

        assertMethodNumberEquals(6, 14, underTest.getMethodNumber(methodA))
        assertMethodNumberEquals(7, 14, underTest.getMethodNumber(methodB))
    }

    fun `test correct number for tests in a context in a forEach loop`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/KotestFunSpecExample.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)

        val methodA = kotlinFile.findCallExpression("""test("Test in looped context""")
        val methodB = kotlinFile.findCallExpression("""xtest("Disabled test in looped context""")

        assertMethodNumberEquals(8, 14, underTest.getMethodNumber(methodA))
        assertMethodNumberEquals(9, 14, underTest.getMethodNumber(methodB))
    }

    fun `test correct number for tests in a disabled context in a forEach loop`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/KotestFunSpecExample.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)

        val methodA = kotlinFile.findCallExpression("""test("Test in disabled looped context""")
        val methodB = kotlinFile.findCallExpression("""xtest("Disabled test in disabled looped context""")

        assertMethodNumberEquals(10, 14, underTest.getMethodNumber(methodA))
        assertMethodNumberEquals(11, 14, underTest.getMethodNumber(methodB))
    }

    fun `test correct number for tests in a top-level disabled context`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/KotestFunSpecExample.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)

        val methodA = kotlinFile.findCallExpression("""test("Test in top-level disabled context""")
        val methodB = kotlinFile.findCallExpression("""xtest("Disabled test in top-level disabled context""")

        assertMethodNumberEquals(12, 14, underTest.getMethodNumber(methodA))
        assertMethodNumberEquals(13, 14, underTest.getMethodNumber(methodB))
    }
    
    @Suppress("SameParameterValue")
    private fun assertMethodNumberEquals(expectedIndex: Int, expectedTotal: Int, actual: MethodIndexDetails?) {
        assertEquals("Wrong method number", expectedIndex, actual?.methodIndexInFile)
        assertEquals("Wrong total number of methods", expectedTotal, actual?.numMethodsInFile)
    }

    @Suppress("UnstableApiUsage")
    private fun KtFile.findCallExpression(expressionSourceCode: String): KtCallExpression =
        text.indexOf(expressionSourceCode)
            .takeIf { it >= 0 }
            ?.let { offset ->
                elementsAtOffsetUp(offset)
                    .asSequence()
                    .toList()
                    .map { it.first }
                    .filterIsInstance<KtCallExpression>()
                    .single { it.text.startsWith(expressionSourceCode) }
            }
            ?: error("Can't find expression source code for [$expressionSourceCode]")

    override fun getTestDataPath() = "src/test/testData/kotlin"
}
