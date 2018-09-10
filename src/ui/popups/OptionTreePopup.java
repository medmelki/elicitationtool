package ui.popups;

import Main.MainGUI;
import org.apache.commons.lang.StringUtils;
import ui.jtree.DiscussionType;
import ui.jtree.RequirementAction;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class OptionTreePopup extends JPopupMenu {
    public OptionTreePopup(JTree tree) {
        JMenuItem itemAddPos = new JMenuItem("Add Positive");
        JMenuItem itemAddNeg = new JMenuItem("Add Negative");
        JMenuItem itemAddOriginator = new JMenuItem("Add Originator");
        JMenuItem itemAddPerspective = new JMenuItem("Add Perspective");
        JMenuItem itemAddPriority = new JMenuItem("Add Priority");
        JMenuItem itemAddConsequence = new JMenuItem("Add Consequence Question");
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

        itemAddOriginator.addActionListener(ae -> {
            String value = JOptionPane.showInputDialog("Insert Originator: ");
            if (StringUtils.isEmpty(value))
                return;
            DefaultMutableTreeNode selectedNode = MainGUI.selectedNode;

            RequirementAction originatorAction = new RequirementAction("Originator: " + value,
                    new ImageIcon("resources\\originator.png"), DiscussionType.ORIGINATOR);
            DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(originatorAction);
            DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
            model.insertNodeInto(optionNode, selectedNode, selectedNode.getChildCount());
            model.reload();

            MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());

        });

        itemAddPerspective.addActionListener(ae -> {
            String value = JOptionPane.showInputDialog("Insert Perspective: ");
            if (StringUtils.isEmpty(value))
                return;
            DefaultMutableTreeNode selectedNode = MainGUI.selectedNode;

            RequirementAction perspectiveAction = new RequirementAction("Perspective: " + value,
                    new ImageIcon("resources\\perspective.png"), DiscussionType.PERSPECTIVE);
            DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(perspectiveAction);
            DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
            model.insertNodeInto(optionNode, selectedNode, selectedNode.getChildCount());
            model.reload();

            MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());

        });

        itemAddPriority.addActionListener(ae -> {
            DefaultMutableTreeNode selectedNode = MainGUI.selectedNode;

            int score = 0;
            Enumeration e = selectedNode.children();
            while (e.hasMoreElements()) {
                DiscussionType type = ((RequirementAction) ((DefaultMutableTreeNode) e.nextElement())
                        .getUserObject()).getDiscussionType();
                if (type.equals(DiscussionType.POSITIVE_CRITERIA)) {
                    score++;
                }
                if (type.equals(DiscussionType.NEGATIVE_CRITERIA)) {
                    score--;
                }
            }

            RequirementAction priorityAction = new RequirementAction("Priority: " + score,
                    new ImageIcon("resources\\priority.png"), DiscussionType.PRIORITY);
            DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(priorityAction);
            DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
            model.insertNodeInto(optionNode, selectedNode, selectedNode.getChildCount());
            model.reload();

            MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());

        });

        itemAddConsequence.addActionListener(ae -> {
            String value = JOptionPane.showInputDialog("Insert Consequence question : ");
            if (StringUtils.isEmpty(value))
                return;
            DefaultMutableTreeNode selectedNode = MainGUI.selectedNode;

            RequirementAction consequenceAction = new RequirementAction("Consequence question : " + value,
                    null, DiscussionType.CONSEQUENCE);
            DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(consequenceAction);
            DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
            model.insertNodeInto(optionNode, selectedNode, selectedNode.getChildCount());
            model.reload();

            MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());

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
        add(itemAddOriginator);
        add(new JSeparator());
        add(itemAddPerspective);
        add(new JSeparator());
        add(itemAddPriority);
        add(new JSeparator());
        add(itemAddConsequence);
        add(new JSeparator());
        add(itemDelete);
    }
}