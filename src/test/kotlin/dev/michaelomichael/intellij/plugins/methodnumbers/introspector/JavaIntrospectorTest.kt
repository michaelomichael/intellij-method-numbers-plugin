package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaIntrospectorTest : BasePlatformTestCase() {

    fun `test correct refs for methods`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.java")
        val javaFile = assertInstanceOf(psiFile, PsiJavaFile::class.java)
        
        val underTest = JavaIntrospector()
        val result = underTest.findAllMethods(javaFile)
        
        assertEquals(listOf(
            MethodKey("greeting",                  57,   "aaaa.bbbb.MyClass#greeting()"), 
            MethodKey("farewell",                  137,  "aaaa.bbbb.MyClass#farewell(MyInnerClass fakeChild,List<String> names)"), 
            MethodKey("innerGreeting",             304,  "aaaa.bbbb.MyClass.MyInnerClass#innerGreeting()"), 
            MethodKey("innerInnerGreeting",        447,  "aaaa.bbbb.MyClass.MyInnerClass.MyInnerInnerClass#innerInnerGreeting()"), 
            MethodKey("innerInnerFarewell",        574,  "aaaa.bbbb.MyClass.MyInnerClass.MyInnerInnerClass#innerInnerFarewell()"), 
            MethodKey("innerFarewell",             712,  "aaaa.bbbb.MyClass.MyInnerClass#innerFarewell()"), 
            MethodKey("staticInnerGreeting",       869,  "aaaa.bbbb.MyClass.MyStaticInnerClass#staticInnerGreeting()"), 
            MethodKey("staticInnerFarewell",       983,  "aaaa.bbbb.MyClass.MyStaticInnerClass#staticInnerFarewell()"),
            MethodKey("generateStatic",            1103, "aaaa.bbbb.MyClass#generateStatic()"),
            MethodKey("privateTopLevelGreeting",   1246, "aaaa.bbbb.PrivateTopLevelClass#privateTopLevelGreeting()"), 
            MethodKey("privateTopLevelFarewell",   1354, "aaaa.bbbb.PrivateTopLevelClass#privateTopLevelFarewell()"),
        ), result)
    }

    override fun getTestDataPath() = "src/test/testData/java"
}
