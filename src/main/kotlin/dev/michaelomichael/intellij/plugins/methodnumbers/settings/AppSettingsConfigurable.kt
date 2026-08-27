package dev.michaelomichael.intellij.plugins.methodnumbers.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.NlsContexts
import dev.michaelomichael.intellij.plugins.methodnumbers.MyBundle
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class AppSettingsConfigurable() : Configurable {
    private var mySettingsComponent: AppSettingsComponent? = null

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): @NlsContexts.ConfigurableName String = MyBundle.message("settings.title")

    override fun getPreferredFocusedComponent(): JComponent =
        mySettingsComponent!!.preferredFocusedComponent

    override fun createComponent(): JComponent =
        AppSettingsComponent()
            .also { mySettingsComponent = it }
            .mainPanel

    override fun isModified(): Boolean =
        mySettingsComponent!!.enabled != getState().enabled &&
                mySettingsComponent!!.applyOnlyToTests != getState().applyOnlyToTests

    override fun apply() {
        getState().enabled = mySettingsComponent!!.enabled
        getState().applyOnlyToTests = mySettingsComponent!!.applyOnlyToTests
    }

    override fun reset() {
        mySettingsComponent!!.enabled = getState().enabled
        mySettingsComponent!!.applyOnlyToTests = getState().applyOnlyToTests
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }

    private fun getState() = AppSettings.Companion.getInstance().state
}