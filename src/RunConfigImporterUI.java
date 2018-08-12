import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.ui.components.JBList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import impl.RunConfImporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RunConfigImporterUI extends AnAction {

	private DialogBuilder dialogBuilder;
	private List<RunnerAndConfigurationSettings> runnerAndConfigurationSettings = new ArrayList<>();

	@Override
	public void actionPerformed(AnActionEvent e) {
		dialogBuilder = new DialogBuilder().centerPanel(createExportDialog(e.getProject()));
		dialogBuilder.setTitle("Import All Run Configurations");
		dialogBuilder.addOkAction();
		dialogBuilder.addCancelAction();
		dialogBuilder.setOkActionEnabled(false);
		dialogBuilder.setOkOperation(() -> {
			impl.RunConfImporter.getInstance(e.getProject()).importConfigs(runnerAndConfigurationSettings);
			dialogBuilder.getDialogWrapper().close(1);
		});
		dialogBuilder.getOkAction().setText("Import");
		dialogBuilder.show();
	}

	private JComponent createExportDialog(Project project) {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
		JTextField importField = new JTextField();
		importField.setEditable(false);
		panel.add(importField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		JButton openButton = new JButton();
		openButton.setText("Choose...");
		panel.add(openButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		DefaultListModel<String> model = new DefaultListModel<>();
		JBList<String> importList = new JBList<>(model);
		importList.setVisible(false);
		panel.add(importList, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));

		ActionListener use_for_import = e -> {
			FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
			descriptor.setDescription("Folder to import configurations from");
			FileChooser.chooseFiles(descriptor, project, null, virtualFiles -> {
				String importPath = virtualFiles.get(0).getPath();
				importField.setText(importPath);
				List<RunnerAndConfigurationSettings> runnerAndConfigurationSettings = RunConfImporter.getInstance(project).retrieveNewConfigs(importPath);
				runnerAndConfigurationSettings.stream()
						.map(RunnerAndConfigurationSettings::getConfiguration)
						.forEach(setting -> model.addElement(setting.getName()));
				importList.setVisible(!runnerAndConfigurationSettings.isEmpty());

				dialogBuilder.setOkActionEnabled(true);
			});
		};
		openButton.addActionListener(use_for_import);
		return panel;
	}
}
