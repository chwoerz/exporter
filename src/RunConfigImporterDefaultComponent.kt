import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import impl.RunConfImporter

import java.io.File

class RunConfigImporterDefaultComponent internal constructor(private val project: Project) : ProjectComponent {

    override fun projectOpened() {
        val appConfig = ImportConfigApp.getInstance()
        importFromPath(appConfig)
        val importConfig = ImportConfig.getInstance(project)
        importFromPath(importConfig)
    }

    private fun importFromPath(importconfig: ImportConfigInterface) {
        if (importconfig.hasDefault() && importconfig.getPath() != null) {
            val path = importconfig.getPath()
            val dir = File(path!!)
            if (dir.exists()) {
                RunConfImporter.getInstance(project).importConfigs(path)
            }
        }
    }
}
