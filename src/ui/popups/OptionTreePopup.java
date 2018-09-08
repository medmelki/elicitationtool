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

public class OptionTreePopup extends JPopupMenu {
	public OptionTreePopup(JTree tree) {
		JMenuItem itemAddPos = new JMenuItem("Add Positive");
		JMenuItem itemAddNeg = new JMenuItem("Add Negative");
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

		itemAddPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String statement = JOptionPane.showInputDialog("Insert Positive Criteria: ");
				if (statement == null || statement == "")
					return;
				DefaultMutableTreeNode selectedNode = MainGUI.selectedNode;

				RequirementAction PositiveAction = new RequirementAction("Positive: " + statement,
						new ImageIcon("resources\\positive.png"), DiscussionType.POSITIVE_CRITERIA);
				DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(PositiveAction);
				// selectedNode.add(optionNode);
				DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
				model.insertNodeInto(optionNode, selectedNode, selectedNode.getChildCount());
				model.reload();

				MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());

			}
		});

		itemAddNeg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				String statement = JOptionPane.showInputDialog("Insert Negative Criteria: ");
				if (statement == null || statement == "")
					return;

				DefaultMutableTreeNode selectedNode = MainGUI.selectedNode;

				RequirementAction NegativeAction = new RequirementAction("Negative: " + statement,
						new ImageIcon("resources\\negative.png"), DiscussionType.NEGATIVE_CRITERIA);
				DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(NegativeAction);
				// selectedNode.add(optionNode);
				DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
				model.insertNodeInto(optionNode, selectedNode, selectedNode.getChildCount());
				model.reload();
				MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());

			}
		});

		add(itemAddPos);
		add(new JSeparator());
		add(itemAddNeg);
		add(new JSeparator());

		add(itemDelete);
	}
}