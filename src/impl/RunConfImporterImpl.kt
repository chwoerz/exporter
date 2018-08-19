package impl

import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.JDOMUtil
import org.apache.commons.io.FileUtils
import java.io.File
import kotlin.streams.toList

class RunConfImporterImpl(private val project: Project) : RunConfImporter {

    override fun importConfigs(settingsList: List<RunnerAndConfigurationSettings>) {
        val manager = RunManagerImpl.getInstanceImpl(project)
        settingsList.forEach { manager.addConfiguration(it) }
    }

    override fun importConfigs(importPath: String) {
        val manager = RunManagerImpl.getInstanceImpl(project)
        retrieveNewConfigs(importPath).forEach { manager.addConfiguration(it) }
    }

    override fun retrieveNewConfigs(importPath: String): List<RunnerAndConfigurationSettings> {
        val manager = RunManagerImpl.getInstanceImpl(project)
        val dir = File(importPath)
        val exts = arrayOf("xml")
        val files = FileUtils.listFiles(dir, exts, false)
        val errors = mutableListOf<File>()
        val successfullyReadFiles = files.stream()
                .map {
                    try {
                        val data = JDOMUtil.load(it)
                        val settings = RunnerAndConfigurationSettingsImpl(manager)
                        settings.readExternal(data.children[0], true)
                        settings
                    } catch (e: RuntimeException) {
                        errors.add(it)
                        e.printStackTrace()
                        null
                    }
                }
                .toList()
                .filterNotNull()
                .filter { isNew(manager.allConfigurationsList, it.configuration) }
        if (!errors.isEmpty()) {
            val stringBuilder = StringBuilder()
            errors.forEach { stringBuilder.append("\n" + it.name) }
            Messages.showMessageDialog(project, "Some files could not be imported, Check the Log for specific errors:" + stringBuilder.toString(),
                    "Errors during import", Messages.getInformationIcon())
        }
        return successfullyReadFiles
    }

    private fun isNew(configurations: List<RunConfiguration>, newConfig: RunConfiguration): Boolean {
        return configurations.stream()
                .noneMatch { config -> config.name == newConfig.name && newConfig.type == config.type }
    }
}
