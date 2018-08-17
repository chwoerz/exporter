package impl

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project

interface RunConfExporter {

    fun exportConfigs(pathToFolder: String)

    companion object {
        fun getInstance(project: Project): RunConfExporter {
            return ServiceManager.getService(project, RunConfExporter::class.java)
        }
    }
}
