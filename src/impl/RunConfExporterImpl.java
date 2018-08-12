package impl;

import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RunConfExporterImpl implements RunConfExporter {

	private Project project;

	public RunConfExporterImpl(Project project) {
		this.project = project;
	}

	@Override
	public void exportConfigs(String pathToFolder) {
		RunManagerImpl manager = RunManagerImpl.getInstanceImpl(project);
		File dir = new File(pathToFolder);
		List<RunConfiguration> allConfigurationsList = manager.getAllConfigurationsList();
		allConfigurationsList.stream()
				.map(RunConfExporterImpl::writeToXml)
				.forEach(config -> {
					try {
						Element component = new Element("component");
						component.setAttribute("name", "ProjectRunConfigurationManager");
						component.getChildren().add(config);
						JDOMUtil.write(component, new File(dir.getAbsolutePath() + "/" + config.getAttributeValue("name") + ".xml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

	}

	private static Element writeToXml(@NotNull RunConfiguration configuration) {
		RunnerAndConfigurationSettingsImpl settings = RunManagerImpl.getInstanceImpl(configuration.getProject()).getSettings(configuration);
		Element element = new Element("configuration");
		try {
			settings.writeExternal(element);
		} catch (WriteExternalException e) {
			e.printStackTrace();
		}
		return element;
	}
}
