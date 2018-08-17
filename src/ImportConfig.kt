import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "ImportConfig", storages = [(Storage("ImportConfig.xml"))])
class ImportConfig : PersistentStateComponent<ImportConfig> {

    var defaultPath: String? = null
    var hasDefault: Boolean = false


    override fun getState(): ImportConfig {
        return this
    }

    override fun loadState(importConfig: ImportConfig) {
        XmlSerializerUtil.copyBean(importConfig, this)
    }

    companion object {

        fun getInstance(project: Project): ImportConfig {
            return ServiceManager.getService(project, ImportConfig::class.java)
        }
    }
}
