package ui.popups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import Main.MainGUI;
import ui.jtree.DiscussionType;
import ui.jtree.RequirementAction;

public class QuestionTreePopup extends JPopupMenu {
	public QuestionTreePopup(JTree tree) {
		JMenuItem itemAdd = new JMenuItem("Add Option");
		JMenuItem itemDelete = new JMenuItem("Delete");
		itemDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
				if (MainGUI.selectedNode.getChildCount() > 0) {
					JOptionPane.showMessageDialog(null, "Could not delete node with children");
					return;
				}

				model.removeNodeFromParent(MainGUI.selectedNode);
			}
		});

		itemAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String statement = JOptionPane.showInputDialog("Insert Option: ");
				if (statement == null && statement == "")
					return;

				DefaultMutableTreeNode selectedNode = MainGUI.selectedNode;

				RequirementAction QuestionAction = new RequirementAction("Option: " + statement,
						new ImageIcon("resources\\clarification.png"), DiscussionType.OPTION);
				DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(QuestionAction);
				// selectedNode.add(optionNode);
				DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
				model.insertNodeInto(optionNode, selectedNode, selectedNode.getChildCount());

				MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());

			}
		});

		add(itemAdd);
		add(new JSeparator());
		add(itemDelete);

	}
}