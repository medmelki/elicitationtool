package ui.jtree;

import javax.swing.tree.DefaultMutableTreeNode;

public class OptionScore {
	private DefaultMutableTreeNode optionNode;
	private DefaultMutableTreeNode questionNode;
	private String option;
	private Integer score = 0;

	public OptionScore(String option, Integer score) {
		this.option = option;
		this.score = score;
	}

	public OptionScore() {
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	



	public DefaultMutableTreeNode getOptionNode() {
		return optionNode;
	}

	public void setOptionNode(DefaultMutableTreeNode optionNode) {
		this.optionNode = optionNode;
	}

	
	public DefaultMutableTreeNode getQuestionNode() {
		return questionNode;
	}

	public void setQuestionNode(DefaultMutableTreeNode questionNode) {
		this.questionNode = questionNode;
	}

	@Override
	public String toString() {
		RequirementAction ra = (RequirementAction) optionNode.getUserObject();
		return "OptionScore [option=" + option + ", score=" + score + ", isChecked="+ ra.isChecked()+"]";
	}

}
