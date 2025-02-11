package dev.michaelomichael.intellij.plugins.methodnumbers.services.introspector

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.psi.KtFile

class KotlinIntrospectorTest : BasePlatformTestCase() {

    fun `test correct refs for methods`() {
        val psiFile = myFixture.configureByFile("some/pkg/MyClass.kt")
        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        
        val underTest = KotlinIntrospector()
        val result = underTest.findAllMethods(kotlinFile)
        
        assertEquals(listOf(
            MethodRef("greeting",                  "MyClass.greeting()"), 
            MethodRef("farewell",                  "MyClass.farewell(fakeChild: myInnerClass?,names: List<String>)"), 
            MethodRef("innerGreeting",             "MyClass.MyInnerClass.innerGreeting()"), 
            MethodRef("innerInnerGreeting",        "MyClass.MyInnerClass.MyInnerInnerClass.innerInnerGreeting()"), 
            MethodRef("innerInnerFarewell",        "MyClass.MyInnerClass.MyInnerInnerClass.innerInnerFarewell()"), 
            MethodRef("innerFarewell",             "MyClass.MyInnerClass.innerFarewell()"), 
            MethodRef("innerGreeting",             "MyClass.MyStaticInnerClass.innerGreeting()"), 
            MethodRef("innerFarewell",             "MyClass.MyStaticInnerClass.innerFarewell()"),
            
            
            MethodRef("generateStatic",           "MyClass.Companion.generateStatic()"),
            
            MethodRef("privateTopLevelGreeting",   "PrivateTopLevelClass.privateTopLevelGreeting()"), 
            MethodRef("privateTopLevelFarewell",   "PrivateTopLevelClass.privateTopLevelFarewell()"),
            
            MethodRef("topLevelFunction",   "topLevelFunction()"),
        ), result)
    }

    override fun getTestDataPath() = "src/test/testData/kotlin"
}
