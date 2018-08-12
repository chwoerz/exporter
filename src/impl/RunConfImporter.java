package impl;

import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface RunConfImporter {
	static RunConfImporter getInstance(@NotNull Project project) {
		return ServiceManager.getService(project, RunConfImporter.class);
	}


	void importConfigs(List<RunnerAndConfigurationSettings> settingsList);

	void importConfigs(String importPath);

	List<RunnerAndConfigurationSettings> retrieveNewConfigs(String importPath);
}
