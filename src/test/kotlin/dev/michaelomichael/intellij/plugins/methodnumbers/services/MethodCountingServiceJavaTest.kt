package dev.michaelomichael.intellij.plugins.methodnumbers.services

import com.intellij.openapi.components.service
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.childrenOfType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import dev.michaelomichael.intellij.plugins.methodnumbers.services.MethodCountingService.MethodIndexDetails

class MethodCountingServiceJavaTest : BasePlatformTestCase() {

    fun `test uses correct file`() {
        val psiFile = myFixture.configureByFile("cccc/MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val method1 = mainClass.childrenOfType<PsiMethod>()[0]
        val method2 = mainClass.childrenOfType<PsiMethod>()[1]

        assertMethodNumberEquals(2, 6, underTest.getMethodNumber(method1))
        assertMethodNumberEquals(5, 6, underTest.getMethodNumber(method2))
    }

    fun `test correct number for method in main java class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val method1 = mainClass.childrenOfType<PsiMethod>()[0]
        val method2 = mainClass.childrenOfType<PsiMethod>()[1]

        assertMethodNumberEquals(0, 11, underTest.getMethodNumber(method1))
        assertMethodNumberEquals(1, 11, underTest.getMethodNumber(method2))
    }

    fun `test correct number for static method in main java class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]

        val staticMethod = mainClass.childrenOfType<PsiMethod>()[2]

        assertMethodNumberEquals(8, 11, underTest.getMethodNumber(staticMethod))
    }

    fun `test correct number for method in inner class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val innerClass = mainClass.childrenOfType<PsiClass>()[0]

        val innerClassMethod1 = innerClass.childrenOfType<PsiMethod>()[0]
        val innerClassMethod2 = innerClass.childrenOfType<PsiMethod>()[1]

        assertMethodNumberEquals(2, 11, underTest.getMethodNumber(innerClassMethod1))
        assertMethodNumberEquals(5, 11, underTest.getMethodNumber(innerClassMethod2))
    }

    fun `test correct number for method in inner inner class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val innerClass = mainClass.childrenOfType<PsiClass>()[0]
        val innerInnerClass = innerClass.childrenOfType<PsiClass>()[0]

        val innerInnerClassMethod1 = innerInnerClass.childrenOfType<PsiMethod>()[0]
        val innerInnerClassMethod2 = innerInnerClass.childrenOfType<PsiMethod>()[1]

        assertMethodNumberEquals(3, 11, underTest.getMethodNumber(innerInnerClassMethod1))
        assertMethodNumberEquals(4, 11, underTest.getMethodNumber(innerInnerClassMethod2))
    }

    fun `test correct number for method in static inner class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val mainClass = javaFile.childrenOfType<PsiClass>()[0]
        val staticInnerClass = mainClass.childrenOfType<PsiClass>()[1]

        val staticInnerClassMethod1 = staticInnerClass.childrenOfType<PsiMethod>()[0]
        val staticInnerClassMethod2 = staticInnerClass.childrenOfType<PsiMethod>()[1]

        // TODO: Why is the cast needed here? If I remove it, running the tests gives a NoSuchMethodError
        assertMethodNumberEquals(6, 11, underTest.getMethodNumber(staticInnerClassMethod1 as PsiElement))
        assertMethodNumberEquals(7, 11, underTest.getMethodNumber(staticInnerClassMethod2))
    }
    
    fun `test correct number for method in private top-level class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.java")
        val underTest = project.service<MethodCountingService>()

        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        val privateTopLevelClass = javaFile.childrenOfType<PsiClass>()[1]
        
        val privateTopLevelClassMethod1 = privateTopLevelClass.childrenOfType<PsiMethod>()[0]
        val privateTopLevelClassMethod2 = privateTopLevelClass.childrenOfType<PsiMethod>()[1]

        assertMethodNumberEquals(9, 11, underTest.getMethodNumber(privateTopLevelClassMethod1))
        assertMethodNumberEquals(10, 11, underTest.getMethodNumber(privateTopLevelClassMethod2))
    }
    
    private fun assertMethodNumberEquals(expectedIndex: Int, expectedTotal: Int, actual: MethodIndexDetails?) {
        assertEquals("Wrong method number", expectedIndex, actual?.methodIndexInFile)
        assertEquals("Wrong total number of methods", expectedTotal, actual?.numMethodsInFile)
    }
    
    override fun getTestDataPath() = "src/test/testData/java"
}
