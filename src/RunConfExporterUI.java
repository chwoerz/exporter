import com.intellij.execution.impl.RunManagerImpl;
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
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class RunConfExporterUI extends AnAction {

	private String exportPath;
	private DialogBuilder dialogBuilder;

	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getProject();
		assert project != null;
		dialogBuilder = new DialogBuilder().centerPanel(createExportDialog(project));
		dialogBuilder.getCenterPanel().setPreferredSize(new Dimension(600, 300));
		dialogBuilder.setTitle("Export All Run Configurations");
		dialogBuilder.addOkAction();
		dialogBuilder.addCancelAction();
		dialogBuilder.setOkActionEnabled(false);
		dialogBuilder.setOkOperation(() -> {
			impl.RunConfExporter.getInstance(project).exportConfigs(exportPath);
			dialogBuilder.getDialogWrapper().close(1);
		});
		dialogBuilder.getOkAction().setText("Export");
		dialogBuilder.show();
	}

	private JComponent createExportDialog(Project project) {
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(2, 2, JBUI.emptyInsets(), -1, -1));
		JTextField exportField = new JTextField();
		exportField.setEditable(false);
		panel1.add(exportField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		JButton openButton = new JButton();
		openButton.setText("Choose...");
		panel1.add(openButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel1.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		DefaultListModel<String> model = new DefaultListModel<>();
		JBList<String> exportList = new JBList<>(model);
		exportList.setEnabled(false);
		exportList.setEmptyText("No configurations found to export");
		panel1.add(exportList, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
		RunManagerImpl manager = RunManagerImpl.getInstanceImpl(project);
		manager.getAllConfigurationsList().forEach(config -> model.addElement(config.getType().getDisplayName() + " : " + config.getName()));
		openButton.addActionListener(e -> {
			FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
			descriptor.setDescription("Folder to export configurations to");
			FileChooser.chooseFiles(descriptor, project, null, virtualFiles -> {
				String exportPath = virtualFiles.get(0).getPath();
				exportField.setText(exportPath);
				this.exportPath = exportPath;
				dialogBuilder.setOkActionEnabled(true);
			});
		});
		return panel1;
	}
}
