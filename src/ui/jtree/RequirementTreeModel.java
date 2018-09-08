package ui.jtree;

import java.io.Serializable;

import javax.swing.tree.TreeModel;

public class RequirementTreeModel implements Serializable {
	private TreeModel treeModel;
	private String currentOntologyPath;

	public TreeModel getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(TreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public String getCurrentOntologyPath() {
		return currentOntologyPath;
	}

	public void setCurrentOntologyPath(String currentOntologyPath) {
		this.currentOntologyPath = currentOntologyPath;
	}

	public RequirementTreeModel(TreeModel treeModel, String currentOntologyPath) {
		super();
		this.treeModel = treeModel;
		this.currentOntologyPath = currentOntologyPath;
	}

}
