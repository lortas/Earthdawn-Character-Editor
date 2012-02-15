package de.earthdawn.ui2.tree;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import de.earthdawn.CharacterContainer;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.COINSType;
import de.earthdawn.data.DEFENSEABILITYType;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.MAGICITEMType;
import de.earthdawn.data.TALENTABILITYType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;
import de.earthdawn.data.WEAPONType;
import de.earthdawn.data.WOUNDType;
import de.earthdawn.data.YesnoType;

public class ItemTreeCellRenderer implements TreeCellRenderer {
	private HashMap<String, ImageIcon> treeIcons;
	private int intWidth = 30;
	private int intHeight = 30;

	public ItemTreeCellRenderer() {
		super();
		System.out.println("Init");
		File icondir = new File("icons");

		treeIcons = new HashMap<String, ImageIcon>();
		for(File file : icondir.listFiles()) {
			String strFilename=file.getName();
			if( strFilename.endsWith(".png") ) {
				try {
					ImageIcon orgIcon = new ImageIcon(file.getCanonicalPath());
					treeIcons.put(stripExtension(strFilename.toUpperCase()), scale(orgIcon.getImage()));
				} catch (IOException e) {
					// Viel kann man nicht tun. Wenn nicht geht, dann geht es halt nicht
					System.err.println(e.getMessage());
				}
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

		// Coins
		if( value == "Purse" ) {
			label.setText("Purse");
			label.setIcon((ImageIcon)treeIcons.get("PURSE"));
		}

		// ThreadRank
		if(value instanceof THREADRANKType){
			THREADRANKType rank = (THREADRANKType)value;
			THREADITEMType item = (THREADITEMType)((ItemTreeModel)tree.getModel()).getParent(value);
			int rankindex =  (item.getTHREADRANK().indexOf(rank) +1);
			label.setText("Rank " + rankindex + ": " + rank.getEffect()+" - "+rank.getKeyknowledge());
			if(treeIcons.containsKey("RANK" + rankindex)){	
				label.setIcon((ImageIcon)treeIcons.get("RANK" + rankindex));
			} else {
				label.setIcon((ImageIcon)treeIcons.get("RANKN"));
			}
		}

		if(value instanceof ITEMType) {
			ITEMType item = (ITEMType)value;
			label.setText(item.getName());
			label.setIcon((ImageIcon)treeIcons.get(item.getKind().toString()));
		}

		if (value instanceof WEAPONType) {
			WEAPONType weapon = (WEAPONType)value;
			label.setText(weapon.getName() +  " - (Damage: " +  weapon.getDamagestep() + ")" + (weapon.getUsed().equals(YesnoType.YES)?" - inuse":"") );
		}

		if (value instanceof ARMORType) {
			ARMORType armor = (ARMORType)value;
			label.setText(armor.getName() +  " - (" +  armor.getPhysicalarmor() + "/" + armor.getMysticarmor() + "/" + armor.getPenalty()  + ")" + (armor.getUsed().equals(YesnoType.YES)?" - inuse":"") );
		}

		if( value instanceof MAGICITEMType ) {
			MAGICITEMType magicitem = (MAGICITEMType)value;
			label.setText(magicitem.getName() +" - ("+ magicitem.getBlooddamage() +"/"+ magicitem.getDepatterningrate() +"/"+ magicitem.getEnchantingdifficultynumber() +")" + (magicitem.getUsed().equals(YesnoType.YES)?" - inuse":"") );
		}

		if( value instanceof THREADITEMType ) {
			THREADITEMType threaditem = (THREADITEMType)value;
			label.setText(threaditem.getName() +" - ("+ threaditem.getSpelldefense() +"/"+ threaditem.getMaxthreads() +"/"+ threaditem.getWeaventhreadrank() +")" + (threaditem.getUsed().equals(YesnoType.YES)?" - inuse":"") );
		}

		if( value instanceof TALENTABILITYType ) {
			TALENTABILITYType ta = (TALENTABILITYType)value;
			StringBuffer out = new StringBuffer();
			out.append(ta.getName());
			if( (ta.getLimitation()!=null) && !ta.getLimitation().isEmpty() ) {
				out.append(" - ");
				out.append(ta.getLimitation());
			}
			if( (ta.getPool()!=null) && !ta.getPool().isEmpty() ) {
				out.append(" - ");
				out.append(ta.getPool());
				
			}
			out.append(" : ");
			out.append(ta.getBonus());
			label.setText(out.toString());
			label.setIcon((ImageIcon)treeIcons.get("TALENTABILITY"));
		}

		if( value instanceof DEFENSEABILITYType ) {
			DEFENSEABILITYType da = (DEFENSEABILITYType)value;
			label.setText(da.getKind().value() +" defense : "+ da.getBonus());
		}

		if( value instanceof WOUNDType ) {
			WOUNDType w = (WOUNDType)value;
			label.setText("normal:"+w.getNormal()+", threshold:"+w.getThreshold()+", penalties:"+w.getPenalties()+", blood:"+w.getBlood());
		}

		if( value instanceof COINSType ) {
			COINSType coins = (COINSType)value;
			String text = coins.getName()+" (";
			text += "c:"+coins.getCopper()+" s:"+coins.getSilver()+" g:"+coins.getGold();
			if( coins.getEarth()>0 )      text += " e:"+coins.getEarth();
			if( coins.getWater()>0 )      text += " w:"+coins.getWater();
			if( coins.getAir()>0 )        text += " a:"+coins.getAir();
			if( coins.getFire()>0 )       text += " f:"+coins.getFire();
			if( coins.getOrichalcum()>0 ) text += " o:"+coins.getOrichalcum();
			if( coins.getGem50()>0)       text += " g50:"+coins.getGem50();
			if( coins.getGem100()>0)      text += " g100:"+coins.getGem100();
			if( coins.getGem200()>0)      text += " g200:"+coins.getGem200();
			if( coins.getGem500()>0)      text += " g500:"+coins.getGem500();
			if( coins.getGem1000()>0)     text += " g1000:"+coins.getGem1000();
			text +=")";
			label.setText(text);
		}
		if( value instanceof DisziplinAbilityNode ) {
			label.setIcon((ImageIcon)treeIcons.get("DISZIPLINABILITY"));
		}

		return label;
	}

	private String stripExtension(String pResourceName ) {
		final int i = pResourceName.lastIndexOf('.');
		if (i < 0) return pResourceName;
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
