import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
		name = "ImportConfig",
		storages = {
				@Storage("ImportConfig.xml")}
)
public class ImportConfig implements PersistentStateComponent<ImportConfig> {

	public String defaultPath;
	public boolean hasDefault;

	boolean hasDefault() {
		return hasDefault;
	}

	void setHasDefault(boolean hasDefault) {
		this.hasDefault = hasDefault;
	}

	String getDefaultPath() {
		return defaultPath;
	}

	void setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
	}

	@Nullable
	@Override
	public ImportConfig getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull ImportConfig importConfig) {
		XmlSerializerUtil.copyBean(importConfig, this);
	}

	@Nullable
	static ImportConfig getInstance(Project project) {
		return ServiceManager.getService(project, ImportConfig.class);
	}
}
