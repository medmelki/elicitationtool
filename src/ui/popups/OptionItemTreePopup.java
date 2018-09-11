package ui.popups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import Main.MainGUI;

public class OptionItemTreePopup extends JPopupMenu {

	public OptionItemTreePopup() {
		super();
		JMenuItem itemDelete = new JMenuItem("Delete");
		itemDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
				if (MainGUI.selectedNode.getChildCount() > 0) {
					JOptionPane.showMessageDialog(null, "Could not delete node with children");
					return;
				}

				model.removeNodeFromParent(MainGUI.selectedNode);
			}
		});

		// TODO Auto-generated constructor stub
		add(itemDelete);

	}

}