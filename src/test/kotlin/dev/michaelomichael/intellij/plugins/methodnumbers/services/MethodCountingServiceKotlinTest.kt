package dev.michaelomichael.intellij.plugins.methodnumbers.services

import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import dev.michaelomichael.intellij.plugins.methodnumbers.services.MethodCountingService.MethodIndexDetails
import org.jetbrains.kotlin.psi.KtFile

class MethodCountingServiceKotlinTest : BasePlatformTestCase() {

    fun `test uses correct file`() {
        val psiFile = myFixture.configureByFile("cccc/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val method1 = mainClass.methods[0]
        val method2 = mainClass.methods[1]

        assertMethodNumberEquals(2, 6, underTest.getMethodNumber(method1))
        assertMethodNumberEquals(5, 6, underTest.getMethodNumber(method2))
    }

    fun `test correct number for method in main java class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val method1 = mainClass.methods[0]
        val method2 = mainClass.methods[1]

        assertMethodNumberEquals(0, 12, underTest.getMethodNumber(method1 as PsiElement))
        assertMethodNumberEquals(1, 12, underTest.getMethodNumber(method2))
    }

    fun `test correct number for static method in main java class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val companionObject = mainClass.innerClasses[2]
        val staticMethod = companionObject.methods[0]

        assertMethodNumberEquals(8, 12, underTest.getMethodNumber(staticMethod))
    }

    fun `test correct number for method in inner class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val innerClass = mainClass.innerClasses[0]

        val innerClassMethod1 = innerClass.methods[0]
        val innerClassMethod2 = innerClass.methods[1]

        assertMethodNumberEquals(2, 12, underTest.getMethodNumber(innerClassMethod1))
        assertMethodNumberEquals(5, 12, underTest.getMethodNumber(innerClassMethod2))
    }

    fun `test correct number for method in inner inner class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val innerClass = mainClass.innerClasses[0]
        val innerInnerClass = innerClass.innerClasses[0]

        val innerInnerClassMethod1 = innerInnerClass.methods[0]
        val innerInnerClassMethod2 = innerInnerClass.methods[1]

        assertMethodNumberEquals(3, 12, underTest.getMethodNumber(innerInnerClassMethod1))
        assertMethodNumberEquals(4, 12, underTest.getMethodNumber(innerInnerClassMethod2))
    }

    fun `test correct number for method in static inner class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val staticInnerClass = mainClass.innerClasses[1]

        val staticInnerClassMethod1 = staticInnerClass.methods[0]
        val staticInnerClassMethod2 = staticInnerClass.methods[1]

        assertMethodNumberEquals(6, 12, underTest.getMethodNumber(staticInnerClassMethod1))
        assertMethodNumberEquals(7, 12, underTest.getMethodNumber(staticInnerClassMethod2))
    }
    
    fun `test correct number for method in private top-level class`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val privateTopLevelClass = kotlinFile.classes[1]
        
        val privateTopLevelClassMethod1 = privateTopLevelClass.methods[0]
        val privateTopLevelClassMethod2 = privateTopLevelClass.methods[1]

        assertMethodNumberEquals(9, 12, underTest.getMethodNumber(privateTopLevelClassMethod1))
        assertMethodNumberEquals(10, 12, underTest.getMethodNumber(privateTopLevelClassMethod2))
    }
    
    fun `test correct number for top level method`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val privateTopLevelMethod = kotlinFile.classes[2].methods[0]
        
        assertMethodNumberEquals(11, 12, underTest.getMethodNumber(privateTopLevelMethod))
    }
    
    private fun assertMethodNumberEquals(expectedIndex: Int, expectedTotal: Int, actual: MethodIndexDetails?) {
        assertEquals("Wrong method number", expectedIndex, actual?.methodIndexInFile)
        assertEquals("Wrong total number of methods", expectedTotal, actual?.numMethodsInFile)
    }
    
    override fun getTestDataPath() = "src/test/testData/kotlin"
}
