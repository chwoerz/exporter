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

    fun createPanel(project: Project, importConfig: ImportConfig): JPanel {
        val rootPanel = JPanel()
        rootPanel.layout = GridLayoutManager(5, 3, Insets(0, 0, 0, 0), -1, -1)
        val defaultPathField = JTextField()
        defaultPathField.isEditable = false
        rootPanel.add(defaultPathField, GridConstraints(1, 0, 2, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, Dimension(150, -1), null, 0, false))
        val openButton = JButton()
        openButton.text = "Choose default"
        rootPanel.add(openButton, GridConstraints(1, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        val spacer1 = Spacer()
        rootPanel.add(spacer1, GridConstraints(3, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false))
        val hasDefaultPath = JCheckBox()
        hasDefaultPath.text = "Set a folder to automatically pull the run configrations from at project start"
        rootPanel.add(hasDefaultPath, GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        val importButton = JButton()
        importButton.text = "Import"
        rootPanel.add(importButton, GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        val spacer2 = Spacer()
        rootPanel.add(spacer2, GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false))

        openButton.isEnabled = importConfig.hasDefault
        hasDefaultPath.isSelected = importConfig.hasDefault
        defaultPathField.text = importConfig.defaultPath
        val dir = if (importConfig.defaultPath == null) null else File(importConfig.defaultPath!!)
        importButton.isEnabled = dir != null && importConfig.hasDefault

        hasDefaultPath.addActionListener { event ->
            val abstractButton = event.source as AbstractButton
            val selected = abstractButton.model.isSelected
            openButton.isEnabled = selected
            importConfig.hasDefault = selected
        }
        openButton.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            descriptor.description = "Folder to import configurations from"
            FileChooser.chooseFiles(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, null) { virtualFiles ->
                importConfig.defaultPath = virtualFiles[0].path
                defaultPathField.text = importConfig.defaultPath
                importButton.isEnabled = true
            }
        }

        importButton.addActionListener { RunConfImporter.getInstance(project).importConfigs(defaultPathField.text) }
        return rootPanel
    }


}
