import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.DialogBuilder
import impl.RunConfImporter

class RunConfigImporterUI : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val dialogBuilder = DialogBuilder()
        val dial = RunConfDialog(dialogBuilder, "Import All Run Configurations", "Import", "Choose folder to import configurations from")
        val newConfigurations = mutableListOf<RunnerAndConfigurationSettings>()
        dial.openButton.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            FileChooser.chooseFiles(descriptor, project, null) { virtualFiles ->
                val importPath = virtualFiles[0].path
                dial.pathField.text = importPath
                newConfigurations.clear()
                newConfigurations.addAll(RunConfImporter.getInstance(project).retrieveNewConfigs(importPath))
                val configs = newConfigurations.map { settings -> settings.configuration }
                RunConfUiUtils.initTableModel(dial.model, configs)
                dial.list.isVisible = !newConfigurations.isEmpty()
                dial.list.setEmptyText("No new configurations found to import")


                dialogBuilder.setOkActionEnabled(true)
            }
        }
        dialogBuilder.setOkOperation {
            RunConfImporter.getInstance(project).importConfigs(newConfigurations)
            dialogBuilder.dialogWrapper.close(1)
        }
        dialogBuilder.show()
    }
}
