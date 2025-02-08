package dev.michaelomichael.intellij.plugins.methodnumbers.hints

import com.intellij.testFramework.utils.inlays.declarative.DeclarativeInlayHintsProviderTestCase
import org.intellij.lang.annotations.Language

class JavaMethodNumbersInlayHintsProviderTest : DeclarativeInlayHintsProviderTestCase() {

    fun `test basic cases`() {
        val text = """
            class Test {
              /*<# 1 #>*/public String greeting() {
                  return "Hello, world!";
              }
              
              /*<# 2 #>*/public String farewell() {
                  return "Goodbye, cruel world!";
              }
            }
        """.trimIndent()
        testTypeHints(text)
    }

    private fun testTypeHints(@Language("Java") text: String) {
        doTestProvider("Test.java", text, JavaMethodNumbersInlayHintsProvider())
    }
}