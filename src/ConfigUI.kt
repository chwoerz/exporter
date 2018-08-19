import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import impl.RunConfImporter
import java.awt.Dimension
import java.awt.Insets
import java.io.File
import javax.swing.*

internal object ConfigUI {

    fun createPanel(project: Project, appConfig: ImportConfigApp, projectConfig: ImportConfig): JPanel {
        val panel = JPanel()
        panel.layout = GridLayoutManager(6, 3, Insets(0, 0, 0, 0), -1, -1)

        val appDefaultCheckbox = JCheckBox()
        appDefaultCheckbox.isSelected = appConfig.hasDefault()
        appDefaultCheckbox.text = "Use Base-Settings for all Projects"
        panel.add(appDefaultCheckbox, GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))

        val appPathField = JTextField()
        appPathField.isEnabled = false
        appPathField.text = appConfig.defaultPath
        panel.add(appPathField, GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, Dimension(150, -1), null, 0, false))

        val appChooseButton = JButton()
        appChooseButton.isEnabled = appDefaultCheckbox.isSelected
        appChooseButton.text = "Choose App wide Path"
        panel.add(appChooseButton, GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))

        val projectDefaultCheckbox = JCheckBox()
        projectDefaultCheckbox.isSelected = projectConfig.hasDefault()
        projectDefaultCheckbox.text = "Use settings for only this project"
        panel.add(projectDefaultCheckbox, GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))

        val spacer1 = Spacer()
        panel.add(spacer1, GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false))

        val projectPathField = JTextField()
        projectPathField.isEnabled = false
        projectPathField.text = projectConfig.defaultPath
        panel.add(projectPathField, GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, Dimension(150, -1), null, 0, false))

        val projectChooseButton = JButton()
        projectChooseButton.text = "Choose Project Path"
        projectChooseButton.isEnabled = projectDefaultCheckbox.isEnabled
        panel.add(projectChooseButton, GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))

        val importButton = JButton()
        importButton.text = "Import"
        panel.add(importButton, GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))


        val dir = if (appConfig.defaultPath == null) null else File(appConfig.defaultPath!!)
        importButton.isEnabled = dir != null && appConfig.hasDefault
        appConfig.hasDefault = true
        appDefaultCheckbox.addActionListener { event ->
            val abstractButton = event.source as AbstractButton
            val selected = abstractButton.model.isSelected
            appChooseButton.isEnabled = selected
            appConfig.hasDefault = selected
        }

        projectDefaultCheckbox.addActionListener { event ->
            val abstractButton = event.source as AbstractButton
            val selected = abstractButton.model.isSelected
            projectChooseButton.isEnabled = selected
            projectConfig.hasDefault = selected
        }

        appChooseButton.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            descriptor.description = "Folder to import configurations from"
            FileChooser.chooseFiles(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, null) { virtualFiles ->
                appConfig.defaultPath = virtualFiles[0].path
                appPathField.text = appConfig.defaultPath
                importButton.isEnabled = true
            }
        }

        projectChooseButton.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            descriptor.description = "Folder to import configurations from"
            FileChooser.chooseFiles(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, null) { virtualFiles ->
                projectConfig.defaultPath = virtualFiles[0].path
                projectPathField.text = projectConfig.defaultPath
                importButton.isEnabled = true
            }
        }

        importButton.addActionListener {
            if (appPathField.text != null) {
                RunConfImporter.getInstance(project).importConfigs(appPathField.text)
            }

            if (projectPathField.text != null) {
                RunConfImporter.getInstance(project).importConfigs(projectPathField.text)
            }
        }

        return panel
    }

}
