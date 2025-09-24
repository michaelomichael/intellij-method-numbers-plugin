package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.openapi.components.service
import com.intellij.testFramework.assertInstanceOf
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import dev.michaelomichael.intellij.plugins.methodnumbers.settings.AppSettings

class IntrospectorFactoryTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        givenApplyOnlyToTestsSettingIs(false)
    }

    override fun tearDown() {
        super.tearDown()
        givenApplyOnlyToTestsSettingIs(false)
    }

    fun `test java file type is supported`() {
        val psiFile = myFixture.configureByFile("java/aaaa/bbbb/MyClass.java")
        assertTrue(IntrospectorFactory.isFiletypeSupported(psiFile))
    }

    fun `test kotlin file type is supported`() {

        val psiFile = myFixture.configureByFile("kotlin/aaaa/bbbb/MyClass.kt")
        assertTrue(IntrospectorFactory.isFiletypeSupported(psiFile))
    }

    fun `test properties file type is not supported`() {

        val psiFile = myFixture.configureByFile("other/Test.properties")
        assertFalse(IntrospectorFactory.isFiletypeSupported(psiFile))
    }

    fun `test non-test java file is not supported when applyOnlyToTests is true`() {
        givenApplyOnlyToTestsSettingIs(true)

        val psiFile = myFixture.configureByFile("java/aaaa/bbbb/MyClass.java")
        assertFalse(IntrospectorFactory.isFiletypeSupported(psiFile))
    }

    fun `test non-test kotlin file is not supported when applyOnlyToTests is true`() {
        givenApplyOnlyToTestsSettingIs(true)

        val psiFile = myFixture.configureByFile("kotlin/aaaa/bbbb/MyClass.kt")
        assertFalse(IntrospectorFactory.isFiletypeSupported(psiFile))
    }

    fun `test kotest file is supported when applyOnlyToTests is true`() {
        givenApplyOnlyToTestsSettingIs(true)

        val psiFile = myFixture.configureByFile("kotlin/aaaa/bbbb/KotestFunSpecExample.kt")
        assertFalse(IntrospectorFactory.isFiletypeSupported(psiFile))
    }

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

    fun `test throws when requesting an introspector for Properties file`() {
        val psiFile = myFixture.configureByFile("other/Test.properties")

        assertThrows(Exception::class.java) {
            IntrospectorFactory.introspectorFor(psiFile)
        }
    }

    override fun getTestDataPath() = "src/test/testData"

    private fun givenApplyOnlyToTestsSettingIs(value: Boolean) {
        service<AppSettings>().state!!.applyOnlyToTests = value
    }
}
