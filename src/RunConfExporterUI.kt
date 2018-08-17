import com.intellij.execution.impl.RunManagerImpl
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
import com.intellij.util.ui.JBUI
import java.awt.Dimension
import javax.swing.*

class RunConfExporterUI : AnAction() {

    private var exportPath: String? = null
    private var dialogBuilder: DialogBuilder? = null

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        dialogBuilder = DialogBuilder().centerPanel(createExportDialog(project))
        dialogBuilder!!.centerPanel.preferredSize = Dimension(600, 300)
        dialogBuilder!!.setTitle("Export All Run Configurations")
        dialogBuilder!!.addOkAction()
        dialogBuilder!!.addCancelAction()
        dialogBuilder!!.setOkActionEnabled(false)
        dialogBuilder!!.setOkOperation {
            impl.RunConfExporter.getInstance(project).exportConfigs(exportPath!!)
            dialogBuilder!!.dialogWrapper.close(1)
        }
        dialogBuilder!!.okAction.setText("Export")
        dialogBuilder!!.show()
    }

    private fun createExportDialog(project: Project): JComponent {
        val panel1 = JPanel()
        panel1.layout = GridLayoutManager(2, 2, JBUI.emptyInsets(), -1, -1)
        val exportField = JTextField()
        exportField.isEditable = false
        panel1.add(exportField, GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, Dimension(150, -1), null, 0, false))
        val openButton = JButton()
        openButton.text = "Choose..."
        panel1.add(openButton, GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        val spacer1 = Spacer()
        panel1.add(spacer1, GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false))
        val model = DefaultListModel<String>()
        val exportList = JBList(model)
        exportList.isEnabled = false
        exportList.setEmptyText("No configurations found to export")
        panel1.add(exportList, GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, Dimension(150, 50), null, 0, false))
        val manager = RunManagerImpl.getInstanceImpl(project)
        manager.allConfigurationsList.forEach { config -> model.addElement(config.type.displayName + " : " + config.name) }
        openButton.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            descriptor.description = "Folder to export configurations to"
            FileChooser.chooseFiles(descriptor, project, null) { virtualFiles ->
                val exportPath = virtualFiles[0].path
                exportField.text = exportPath
                this.exportPath = exportPath
                dialogBuilder!!.setOkActionEnabled(true)
            }
        }
        return panel1
    }
}
