package de.earthdawn.ui2.tree;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractNodePanel<Type> extends JPanel {
	private static final long serialVersionUID = 1L;
	protected Type nodeObject;

	public AbstractNodePanel(Type node) {
		super();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				do_this_mouseClicked(arg0);
			}
		});
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
	protected void do_this_mouseClicked(MouseEvent arg0) {
	}
}
