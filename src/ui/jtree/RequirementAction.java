package ui.jtree;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class RequirementAction implements Serializable {
	private String name;
	private boolean isChecked = false;
	private String flagIcon;
	private String agreedRequirement = "";
	private ImageIcon icon;
	private DiscussionType discussionType;

	@Override
	public String toString() {
		return "RequirementAction [name=" + name + ", isChecked=" + isChecked + ", flagIcon=" + flagIcon + ", icon="
				+ icon + ", discussionType=" + discussionType + "]";
	}

	public RequirementAction(String name, ImageIcon icon, DiscussionType type) {
		this.name = name;
		this.icon = icon;
		this.discussionType = type;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlagIcon() {
		return flagIcon;
	}

	public void setFlagIcon(String flagIcon) {
		this.flagIcon = flagIcon;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public DiscussionType getDiscussionType() {
		return discussionType;
	}

	public void setDiscussionType(DiscussionType discussionType) {
		this.discussionType = discussionType;
	}

	public String getAgreedRequirement() {
		return agreedRequirement;
	}

	public void setAgreedRequirement(String agreedRequirement) {
		this.agreedRequirement = agreedRequirement;
	}
	
	

}
