package impl

import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project

interface RunConfImporter {

    fun importConfigs(settingsList: List<RunnerAndConfigurationSettings>)

    fun importConfigs(importPath: String)

    fun retrieveNewConfigs(importPath: String): List<RunnerAndConfigurationSettings>

    companion object {
        fun getInstance(project: Project): RunConfImporter {
            return ServiceManager.getService(project, RunConfImporter::class.java)
        }
    }
}
