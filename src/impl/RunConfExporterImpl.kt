package impl

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMUtil
import org.jdom.Element
import java.io.File

class RunConfExporterImpl(private val project: Project) : RunConfExporter {

    override fun exportConfigs(pathToFolder: String) {
        val manager = RunManager.getInstance(project)
        val dir = File(pathToFolder)
        val allConfigurationsList = manager.allConfigurationsList
        allConfigurationsList.stream()
                .map { writeToXml(it) }
                .forEach {
                    // fixme: check for errors
                    val component = Element("component")
                    component.setAttribute("name", "ProjectRunConfigurationManager")
                    component.children.add(it)
                    JDOMUtil.write(component, File(dir.absolutePath + "/" + it.getAttributeValue("name") + ".xml"))
                }

    }

    private fun writeToXml(configuration: RunConfiguration): Element {
        val settings = RunManagerImpl.getInstanceImpl(configuration.project).getSettings(configuration)
        val element = Element("configuration")
        settings!!.writeExternal(element)

        return element
    }
}
