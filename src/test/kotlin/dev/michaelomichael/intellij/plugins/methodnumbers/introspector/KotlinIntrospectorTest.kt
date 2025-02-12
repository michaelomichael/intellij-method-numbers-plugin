package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.psi.KtFile

class KotlinIntrospectorTest : BasePlatformTestCase() {

    fun `test correct refs for methods`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.kt")
        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        
        val underTest = KotlinIntrospector()
        val result = underTest.findAllMethods(kotlinFile)
        
        assertEquals(listOf(
            MethodKey("greeting",                  39,   "aaaa.bbbb.MyClass.greeting()"), 
            MethodKey("farewell",                  116,  "aaaa.bbbb.MyClass.farewell(fakeChild: myInnerClass?,names: List<String>)"), 
            MethodKey("innerGreeting",             290,  "aaaa.bbbb.MyClass.MyInnerClass.innerGreeting()"), 
            MethodKey("innerInnerGreeting",        441,  "aaaa.bbbb.MyClass.MyInnerClass.MyInnerInnerClass.innerInnerGreeting()"), 
            MethodKey("innerInnerFarewell",        566,  "aaaa.bbbb.MyClass.MyInnerClass.MyInnerInnerClass.innerInnerFarewell()"), 
            MethodKey("innerFarewell",             705,  "aaaa.bbbb.MyClass.MyInnerClass.innerFarewell()"), 
            MethodKey("innerGreeting",             857,  "aaaa.bbbb.MyClass.MyStaticInnerClass.innerGreeting()"), 
            MethodKey("innerFarewell",             962,  "aaaa.bbbb.MyClass.MyStaticInnerClass.innerFarewell()"),
            MethodKey("generateStatic",            1104, "aaaa.bbbb.MyClass.Companion.generateStatic()"),
            MethodKey("privateTopLevelGreeting",   1248, "aaaa.bbbb.PrivateTopLevelClass.privateTopLevelGreeting()"), 
            MethodKey("privateTopLevelFarewell",   1352, "aaaa.bbbb.PrivateTopLevelClass.privateTopLevelFarewell()"),
            MethodKey("topLevelFunction",          1463, "aaaa.bbbb.topLevelFunction()"),
        ), result)
    }

    override fun getTestDataPath() = "src/test/testData/kotlin"
}
