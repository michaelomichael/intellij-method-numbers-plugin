package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.testFramework.assertInstanceOf
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class IntrospectorFactoryTest : BasePlatformTestCase() {

    fun `test uses JavaIntrospector for java file`() {
        val psiFile = myFixture.configureByFile("java/aaaa/bbbb/MyClass.java")

        val result = IntrospectorFactory.introspectorFor(psiFile)

        assertInstanceOf<JavaIntrospector>(result)
    }

    fun `test uses KotlinIntrospector for kotlin file`() {
        val psiFile = myFixture.configureByFile("kotlin/aaaa/bbbb/MyClass.kt")

        val result = IntrospectorFactory.introspectorFor(psiFile)

        assertInstanceOf<KotlinIntrospector>(result)
    }

    fun `test uses KotestFunSpecIntrospector for Kotest file`() {
        val psiFile = myFixture.configureByFile("kotlin/aaaa/bbbb/KotestFunSpecExample.kt")

        val result = IntrospectorFactory.introspectorFor(psiFile)

        assertInstanceOf<KotestFunSpecIntrospector>(result)
    }

    fun `test does not return an introspector for Properties file`() {
        val psiFile = myFixture.configureByFile("other/Test.properties")

        val result = IntrospectorFactory.introspectorFor(psiFile)

        assertNull(result)
    }

    override fun getTestDataPath() = "src/test/testData"
}
