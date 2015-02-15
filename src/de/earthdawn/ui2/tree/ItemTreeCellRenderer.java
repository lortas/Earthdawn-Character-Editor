package de.earthdawn.ui2.tree;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.COINSType;
import de.earthdawn.data.DEFENSEABILITYType;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.MAGICITEMType;
import de.earthdawn.data.PATTERNITEMType;
import de.earthdawn.data.TALENTABILITYType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;
import de.earthdawn.data.WEAPONType;
import de.earthdawn.data.WOUNDType;
import de.earthdawn.data.YesnoType;
import de.earthdawn.ui2.EDInventoryRootNodeType;

public class ItemTreeCellRenderer implements TreeCellRenderer {
	private Map<String, ImageIcon> treeIcons;
	private int intWidth = 30;
	private int intHeight = 30;

	public ItemTreeCellRenderer() {
		super();
		File icondir = new File("icons");
		treeIcons = new TreeMap<String, ImageIcon>();
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

		// Items
		if(value == EDInventoryRootNodeType.ITEMS){
			label.setText(EDInventoryRootNodeType.ITEMS.value());
			label.setIcon((ImageIcon)treeIcons.get("BACKPACK"));
		}

		// Armor
		if(value == EDInventoryRootNodeType.ARMOR){
			label.setText(EDInventoryRootNodeType.ARMOR.value());
			label.setIcon((ImageIcon)treeIcons.get("HELM"));
		}

		// Shield
		if(value == EDInventoryRootNodeType.SHIELD){
			label.setText("Shield");
			label.setIcon((ImageIcon)treeIcons.get("SHIELD"));
		}

		// Weapons
		if(value == EDInventoryRootNodeType.WEAPONS){
			label.setText(EDInventoryRootNodeType.WEAPONS.value());
			label.setIcon((ImageIcon)treeIcons.get("MELEE_WEAPON"));
		}

		//Common Magic Items
		if(value == EDInventoryRootNodeType.COMMONMAGICITEMS){
			label.setText(EDInventoryRootNodeType.COMMONMAGICITEMS.value());	
			label.setIcon((ImageIcon)treeIcons.get("MAGIC_ITEM"));
		}

		// Bloodcharms
		if(value == EDInventoryRootNodeType.BLOODCHARMS){
			label.setText(EDInventoryRootNodeType.BLOODCHARMS.value());	
			label.setIcon((ImageIcon)treeIcons.get("BLOOD_CHARM"));
		}

		// ThreadItems
		if(value == EDInventoryRootNodeType.THREADITEMS){
			label.setText(EDInventoryRootNodeType.THREADITEMS.value());	
			if(expanded){
				label.setIcon((ImageIcon)treeIcons.get("CHEST_OPEN"));
			}
			else{
				label.setIcon((ImageIcon)treeIcons.get("CHEST_CLOSED"));
			}
		}

		//Pattern Items
		if(value == EDInventoryRootNodeType.PATTERNITEMS){
			label.setText(EDInventoryRootNodeType.PATTERNITEMS.value());	
			label.setIcon((ImageIcon)treeIcons.get("MAGIC_ITEM"));
		}

		// Coins
		if( value == EDInventoryRootNodeType.PURSE ) {
			label.setText(EDInventoryRootNodeType.PURSE.value());
			label.setIcon((ImageIcon)treeIcons.get("PURSE"));
		}

		// ThreadRank
		if(value instanceof THREADRANKType){
			THREADRANKType rank = (THREADRANKType)value;
			StringBuffer labeltext = new StringBuffer(rank.getEffect());
			if( ! rank.getKeyknowledge().isEmpty() ) {
				labeltext.append(" -- ");
				labeltext.append(rank.getKeyknowledge());
			}
			if( ! rank.getDeed().isEmpty() ) {
				labeltext.append(" -- ");
				labeltext.append(rank.getDeed());
			}
			TreeModel treemodel = tree.getModel();
			THREADITEMType item = null;
			if( treemodel instanceof ItemTreeModel ) item = (THREADITEMType)((ItemTreeModel)treemodel).getParent(value);
			if( treemodel instanceof ItemStoreTreeModel ) item = (THREADITEMType)((ItemStoreTreeModel)treemodel).getParent(value);
			if( item != null ) {
				int rankindex =  (item.getTHREADRANK().indexOf(rank) +1);
				label.setText("Rank "+rankindex+": "+labeltext.toString());
				if(treeIcons.containsKey("RANK" + rankindex)){	
					label.setIcon((ImageIcon)treeIcons.get("RANK" + rankindex));
				} else {
					label.setIcon((ImageIcon)treeIcons.get("RANKN"));
				}
			} else {
				label.setText("Rank n: " + labeltext.toString());
				label.setIcon((ImageIcon)treeIcons.get("RANKN"));
			}
		}

		if( value instanceof ITEMType ) renderItems((ITEMType)value, label);

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
			label.setIcon((ImageIcon)treeIcons.get("DEFENSE"));
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

		if( value instanceof StringNode ) {
			switch( ((StringNode)value).getType() ) {
			case ABILITY: label.setIcon((ImageIcon)treeIcons.get("ABILITY")); break;
			case SPELL:   label.setIcon((ImageIcon)treeIcons.get("SPELL"));   break;
			}
		}

		return label;
	}

	public void renderItems(ITEMType item, JLabel label) {
		label.setIcon((ImageIcon)treeIcons.get(item.getKind().toString()));
		StringBuffer text=new StringBuffer(item.getName());

		if (item instanceof WEAPONType) {
			WEAPONType weapon = (WEAPONType)item;
			text.append(", Damage: ");
			text.append(weapon.getDamagestep());
		}

		if (item instanceof ARMORType) {
			ARMORType armor = (ARMORType)item;
			text.append(" (");
			text.append(armor.getPhysicalarmor());
			text.append("/");
			text.append(armor.getMysticarmor());
			text.append("/");
			text.append(armor.getPenalty());
			text.append(")");
		}

		if( item instanceof MAGICITEMType ) {
			MAGICITEMType magicitem = (MAGICITEMType)item;
			text.append(" (");
			text.append(magicitem.getSpelldefense());
			text.append("/");
			text.append(magicitem.getBlooddamage());
			text.append("/");
			text.append(magicitem.getDepatterningrate());
			text.append("/");
			text.append(magicitem.getEnchantingdifficultynumber());
			text.append(")");
			if( ! magicitem.getEffect().isEmpty() ) {
				text.append(" -- ");
				text.append(magicitem.getEffect());
			}
		}

		if( item instanceof PATTERNITEMType ) {
			PATTERNITEMType patternitem = (PATTERNITEMType)item;
			if( ! patternitem.getTruepattern().isEmpty() ) {
				text.append(" -- ");
				text.append(patternitem.getTruepattern());
			}
		}

		if( item instanceof THREADITEMType ) {
			THREADITEMType threaditem = (THREADITEMType)item;
			text.append(" (");
			text.append(threaditem.getSpelldefense());
			text.append("/");
			text.append(threaditem.getMaxthreads());
			text.append("/");
			text.append(threaditem.getWeaventhreadrank());
			text.append(")");
		}

		if( item.getUsed().equals(YesnoType.YES) ) text.append(", in use");
		if( item.getVirtual().equals(YesnoType.YES) ) text.append(", virtual");
		if( !item.getBookref().isEmpty() ) {
			text.append(" (");
			text.append(item.getBookref());
			text.append(")");
		}
		label.setText(text.toString());
	}

	private String stripExtension(String pResourceName ) {
		final int i = pResourceName.lastIndexOf('.');
		if (i < 0) return pResourceName;
		return pResourceName.substring(0,i);
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
