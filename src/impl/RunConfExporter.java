package impl;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface RunConfExporter {
	static RunConfExporter getInstance(@NotNull Project project) {
		return ServiceManager.getService(project, RunConfExporter.class);
	}

	void exportConfigs(String pathToFolder);
}
