package dev.michaelomichael.intellij.plugins.methodnumbers.introspector

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.psi.KtFile

class KotestFunSpecIntrospectorTest : BasePlatformTestCase() {

    fun `test correct locations for method declarations`() {
        val psiFile = myFixture.configureByFile("aaaa/bbbb/KotestFunSpecExample.kt")
        val kotlinFile = assertInstanceOf(psiFile, KtFile::class.java)

        val underTest = KotestFunSpecIntrospector()
        val result = underTest.findAllMethods(kotlinFile)

        // TODO: escape the individual components of the keys
        assertEquals(listOf(
            MethodKey("(\"Top-level test\")",                              113,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level test\")"),
            MethodKey("(\"Top-level disabled test\")",                     152,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level disabled test\")"),

            MethodKey("(\"Nested test\")",                                 245,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Nested test\")"),
            MethodKey("(\"Nested disabled test\")",                        289,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Nested disabled test\")"),

            MethodKey("(\"Nested nested context test\")",                  388,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Nested context\").(\"Nested nested context test\")"),
            MethodKey("(\"Nested nested context disabled test\")",         455,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Nested context\").(\"Nested nested context disabled test\")"),

            MethodKey("(\"Test in loop iteration \$it\")",                 574,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Test in loop iteration \$it\")"),
            MethodKey("(\"Disabled test in loop iteration \$it\")",        641,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Disabled test in loop iteration \$it\")"),

            MethodKey("(\"Test in looped context\")",                      823,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Context in loop iteration \$it\").(\"Test in looped context\")"),
            MethodKey("(\"Disabled test in looped context\")",             895,  "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Context in loop iteration \$it\").(\"Disabled test in looped context\")"),

            MethodKey("(\"Test in disabled looped context\")",             1056, "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Disabled context in loop iteration \$it\").(\"Test in disabled looped context\")"),
            MethodKey("(\"Disabled test in disabled looped context\")",    1137, "aaaa.bbbb.KotestFunSpecExample.(\"Top-level context\").(\"Disabled context in loop iteration \$it\").(\"Disabled test in disabled looped context\")"),

            MethodKey("(\"Test in top-level disabled context\")",          1288, "aaaa.bbbb.KotestFunSpecExample.(\"Top-level disabled context\").(\"Test in top-level disabled context\")"),
            MethodKey("(\"Disabled test in top-level disabled context\")", 1356, "aaaa.bbbb.KotestFunSpecExample.(\"Top-level disabled context\").(\"Disabled test in top-level disabled context\")"),
        ), result)
    }

    override fun getTestDataPath() = "src/test/testData/kotlin"
}
