package dev.michaelomichael.intellij.plugins.methodnumbers.services.introspector

import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaIntrospectorTest : BasePlatformTestCase() {

    fun `test correct refs for methods`() {
        val psiFile = myFixture.configureByFile("some/pkg/MyClass.java")
        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        
        val underTest = JavaIntrospector()
        val result = underTest.findAllMethods(javaFile)
        
        assertEquals(listOf(
            MethodRef("greeting",                  "MyClass#greeting()"), 
            MethodRef("farewell",                  "MyClass#farewell(MyInnerClass fakeChild,List<String> names)"), 
            MethodRef("innerGreeting",             "MyClass.MyInnerClass#innerGreeting()"), 
            MethodRef("innerInnerGreeting",        "MyClass.MyInnerClass.MyInnerInnerClass#innerInnerGreeting()"), 
            MethodRef("innerInnerFarewell",        "MyClass.MyInnerClass.MyInnerInnerClass#innerInnerFarewell()"), 
            MethodRef("innerFarewell",             "MyClass.MyInnerClass#innerFarewell()"), 
            MethodRef("staticInnerGreeting",       "MyClass.MyStaticInnerClass#staticInnerGreeting()"), 
            MethodRef("staticInnerFarewell",       "MyClass.MyStaticInnerClass#staticInnerFarewell()"),
            
            
            MethodRef("generateStatic",           "MyClass#generateStatic()"),
            
            MethodRef("privateTopLevelGreeting",   "PrivateTopLevelClass#privateTopLevelGreeting()"), 
            MethodRef("privateTopLevelFarewell",   "PrivateTopLevelClass#privateTopLevelFarewell()"),
        ), result)
    }

    override fun getTestDataPath() = "src/test/testData/java"
}
