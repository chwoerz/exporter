import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.components.JBList
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import impl.RunConfImporter
import java.awt.Dimension
import java.awt.Insets
import javax.swing.*

class RunConfigImporterUI : AnAction() {

    private var dialogBuilder: DialogBuilder? = null
    private var newConfigurations = listOf<RunnerAndConfigurationSettings>()

    override fun actionPerformed(e: AnActionEvent) {
        dialogBuilder = DialogBuilder().centerPanel(createExportDialog(e.project))
        dialogBuilder!!.centerPanel.preferredSize = Dimension(600, 300)
        dialogBuilder!!.setTitle("Import All Run Configurations")
        dialogBuilder!!.addOkAction()
        dialogBuilder!!.addCancelAction()
        dialogBuilder!!.setOkActionEnabled(false)
        dialogBuilder!!.window.setSize(500, 300)
        dialogBuilder!!.centerPanel.setSize(500, 300)
        dialogBuilder!!.setOkOperation {
            RunConfImporter.getInstance(e.project!!).importConfigs(newConfigurations)
            dialogBuilder!!.dialogWrapper.close(1)
        }
        dialogBuilder!!.okAction.setText("Import")
        dialogBuilder!!.show()
    }

    private fun createExportDialog(project: Project?): JComponent {
        val panel = JPanel()
        panel.layout = GridLayoutManager(3, 2, Insets(0, 0, 0, 0), -1, -1)
        val importField = JTextField()
        importField.isEditable = false
        panel.add(importField, GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, Dimension(150, -1), null, 0, false))
        val openButton = JButton()
        openButton.text = "Choose..."
        panel.add(openButton, GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        val spacer1 = Spacer()
        panel.add(spacer1, GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false))
        val model = DefaultListModel<String>()
        val importList = JBList(model)
        importList.isEnabled = false
        importList.setEmptyText("Choose folder to import configurations from")
        panel.add(importList, GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, Dimension(150, 50), null, 0, false))


        openButton.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            descriptor.description = "Folder to import configurations from"
            FileChooser.chooseFiles(descriptor, project, null) { virtualFiles ->
                val importPath = virtualFiles[0].path
                importField.text = importPath
                newConfigurations = RunConfImporter.getInstance(project!!).retrieveNewConfigs(importPath)
                newConfigurations.stream()
                        .map { settings -> settings.configuration }
                        .forEach { setting -> model.addElement(setting.type.displayName + " : " + setting.name) }
                importList.isVisible = !newConfigurations.isEmpty()
                importList.setEmptyText("No new configurations found to import")


                dialogBuilder!!.setOkActionEnabled(true)
            }
        }
        return panel
    }
}
