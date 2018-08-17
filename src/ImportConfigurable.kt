import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class ImportConfigurable(private val project: Project) : SearchableConfigurable {
    private val importConfig = ImportConfig.getInstance(project)

    override fun getId(): String {
        return "preferences.RunConfConfigurable"
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String {
        return "RunConf RunConfImporter"
    }

    override fun createComponent(): JComponent? {
        return ConfigUI.createPanel(project, importConfig)
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun apply() {

    }

    override fun disposeUIResources() {
        if (!importConfig.hasDefault) {
            importConfig.defaultPath = null
        }
    }
}
