package ui.events;

import Main.ConceptManager;
import Main.MainGUI;
import ui.jtree.RequirementAction;
import ui.popups.OptionItemTreePopup;
import ui.popups.OptionTreePopup;
import ui.popups.QuestionTreePopup;
import ui.popups.RootTreePopup;
import ui.popups.StatementTreePopup;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RequirementTreeMouseListener implements MouseListener {
    public DefaultMutableTreeNode selectedNode;
    public JTree tree;
    public JPopupMenu treePopup;

    public RequirementTreeMouseListener(JTree tree, DefaultMutableTreeNode selectedNode, JPopupMenu treePopup) {
        this.tree = tree;
        this.selectedNode = selectedNode;
        this.treePopup = treePopup;
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        if (arg0.getButton() == MouseEvent.BUTTON3) {

            if (MainGUI.selectedNode == null)
                return;
            if (!ConceptManager.isInitialized()) {

                JOptionPane.showMessageDialog(null,
                        "Please Choose Valid Ontology First from Project -> Create New Project");
                return;
            }

            // if(selectedNode.get)
            RequirementAction selectedAction = (RequirementAction) MainGUI.selectedNode.getUserObject();
            switch (selectedAction.getDiscussionType()) {
                case ROOT:
                    System.out.println("root selected");
                    treePopup = new RootTreePopup(tree);
                    break;

                case STATEMENT:
                    System.out.println("Statement selected");
                    treePopup = new StatementTreePopup(tree);
                    break;
                case QUESTION:
                    System.out.println("Question selected");
                    treePopup = new QuestionTreePopup(tree);
                    break;
                case OPTION:
                    treePopup = new OptionTreePopup(tree);
                    break;
                case POSITIVE_CRITERIA:
                    treePopup = new OptionItemTreePopup();
                    break;
                case ORIGINATOR:
                    treePopup = new OptionItemTreePopup();
                case PERSPECTIVE:
                    treePopup = new OptionItemTreePopup();
                case PRIORITY:
                    treePopup = new OptionItemTreePopup();
                case CONSEQUENCE:
                    treePopup = new OptionItemTreePopup();
            }
            treePopup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
        }

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        TreePath pathForLocation = tree.getPathForLocation(arg0.getPoint().x, arg0.getPoint().y);
        if (pathForLocation != null) {
            MainGUI.selectedNode = (DefaultMutableTreeNode) pathForLocation.getLastPathComponent();
            TreePath path = new TreePath(MainGUI.selectedNode.getPath());
            MainGUI.tree.getSelectionModel().setSelectionPath(path);
            // tree.getSelectionModel().select
            System.out.println("found: " + MainGUI.selectedNode);
            // RequirementAction r =(RequirementAction) selectedNode.getUserObject();

        } else {
            MainGUI.selectedNode = null;
            System.out.println("not found");
        }

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

}
