package dev.michaelomichael.intellij.plugins.methodnumbers.settings

import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "dev.michaelomichael.intellij.plugins.methodnumbers.settings.AppSettings",
    storages = [Storage("SdkSettingsPlugin.xml")]
)
class AppSettings: PersistentStateComponent<AppSettings.State> {
    data class State(var enabled: Boolean = true, var applyOnlyToTests: Boolean = false)

    private var state: State = State()

    override fun getState(): State = this.state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): AppSettings = getApplication().getService(AppSettings::class.java)
    }
}