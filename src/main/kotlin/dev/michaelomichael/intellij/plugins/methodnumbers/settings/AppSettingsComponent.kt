package dev.michaelomichael.intellij.plugins.methodnumbers.settings

import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import dev.michaelomichael.intellij.plugins.methodnumbers.MyBundle
import javax.swing.JComponent
import javax.swing.JPanel

class AppSettingsComponent {
    val mainPanel: JPanel

    private val applyOnlyToTestsCheckbox = JBCheckBox(MyBundle.message("settings.applyOnlyToTests"))

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addComponent(applyOnlyToTestsCheckbox, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    val preferredFocusedComponent: JComponent
        get() = applyOnlyToTestsCheckbox

    var applyOnlyToTests: Boolean
        get() = applyOnlyToTestsCheckbox.isSelected
        set(newValue) { applyOnlyToTestsCheckbox.isSelected = newValue }
}