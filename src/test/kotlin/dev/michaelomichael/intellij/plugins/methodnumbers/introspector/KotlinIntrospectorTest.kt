package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.psi.KtFile

class KotlinIntrospectorTest : BasePlatformTestCase() {

    fun `test correct locations for methods`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/MyClass.kt")
        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)
        
        val underTest = KotlinIntrospector()
        val result = underTest.findAllMethods(kotlinFile)
        
        assertEquals(listOf(
            MethodKey("greeting",                  49,   "aaaa.bbbb.MyClass.greeting()"), 
            MethodKey("farewell",                  126,  "aaaa.bbbb.MyClass.farewell(fakeChild: myInnerClass?,names: List<String>)"), 
            MethodKey("innerGreeting",             304,  "aaaa.bbbb.MyClass.MyInnerClass.innerGreeting()"), 
            MethodKey("innerInnerGreeting",        459,  "aaaa.bbbb.MyClass.MyInnerClass.MyInnerInnerClass.innerInnerGreeting()"), 
            MethodKey("innerInnerFarewell",        584,  "aaaa.bbbb.MyClass.MyInnerClass.MyInnerInnerClass.innerInnerFarewell()"), 
            MethodKey("innerFarewell",             719,  "aaaa.bbbb.MyClass.MyInnerClass.innerFarewell()"), 
            MethodKey("innerGreeting",             871,  "aaaa.bbbb.MyClass.MyStaticInnerClass.innerGreeting()"), 
            MethodKey("innerFarewell",             976,  "aaaa.bbbb.MyClass.MyStaticInnerClass.innerFarewell()"),
            MethodKey("generateStatic",            1118, "aaaa.bbbb.MyClass.Companion.generateStatic()"),
            MethodKey("privateTopLevelGreeting",   1258, "aaaa.bbbb.PrivateTopLevelClass.privateTopLevelGreeting()"), 
            MethodKey("privateTopLevelFarewell",   1363, "aaaa.bbbb.PrivateTopLevelClass.privateTopLevelFarewell()"),
            MethodKey("topLevelFunction",          1471, "aaaa.bbbb.topLevelFunction()"),
        ), result)
    }

    override fun getTestDataPath() = "src/test/testData/kotlin"
}
