package de.earthdawn.ui2.tree;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;
import de.earthdawn.data.WEAPONType;

public class ItemTreeCellRenderer implements TreeCellRenderer {
	private HashMap<String, ImageIcon> treeIcons;
	
	private int intWidth = 30;
	private int intHeight = 30;
	
	

	public ItemTreeCellRenderer() {
		super();
		System.out.println("Init");
		// Init icons
		File dir = new File("./icons");

		treeIcons = new HashMap<String, ImageIcon>();
		for(String strFilename : dir.list()) {
			if( strFilename.endsWith(".png") ) {
				ImageIcon orgIcon = new ImageIcon("./icons/" + strFilename);
				treeIcons.put(stripExtension(strFilename.toUpperCase()),  scale(orgIcon.getImage()));
			}
		}
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		JLabel label = new JLabel();
		label.setText(value.toString());
		CharacterContainer character = (CharacterContainer)tree.getModel().getRoot();
		
		// Items
		if(value == "Items"){
			label.setText("Items");	
			label.setIcon((ImageIcon)treeIcons.get("BACKPACK"));	
		}
		// Armor
		if(value == "Armor"){
			label.setText("Armor");	
			label.setIcon((ImageIcon)treeIcons.get("HELM"));
		}	
		
		// Weapons
		if(value == "Weapons"){
			label.setText("Weapons");	
			label.setIcon((ImageIcon)treeIcons.get("MELEE_WEAPON"));
		}	
		
		// Bloodcharms
		if(value == "Bloodcharms"){
			label.setText("Bloodcharms");	
			label.setIcon((ImageIcon)treeIcons.get("BLOOD_CHARM"));
		}			
		
		// ThreadItems
		if(value == "Thread Items"){
			label.setText("Thread Items");	
			if(expanded){
				label.setIcon((ImageIcon)treeIcons.get("CHEST_OPEN"));
			}
			else{
				label.setIcon((ImageIcon)treeIcons.get("CHEST_CLOSED"));
			}
		}
		
		// ThreadRank
		if(value instanceof THREADRANKType){
			THREADRANKType rank = (THREADRANKType)value;
			THREADITEMType item = (THREADITEMType)((ItemTreeModel)tree.getModel()).getParent(value);
			int rankindex =  (item.getTHREADRANK().indexOf(rank) +1);
			label.setText("Rank " + rankindex + ": " + rank.getEffect());	
			if(treeIcons.containsKey("RANK" + rankindex)){	
				label.setIcon((ImageIcon)treeIcons.get("RANK" + rankindex));
			}	
			else{
				label.setIcon((ImageIcon)treeIcons.get("RANKN"));
			}
		}
		

		
		if(value instanceof ITEMType){	
			ITEMType item = (ITEMType)value;
			label.setText(item.getName());
			label.setIcon((ImageIcon)treeIcons.get(item.getKind().toString()));
		}
		
		if (value instanceof WEAPONType){	
			WEAPONType weapon = (WEAPONType)value;
			label.setText(weapon.getName() +  " - (Damage: " +  weapon.getDamagestep() + ")" );
		}
		
		if (value instanceof ARMORType){	
			ARMORType armor = (ARMORType)value;
			label.setText(armor.getName() +  " - (" +  armor.getPhysicalarmor() + "/" + armor.getMysticarmor() + "/" + armor.getPenalty()  + ")" );
		}
		

		
		
		return label;
	}
	
	private String stripExtension(String pResourceName ) {
		final int i = pResourceName.lastIndexOf('.');
		if (i < 0) {
			return pResourceName;
		}
		final String withoutExtension = pResourceName.substring(0, i);
		return withoutExtension;
	}
	
	private ImageIcon scale(Image src) {

		int type = BufferedImage.TRANSLUCENT;
		BufferedImage dst = new BufferedImage(intWidth, intHeight, type);
		Graphics2D g2 = dst.createGraphics();
		g2.drawImage(src, 0, 0, intWidth, intHeight, null);
		g2.dispose();
		return new ImageIcon(dst);
	}


}
