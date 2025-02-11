package dev.michaelomichael.intellij.plugins.methodnumbers.hints

import com.intellij.testFramework.utils.inlays.declarative.DeclarativeInlayHintsProviderTestCase
import org.intellij.lang.annotations.Language

class MethodNumbersInlayHintsProviderKotlinTest : DeclarativeInlayHintsProviderTestCase() {

    fun `test basic cases`() {
        val text = """
            class Test {
              /*<# 1 #>*/public fun greeting(): String {
                  return "Hello, world!"
              }
              
              /*<# 2 #>*/public fun farewell() = "Goodbye, cruel world!"
            }
        """.trimIndent()
        testTypeHints(text)
    }

    private fun testTypeHints(@Language("Kotlin") text: String) {
        doTestProvider("Test.kt", text, MethodNumbersInlayHintsProvider())
    }
}