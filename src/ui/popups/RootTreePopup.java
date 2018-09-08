package ui.popups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import Main.ConceptManager;
import Main.MainGUI;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import ui.jtree.DiscussionType;
import ui.jtree.RequirementAction;

public class RootTreePopup extends JPopupMenu {
	public RootTreePopup(JTree tree) {

		JMenuItem itemAdd = new JMenuItem("Add Statement");
		// JMenuItem finalize = new JMenuItem("Finalize Statement");

		itemAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DefaultMutableTreeNode selectedNode = MainGUI.selectedNode;

				RequirementAction selectedAction = (RequirementAction) selectedNode.getUserObject();

				System.out.println("selected: " + selectedNode);
				System.out.println("selected: " + selectedAction.getDiscussionType());
				String NodeName = "";
				// TreeModel treeModel = MainGUI.tree.getModel();
				switch (selectedAction.getDiscussionType()) {
				case ROOT:

					System.out.println("is root");
					// String statement = JOptionPane.showInputDialog("Insert Statement: ");
					Object[] options1 = { "Analysis", "Cancel" };
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					// panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
					JLabel mainlabel = new JLabel("Enter the statement of need");
					mainlabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 7, 3));
					panel.add(mainlabel);
					JTextField statementTextField = new JTextField(50);
					panel.add(statementTextField);

					JPanel matchedConceptsPanel = new JPanel();
					boolean confirmed = false;
					String statementOfNeed = "";
					while (!confirmed) {
						matchedConceptsPanel = new JPanel();
						matchedConceptsPanel.setLayout(new BoxLayout(matchedConceptsPanel, BoxLayout.Y_AXIS));

						int result = JOptionPane.showOptionDialog(null, panel, "Analyze",
								JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options1, null);
						System.out.println(result);

						if (result == 0) {
							statementOfNeed = statementTextField.getText();
							Set<OWLNamedClass> matchedConecepts = ConceptManager.extractConcepts(statementOfNeed);
							System.out.println("Matched Concept are: ");
							String concepts = "";

							for (OWLNamedClass concept : matchedConecepts) {
								Collection rdfr = concept.getRDFProperties();
								List<RDFResource> list = new ArrayList(rdfr);
								String description = "";
								for (RDFResource rs : list) {
									RDFProperty rp = (RDFProperty) rs;
									String propName = rp.getBrowserText();
									System.out.println("prob: " + propName);
									if (propName.toLowerCase().contains("description")) {
										description = concept.getPropertyValue((RDFProperty) rs).toString();
										System.out.println("desc: " + description);
										System.out.println(concept.getPropertyValue((RDFProperty) rs));
									}

								}

								JLabel label = new JLabel(
										"<html> <b>" + concept.getBrowserText() + "</b> : " + description + "</html>");

								label.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));
								matchedConceptsPanel.add(label);
								// panel.setla
								concepts += concept.getBrowserText() + ",";
								System.out.println("assoc: " + concept.getAssociatedProperties());
								System.out.println("assoc: " + concept.getNestedBrowserText());
								System.out.println("assoc: " + concept.getLabels());
								System.out.println("assoc: " + concept.getRDFTypes());
								System.out.println("assoc: " + concept.getRDFProperties());
								// OWLObjectProperty r = new
							}
							JLabel label = new JLabel();

							Set<OWLNamedClass> currentMatched = new HashSet<OWLNamedClass>();
							currentMatched.addAll(matchedConecepts);
//							 if (MainGUI.isAllMatched(currentMatched) == true) {

							if (currentMatched.size() > 0) {
								// label.setText(label.getText() + "all Matched");
								label.setText(
										"<html> <b style='color:green'> (your statement is correct and matched with the concepts of the project domain)  </b> ");

							} else {
								label.setText(
										"<html> <b style='color:red'> (some concept(s) in the statement is/are not matched with the concept(s) of the project domain)  </b: ");

							}
							label.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));
							matchedConceptsPanel.add(label);

							System.out.println("matched concepts: " + concepts);
							panel.add(matchedConceptsPanel);
							Object[] options2 = { "Start Discussion", "Rephrase the statement of need" };
							result = JOptionPane.showOptionDialog(null, panel, "Start Discussion",
									JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options2, null);
							if (result == 0) {
								confirmed = true;
							} else if (result == 1) {
								confirmed = false;
								panel.remove(matchedConceptsPanel);
							} else if (result == -1) {
								return;
							}

						} else if (result == 1 || result == -1)
							return;
					}

					// NodeName = JOptionPane.showInputDialog("Insert Statement: ");
					NodeName = statementOfNeed;
					RequirementAction statementAction = new RequirementAction("Statement: " + NodeName,
							new ImageIcon("resources\\statement.png"), DiscussionType.STATEMENT);

					DefaultMutableTreeNode statementNode = new DefaultMutableTreeNode(statementAction);
					// selectedNode.add
					// selectedNode.add(statementNode);
					DefaultTreeModel model = (DefaultTreeModel) MainGUI.tree.getModel();
					model.insertNodeInto(statementNode, selectedNode, selectedNode.getChildCount());
					model.reload();
					// selectedNode.add
					// selectedNode.add(statementNode);
					break;
				// case STATEMENT:
				// System.out.println("is statement");
				// NodeName = JOptionPane.showInputDialog("Insert Option: ");
				// System.out.println("Added");
				// // System.out.println(statement);
				// RequirementAction OptionAction = new RequirementAction("Option: " + NodeName,
				// new ImageIcon("resources\\clarification.png"), DiscussionType.OPTION);
				// DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(OptionAction);
				// selectedNode.add(optionNode);
				// break;
				}
				MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());
			}
		});

		// finalize.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// // TODO Auto-generated method stub
		// TreeModel tm = MainGUI.tree.getModel();
		// DefaultMutableTreeNode root = MainGUI.selectedNode;
		// JOptionPane.showMessageDialog(null, "this statment is marked as done");
		// HashMap<String, List<OptionScore>> questionOptionScore = printNode(root);
		// System.out.println(questionOptionScore);
		//
		// }
		// });
		add(itemAdd);
		// add(new JSeparator());
		// add(finalize);
	}

	// class OptionScore {
	// private String option;
	// private Integer score = 0;
	//
	// public OptionScore(String option, Integer score) {
	// this.option = option;
	// this.score = score;
	// }
	//
	// public OptionScore() {
	// }
	//
	// public String getOption() {
	// return option;
	// }
	//
	// public void setOption(String option) {
	// this.option = option;
	// }
	//
	// public Integer getScore() {
	// return score;
	// }
	//
	// public void setScore(Integer score) {
	// this.score = score;
	// }
	//
	// }
	//
	// public HashMap<String, List<OptionScore>> printNode(DefaultMutableTreeNode
	// node) {
	// HashMap<String, List<OptionScore>> questionOptionScore = new HashMap<>();
	// int childCount = node.getChildCount();
	// // questions
	// for (int i = 0; i < childCount; i++) {
	// DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)
	// node.getChildAt(i);
	// RequirementAction selectedAction = (RequirementAction)
	// childNode.getUserObject();
	//
	// String question = selectedAction.getName();
	// if (questionOptionScore.get(question) == null)
	// questionOptionScore.put(question, new ArrayList<OptionScore>());
	//
	// int grandChildCount = childNode.getChildCount();
	// // options
	// for (int j = 0; j < grandChildCount; j++) {
	// DefaultMutableTreeNode grandChildNode = (DefaultMutableTreeNode)
	// childNode.getChildAt(j);
	// RequirementAction grandChildAction = (RequirementAction)
	// grandChildNode.getUserObject();
	//
	// int criteriaChildCount = grandChildNode.getChildCount();
	// int score = 0;
	// // positive negative
	// for (int k = 0; k < criteriaChildCount; k++) {
	// DefaultMutableTreeNode criteriaNode = (DefaultMutableTreeNode)
	// grandChildNode.getChildAt(k);
	// RequirementAction crtieriaAction = (RequirementAction)
	// criteriaNode.getUserObject();
	// if
	// (crtieriaAction.getDiscussionType().equals(DiscussionType.POSITIVE_CRITERIA))
	// score++;
	// else if
	// (crtieriaAction.getDiscussionType().equals(DiscussionType.NEGATIVE_CRITERIA))
	// score--;
	// }
	// OptionScore opScore = new OptionScore(grandChildAction.getName(), score);
	// List<OptionScore> opList = questionOptionScore.get(question);
	// opList.add(opScore);
	// questionOptionScore.put(question, opList);
	//
	// }
	// }
	// return questionOptionScore;
	// }
}