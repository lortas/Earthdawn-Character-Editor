package de.earthdawn.ui2.tree;

import java.awt.FlowLayout;

import javax.swing.JPanel;

public abstract class AbstractNodePanel<Type> extends JPanel {
	private static final long serialVersionUID = 1L;
	protected Type nodeObject;

	public AbstractNodePanel(Type node) {
		super();
		this.setNodeObject(node);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		this.setOpaque(false);
	}

	public abstract void updateObject();

	public void setNodeObject(Type nodeObject) {
		this.nodeObject = nodeObject;
	}

	public Type getNodeObject() {
		return nodeObject;
	}
}
