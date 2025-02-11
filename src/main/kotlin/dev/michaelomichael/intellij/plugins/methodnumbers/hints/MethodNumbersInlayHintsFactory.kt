package dev.michaelomichael.intellij.plugins.methodnumbers.hints

import com.intellij.codeInsight.hints.declarative.InlayHintsProviderFactory
import com.intellij.codeInsight.hints.declarative.InlayProviderInfo
import com.intellij.lang.Language
import com.intellij.openapi.diagnostic.thisLogger

class MethodNumbersInlayHintsFactory : InlayHintsProviderFactory {
    init {
        thisLogger().info("Initialised MethodNumbersInlayHintsFactory")
    }

    private val provider = InlayProviderInfo(
        MethodNumbersInlayHintsProvider(),
        "dev.michaelomichael.intellij.plugins.methodnumbers.hints.MethodNumbers",
        setOf(),
        true,
        "methodnumbers"
    )

    override fun getProviderInfo(language: Language, providerId: String): InlayProviderInfo = provider
    override fun getProvidersForLanguage(language: Language): List<InlayProviderInfo> = listOf(provider)

    override fun getSupportedLanguages(): Set<Language> =
        setOf(
            Language.findLanguageByID("JAVA"),
            Language.findLanguageByID("Kotlin"),
        )
            .filterNotNull()
            .toSet()
}