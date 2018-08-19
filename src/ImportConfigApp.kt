import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "ImportConfigApp", storages = [(Storage("ImportConfigApp.xml"))])
class ImportConfigApp : PersistentStateComponent<ImportConfigApp>, ImportConfigInterface {

    var defaultPath: String? = null
    var hasDefault: Boolean = false


    override fun getPath(): String? {
        return defaultPath
    }

    override fun hasDefault(): Boolean {
        return hasDefault
    }

    override fun getState(): ImportConfigApp {
        return this
    }

    override fun loadState(importConfig: ImportConfigApp) {
        XmlSerializerUtil.copyBean(importConfig, this)
    }

    companion object {

        fun getInstance(): ImportConfigApp {
            return ServiceManager.getService(ImportConfigApp::class.java)
        }
    }
}
