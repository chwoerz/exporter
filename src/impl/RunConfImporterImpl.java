package impl;

import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMUtil;
import org.apache.commons.io.FileUtils;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RunConfImporterImpl implements RunConfImporter {
	private Project project;

	public RunConfImporterImpl(Project project) {
		this.project = project;
	}

	@Override
	public void importConfigs(List<RunnerAndConfigurationSettings> settingsList) {
		RunManagerImpl manager = RunManagerImpl.getInstanceImpl(project);
		settingsList.forEach(manager::addConfiguration);
	}

	@Override
	public void importConfigs(String importPath) {
		RunManagerImpl manager = RunManagerImpl.getInstanceImpl(project);
		retrieveNewConfigs(importPath).forEach(manager::addConfiguration);
	}

	@Override
	public List<RunnerAndConfigurationSettings> retrieveNewConfigs(String importPath) {
		RunManagerImpl manager = RunManagerImpl.getInstanceImpl(project);
		File dir = new File(importPath);
		String[] exts = new String[]{"xml"};
		Collection<File> files = FileUtils.listFiles(dir, exts, false);
		return files.stream()
				.map(file -> {
					Element data = null;
					try {
						data = JDOMUtil.load(file);
					} catch (JDOMException | IOException e) {
						e.printStackTrace();
					}
					RunnerAndConfigurationSettingsImpl settings = new RunnerAndConfigurationSettingsImpl(manager);
					settings.readExternal(data.getChildren().get(0), true);
					return settings;
				})
				.filter(setting -> isNew(manager.getAllConfigurationsList(), setting.getConfiguration()))
				.collect(Collectors.toList());
	}

	private boolean isNew(List<RunConfiguration> configurations, RunConfiguration newConfig) {
		return configurations.stream()
				.noneMatch(config -> config.getName().equals(newConfig.getName()) && newConfig.getType().equals(config.getType()));
	}
}
