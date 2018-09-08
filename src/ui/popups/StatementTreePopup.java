package ui.popups;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import Main.MainGUI;
import javassist.expr.Instanceof;
import ui.jtree.DiscussionType;
import ui.jtree.OptionScore;
import ui.jtree.RequirementAction;

public class StatementTreePopup extends JPopupMenu {
	public StatementTreePopup(JTree tree) {
		JMenuItem itemAdd = new JMenuItem("Add Question");
		JMenuItem finalize = new JMenuItem("Finalize Statement");
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
				String statement = JOptionPane.showInputDialog("Insert Question: ");
				if (statement == null || statement == "")
					return;

				DefaultMutableTreeNode selectedNode = MainGUI.selectedNode;

				RequirementAction QuestionAction = new RequirementAction("Question: " + statement,
						new ImageIcon("resources\\questionmark.png"), DiscussionType.QUESTION);
				DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(QuestionAction);
				// selectedNode.add(optionNode);
				DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
				model.insertNodeInto(optionNode, selectedNode, selectedNode.getChildCount());
				model.reload();
				MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());
			}
		});

		finalize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				TreeModel tm = MainGUI.tree.getModel();
				DefaultMutableTreeNode root = MainGUI.selectedNode;
				// JOptionPane.showMessageDialog(null, "this statment is marked as done");
				HashMap<String, List<OptionScore>> questionOptionScore = printNode(root);
				RequirementAction rootAction = (RequirementAction) root.getUserObject();
				// System.out.println(questionOptionScore);
				System.out.println(Collections.singletonList(questionOptionScore));

				JPanel panel = new JPanel();
				Object[] options1 = { "Done" };
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				// panel.setSize(new Dimension(100,100));
				// panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
//				panel.add(new JLabel("Make vote for each question and write the  option in full statement (complete requirement statement): " + rootAction.getName()));
				panel.add(new JLabel("Make vote for each question and write the  option in full statement (complete requirement statement): " ));
				int i = 0;
				boolean validVooting = false;
				for (String question : questionOptionScore.keySet()) {
					JLabel questionLabel = new JLabel(++i + ". " + question + " ?");
					// questionLabel.
					List<OptionScore> questionOptions = questionOptionScore.get(question);
					if (questionOptions.size() > 0) {
						panel.add(questionLabel);
					}
					JTextField agreedReq = new JTextField("");
					ButtonGroup  bg = new ButtonGroup ();
					for (OptionScore os : questionOptions) {
						validVooting = true;
//						JCheckBox option = new JCheckBox(os.getOption() + "(" + os.getScore() + ")");
//						JCheckBox option = new JCheckBox(os.getOption());
						JRadioButton option = new JRadioButton(os.getOption());
						bg.add(option);
//						JTextField agreedReq = new JTextField("agreed req: " + os.getOption());

						// agreedReq.setText("");
						// option.set
						// option.addc
						option.addItemListener(new ItemListener() {

							@Override
							public void itemStateChanged(ItemEvent arg0) {
								// TODO Auto-generated method stub
								System.out.println("agreedReq.getText" + agreedReq.getText());
								DefaultMutableTreeNode node = os.getOptionNode();
								RequirementAction ra = (RequirementAction) node.getUserObject();
								System.out.println("is selected: " + option.isSelected());
								ra.setChecked(option.isSelected());
								agreedReq.putClientProperty("node", ra);

							}
						});
						panel.add(option);
					
					}
					panel.add(agreedReq);
				}
				JOptionPane pane = new JOptionPane("Options Voting", JOptionPane.PLAIN_MESSAGE,
						JOptionPane.YES_NO_CANCEL_OPTION, null, options1, null);
				JDialog dialog = pane.createDialog(panel, "voting");
				if (validVooting) {
					int result = pane.showOptionDialog(null, panel, "Voting", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options1, null);
					System.out.println(result);
					if (result == 0) {
						System.out.println("final: " + questionOptionScore);

						int ind = 0;
						for (Component c : panel.getComponents()) {
							// System.out.println(c);
							if (c instanceof JTextField) {
								JTextField j = (JTextField) c;
								System.out.println("agreed: " + j.getText());
								RequirementAction ra = (RequirementAction) j.getClientProperty("node");
								System.out.println("loaded !");
								ra.setAgreedRequirement(j.getText());
							}
						}
						rootAction.setChecked(true);
						DefaultTreeModel t = (DefaultTreeModel) MainGUI.tree.getModel();
						t.reload();

						rootAction.setName(rootAction.getName());
					}

				} else {
					JOptionPane.showMessageDialog(null, "no option to choose");
				}

			}
		});

		add(itemAdd);
		add(new JSeparator());
		add(finalize);
		add(new JSeparator());
		add(itemDelete);
	}

	public static List<DefaultMutableTreeNode> allQuestions = new ArrayList<>();

	public static void getAllQuestions(DefaultMutableTreeNode root) {
		int childCount = root.getChildCount();
		if (childCount == 0)
			return;
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) root.getChildAt(i);
			RequirementAction selectedAction = (RequirementAction) childNode.getUserObject();
			if (selectedAction.getDiscussionType().equals(DiscussionType.QUESTION)) {
				allQuestions.add(childNode);
			}
			getAllQuestions(childNode);
			// else if (selectedAction.getDiscussionType().equals(DiscussionType.OPTION)) {
			// getAllQuestions(childNode);
			// }
		}
	}

	public static HashMap<String, List<OptionScore>> printNode(DefaultMutableTreeNode node) {
		HashMap<String, List<OptionScore>> questionOptionScore = new HashMap<>();
		int childCount = node.getChildCount();
		// questions
		// List<DefaultMutableTreeNode> allQuestion = new
		// ArrayList<DefaultMutableTreeNode>();
		allQuestions.clear();
		getAllQuestions(node);
		childCount = allQuestions.size();
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode childNode = allQuestions.get(i);
			// DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)
			// node.getChildAt(i);
			RequirementAction selectedAction = (RequirementAction) childNode.getUserObject();

			String question = selectedAction.getName();
			if (questionOptionScore.get(question) == null)
				questionOptionScore.put(question, new ArrayList<OptionScore>());

			int grandChildCount = childNode.getChildCount();
			// options
			for (int j = 0; j < grandChildCount; j++) {
				DefaultMutableTreeNode grandChildNode = (DefaultMutableTreeNode) childNode.getChildAt(j);
				RequirementAction grandChildAction = (RequirementAction) grandChildNode.getUserObject();

				int criteriaChildCount = grandChildNode.getChildCount();
				int score = 0;
				// positive negative
				for (int k = 0; k < criteriaChildCount; k++) {
					DefaultMutableTreeNode criteriaNode = (DefaultMutableTreeNode) grandChildNode.getChildAt(k);
					RequirementAction crtieriaAction = (RequirementAction) criteriaNode.getUserObject();
					if (crtieriaAction.getDiscussionType().equals(DiscussionType.POSITIVE_CRITERIA))
						score++;
					else if (crtieriaAction.getDiscussionType().equals(DiscussionType.NEGATIVE_CRITERIA))
						score--;
					else if (crtieriaAction.getDiscussionType().equals(DiscussionType.QUESTION)) {

					}
				}
				OptionScore opScore = new OptionScore(grandChildAction.getName(), score);
				opScore.setOptionNode(grandChildNode);
				opScore.setQuestionNode(childNode);
				List<OptionScore> opList = questionOptionScore.get(question);
				opList.add(opScore);

				questionOptionScore.put(question, opList);

			}
		}
		return questionOptionScore;
	}
}