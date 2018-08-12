import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ImportConfigurable implements SearchableConfigurable {

	private final Project project;
	private final ImportConfig importConfig;

	public ImportConfigurable(@NotNull Project project) {
		this.project = project;
		this.importConfig = ImportConfig.getInstance(project);
	}

	@NotNull
	@Override
	public String getId() {
		return "preferences.RunConfConfigurable";
	}

	@Nls(capitalization = Nls.Capitalization.Title)
	@Override
	public String getDisplayName() {
		return "RunConf RunConfImporter";
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		return ConfigUI.createPanel(project, importConfig);
	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void apply() {

	}

	@Override
	public void disposeUIResources() {
		if (!importConfig.hasDefault) {
			importConfig.setDefaultPath(null);
		}
	}
}
