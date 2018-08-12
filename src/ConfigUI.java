import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import impl.RunConfImporter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

final class ConfigUI {

	static JPanel createPanel(Project project, ImportConfig importConfig) {
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
		JTextField defaultPathField = new JTextField();
		defaultPathField.setEditable(false);
		rootPanel.add(defaultPathField, new GridConstraints(1, 0, 2, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		JButton openButton = new JButton();
		openButton.setText("Choose default");
		rootPanel.add(openButton, new GridConstraints(1, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		rootPanel.add(spacer1, new GridConstraints(3, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		JCheckBox hasDefaultPath = new JCheckBox();
		hasDefaultPath.setText("Set a folder to automatically pull the run configrations from at project start");
		rootPanel.add(hasDefaultPath, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		JButton importButton = new JButton();
		importButton.setText("Import");
		rootPanel.add(importButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer2 = new Spacer();
		rootPanel.add(spacer2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

		openButton.setEnabled(importConfig.hasDefault());
		hasDefaultPath.setSelected(importConfig.hasDefault());
		defaultPathField.setText(importConfig.getDefaultPath());
		File dir = importConfig.getDefaultPath() == null ? null : new File(importConfig.getDefaultPath());
		importButton.setEnabled(dir != null && importConfig.hasDefault());

		hasDefaultPath.addActionListener(event -> {
			AbstractButton abstractButton = (AbstractButton) event.getSource();
			boolean selected = abstractButton.getModel().isSelected();
			openButton.setEnabled(selected);
			importConfig.setHasDefault(selected);
		});
		openButton.addActionListener(event -> {
			FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
			descriptor.setDescription("Folder to import configurations from");
			FileChooser.chooseFiles(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, null, virtualFiles -> {
				importConfig.setDefaultPath(virtualFiles.get(0).getPath());
				defaultPathField.setText(importConfig.getDefaultPath());
				importButton.setEnabled(true);
			});
		});

		importButton.addActionListener(event -> RunConfImporter.getInstance(project).importConfigs(defaultPathField.getText()));
		return rootPanel;
	}


}
