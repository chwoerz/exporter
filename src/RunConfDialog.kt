import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.components.JBList
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import com.intellij.util.ui.JBUI
import java.awt.Dimension
import javax.swing.DefaultListModel
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

class RunConfDialog(dialogBuilder: DialogBuilder, dialogTitle: String, okButtonText: String, emptyText: String) {
    val model = DefaultListModel<String>()
    val list: JBList<String> = JBList(model)
    val pathField = JTextField()
    val openButton = JButton()

    init {
        val panel1 = JPanel()
        panel1.layout = GridLayoutManager(2, 2, JBUI.emptyInsets(), -1, -1)
        pathField.isEditable = false
        panel1.add(pathField, GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, Dimension(150, -1), null, 0, false))
        openButton.text = "Choose..."
        panel1.add(openButton, GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        val spacer1 = Spacer()
        panel1.add(spacer1, GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false))
        list.isEnabled = false
        list.setEmptyText(emptyText)
        panel1.add(list, GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, Dimension(150, 50), null, 0, false))
        dialogBuilder.centerPanel(panel1)
        dialogBuilder.centerPanel.preferredSize = Dimension(600, 300)
        dialogBuilder.setTitle(dialogTitle)
        dialogBuilder.addOkAction()
        dialogBuilder.addCancelAction()
        dialogBuilder.setOkActionEnabled(false)
        dialogBuilder.setOkOperation {
            dialogBuilder.dialogWrapper.close(1)
        }
        dialogBuilder.okAction.setText(okButtonText)
    }
}
