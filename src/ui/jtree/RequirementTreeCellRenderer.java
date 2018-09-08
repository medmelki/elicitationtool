package ui.jtree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class RequirementTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer, Serializable {
	private JLabel label;

	public RequirementTreeCellRenderer() {
		super();
		label = new JLabel();
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		// return super.getTreeCellRendererComponent(tree, value, selected, expanded,
		// leaf, row, hasFocus);
		label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		// this.setTextSelectionColor(Color.red);
		// this.setBackgroundSelectionColor(Color.blue);
		// this.setBorderSelectionColor(Color.black);
		Object o = ((DefaultMutableTreeNode) value).getUserObject();
		if (o instanceof RequirementAction) {
			RequirementAction action = (RequirementAction) o;
			// URL imageUrl = getClass().getResource(country.getFlagIcon());
			// if (imageUrl != null) {
			// label.setIcon(new ImageIcon(imageUrl));
			// }
			label.setFocusable(true);
			// label
			label.setFont(new Font("Calibri", Font.PLAIN, 20));
			label.setIcon(action.getIcon());
			label.setText(action.getName() + (action.isChecked() == true ? " [Checked]" : ""));
		} else {
			label.setIcon(null);
			label.setText("" + value);
		}
		return label;
	}
}
