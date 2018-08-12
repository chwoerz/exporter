import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import impl.RunConfImporter;

import java.io.File;

public class RunConfigImporterDefaultComponent implements ProjectComponent {

	private Project project;

	RunConfigImporterDefaultComponent(Project project) {
		this.project = project;
	}

	@Override
	public void projectOpened() {
		ImportConfig importConfig = ImportConfig.getInstance(project);
		assert importConfig != null;
		if (importConfig.hasDefault()) {
			String path = importConfig.getDefaultPath();
			File file = new File(path);
			if (file.exists()) {
				RunConfImporter.getInstance(project).importConfigs(path);
			}
		}
	}
}
