import com.intellij.execution.impl.RunManagerImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.DialogBuilder

class RunConfExporterUI : AnAction() {

    private var exportPath: String? = null

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val dialogBuilder = DialogBuilder()
        val dial = RunConfDialog(dialogBuilder, "Export All Run Configurations", "Export", "No configurations found to export")
        dial.openButton.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            descriptor.description = "Folder to export configurations to"
            FileChooser.chooseFiles(descriptor, project, null) { virtualFiles ->
                this.exportPath = virtualFiles[0].path
                dial.pathField.text = exportPath
                dialogBuilder.setOkActionEnabled(true)
            }
        }

        val manager = RunManagerImpl.getInstanceImpl(project)
        manager.allConfigurationsList.forEach { config -> dial.model.addElement("- " + config.type.displayName + " : " + config.name) }
        dialogBuilder.setOkOperation {
            impl.RunConfExporter.getInstance(project).exportConfigs(exportPath!!)
            dialogBuilder.dialogWrapper.close(1)
        }
        dialogBuilder.show()
    }


}
