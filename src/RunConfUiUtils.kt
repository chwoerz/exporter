import com.intellij.execution.configurations.RunConfiguration
import javax.swing.DefaultListModel

class RunConfUiUtils {
    companion object {
        fun initTableModel(model: DefaultListModel<String>, settings: List<RunConfiguration>) {
            settings.forEach { setting -> model.addElement("- " + setting.type.displayName + " : " + setting.name) }
        }
    }
}
