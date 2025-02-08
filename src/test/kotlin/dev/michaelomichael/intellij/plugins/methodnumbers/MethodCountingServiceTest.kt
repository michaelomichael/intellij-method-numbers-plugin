package dev.michaelomichael.intellij.plugins.methodnumbers

import com.intellij.openapi.components.service
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.childrenOfType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import dev.michaelomichael.intellij.plugins.methodnumbers.services.MethodCountingService

class MethodCountingServiceTest : BasePlatformTestCase() {

    fun `test uses correct file`() {
        val psiFile = myFixture.configureByFile("spare/MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val method1 = mainClass.childrenOfType<PsiMethod>()[0]
        val method2 = mainClass.childrenOfType<PsiMethod>()[1]

        assertEquals(3, underTest.getMethodNumber(method1))
        assertEquals(6, underTest.getMethodNumber(method2))
    }

    fun `test correct number for method in main java class`() {
        val psiFile = myFixture.configureByFile("MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val method1 = mainClass.childrenOfType<PsiMethod>()[0]
        val method2 = mainClass.childrenOfType<PsiMethod>()[1]

        assertEquals(1, underTest.getMethodNumber(method1))
        assertEquals(2, underTest.getMethodNumber(method2))
    }

    fun `test correct number for static method in main java class`() {
        val psiFile = myFixture.configureByFile("MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]

        val staticMethod = mainClass.childrenOfType<PsiMethod>()[2]

        assertEquals(9, underTest.getMethodNumber(staticMethod))
    }

    fun `test correct number for method in inner class`() {
        val psiFile = myFixture.configureByFile("MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val innerClass = mainClass.childrenOfType<PsiClass>()[0]

        val innerClassMethod1 = innerClass.childrenOfType<PsiMethod>()[0]
        val innerClassMethod2 = innerClass.childrenOfType<PsiMethod>()[1]

        assertEquals(3, underTest.getMethodNumber(innerClassMethod1))
        assertEquals(6, underTest.getMethodNumber(innerClassMethod2))
    }

    fun `test correct number for method in inner inner class`() {
        val psiFile = myFixture.configureByFile("MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val innerClass = mainClass.childrenOfType<PsiClass>()[0]
        val innerInnerClass = innerClass.childrenOfType<PsiClass>()[0]

        val innerInnerClassMethod1 = innerInnerClass.childrenOfType<PsiMethod>()[0]
        val innerInnerClassMethod2 = innerInnerClass.childrenOfType<PsiMethod>()[1]

        assertEquals(4, underTest.getMethodNumber(innerInnerClassMethod1))
        assertEquals(5, underTest.getMethodNumber(innerInnerClassMethod2))
    }

    fun `test correct number for method in static inner class`() {
        val psiFile = myFixture.configureByFile("MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val staticInnerClass = mainClass.childrenOfType<PsiClass>()[1]

        val staticInnerClassMethod1 = staticInnerClass.childrenOfType<PsiMethod>()[0]
        val staticInnerClassMethod2 = staticInnerClass.childrenOfType<PsiMethod>()[1]

        assertEquals(7, underTest.getMethodNumber(staticInnerClassMethod1))
        assertEquals(8, underTest.getMethodNumber(staticInnerClassMethod2))
    }

    override fun getTestDataPath() = "src/test/testData"
}
