import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import impl.RunConfImporter

import java.io.File

class RunConfigImporterDefaultComponent internal constructor(private val project: Project) : ProjectComponent {

    override fun projectOpened() {
        val importConfig = ImportConfig.getInstance(project)
        if (importConfig.hasDefault) {
            val path = importConfig.defaultPath
            val file = File(path!!)
            if (file.exists()) {
                RunConfImporter.getInstance(project).importConfigs(path)
            }
        }
    }
}
