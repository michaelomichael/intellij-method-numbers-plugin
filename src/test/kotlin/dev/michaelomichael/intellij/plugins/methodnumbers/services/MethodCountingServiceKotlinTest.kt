package dev.michaelomichael.intellij.plugins.methodnumbers.services

import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.psi.KtFile

class MethodCountingServiceKotlinTest : BasePlatformTestCase() {

    fun `test uses correct file`() {
        val psiFile = myFixture.configureByFile("differentPackage/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val method1 = mainClass.methods[0]
        val method2 = mainClass.methods[1]

        assertEquals(3, underTest.getMethodNumber(method1))
        assertEquals(6, underTest.getMethodNumber(method2))
    }

    fun `test correct number for method in main java class`() {
        val psiFile = myFixture.configureByFile("some/pkg/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val method1 = mainClass.methods[0]
        val method2 = mainClass.methods[1]

        assertEquals(1, underTest.getMethodNumber(method1 as PsiElement))
        assertEquals(2, underTest.getMethodNumber(method2))
    }

    fun `test correct number for static method in main java class`() {
        val psiFile = myFixture.configureByFile("some/pkg/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val companionObject = mainClass.innerClasses[2]
        val staticMethod = companionObject.methods[0]

        assertEquals(9, underTest.getMethodNumber(staticMethod))
    }

    fun `test correct number for method in inner class`() {
        val psiFile = myFixture.configureByFile("some/pkg/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val innerClass = mainClass.innerClasses[0]

        val innerClassMethod1 = innerClass.methods[0]
        val innerClassMethod2 = innerClass.methods[1]

        assertEquals(3, underTest.getMethodNumber(innerClassMethod1))
        assertEquals(6, underTest.getMethodNumber(innerClassMethod2))
    }

    fun `test correct number for method in inner inner class`() {
        val psiFile = myFixture.configureByFile("some/pkg/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val innerClass = mainClass.innerClasses[0]
        val innerInnerClass = innerClass.innerClasses[0]

        val innerInnerClassMethod1 = innerInnerClass.methods[0]
        val innerInnerClassMethod2 = innerInnerClass.methods[1]

        assertEquals(4, underTest.getMethodNumber(innerInnerClassMethod1))
        assertEquals(5, underTest.getMethodNumber(innerInnerClassMethod2))
    }

    fun `test correct number for method in static inner class`() {
        val psiFile = myFixture.configureByFile("some/pkg/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val mainClass = kotlinFile.classes[0]
        val staticInnerClass = mainClass.innerClasses[1]

        val staticInnerClassMethod1 = staticInnerClass.methods[0]
        val staticInnerClassMethod2 = staticInnerClass.methods[1]

        assertEquals(7, underTest.getMethodNumber(staticInnerClassMethod1))
        assertEquals(8, underTest.getMethodNumber(staticInnerClassMethod2))
    }
    
    fun `test correct number for method in private top-level class`() {
        val psiFile = myFixture.configureByFile("some/pkg/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val privateTopLevelClass = kotlinFile.classes[1]
        
        val privateTopLevelClassMethod1 = privateTopLevelClass.methods[0]
        val privateTopLevelClassMethod2 = privateTopLevelClass.methods[1]

        assertEquals(10, underTest.getMethodNumber(privateTopLevelClassMethod1))
        assertEquals(11, underTest.getMethodNumber(privateTopLevelClassMethod2))
    }
    
    fun `test correct number for top level method`() {
        val psiFile = myFixture.configureByFile("some/pkg/MyClass.kt")
        val underTest = project.service<MethodCountingService>()

        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        val privateTopLevelMethod = kotlinFile.classes[2].methods[0]
        
        assertEquals(12, underTest.getMethodNumber(privateTopLevelMethod))
    }
    
    override fun getTestDataPath() = "src/test/testData/kotlin"
}
