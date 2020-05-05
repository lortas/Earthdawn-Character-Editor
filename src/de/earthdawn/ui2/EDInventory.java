package de.earthdawn.ui2;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ui2.tree.DisziplinAbilityNode;
import de.earthdawn.ui2.tree.DisziplinAbilityNodeType;
import de.earthdawn.ui2.tree.ItemTreeCellEditor;
import de.earthdawn.ui2.tree.ItemTreeCellRenderer;
import de.earthdawn.ui2.tree.ItemTreeModel;
import de.earthdawn.ui2.tree.StringNode;
import de.earthdawn.ui2.tree.StringNodeType;

import de.earthdawn.data.*;

public class EDInventory extends JPanel {
	private static final long serialVersionUID = 2284943257141442648L;
	private JScrollPane scrollPane;
	private JTree tree;

	private CharacterContainer character;
	private Object currentNode; 
	private TreePath currentPath;
	private BufferedImage backgroundimage = null;
	private EDItemStore itemstore = new EDItemStore(this);

	public CharacterContainer getCharacter() {
		return character;
	}

	protected void finalize() throws Throwable {
		super.finalize();
		if( itemstore!=null ) {
			itemstore.setVisible(false);
			itemstore.dispose();
			itemstore=null;
		}
	}

	public void setCharacter(CharacterContainer character) {	
		this.character = character;
		tree = new JTree(new ItemTreeModel(character));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		tree.setOpaque(false);
		tree.setRootVisible(false);
		scrollPane.setViewportView(tree);
		scrollPane.getViewport().setOpaque(false);
		tree.setEditable(true);
		tree.setCellRenderer(new ItemTreeCellRenderer());
		tree.setCellEditor(new ItemTreeCellEditor());
		tree.setInvokesStopCellEditing(true);

		itemstore.setCharacter(character);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("images/background/inventory.jpg");
			try {
				backgroundimage = ImageIO.read(file);
			} catch (IOException e) {
				System.err.println("can not read background image : "+file.getAbsolutePath());
			}
		}
		if( backgroundimage != null ) g.drawImage(backgroundimage, 0, 0, getWidth(), getHeight(), this);
		super.paintComponent(g);
	}
	/**
	 * Create the panel.
	 */
	public EDInventory() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setOpaque(false);

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane);

		tree = new JTree(new ItemTreeModel(character));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		tree.setOpaque(false);
		scrollPane.add(tree);
		scrollPane.setViewportView(tree);
		scrollPane.getViewport().setOpaque(false);

		itemstore.setCharacter(character);
	}

	protected void do_tree_mouseReleased(MouseEvent event) {
		if( event.getButton() < 2 ) return;
		currentPath = tree.getPathForLocation(event.getX(), event.getY());
		if( currentPath == null ) return;
		currentNode = currentPath.getLastPathComponent();
		if( currentNode == null ) return;
		JPopupMenu popup = new JPopupMenu();

		JMenuItem openstore = new JMenuItem("Open store");
		openstore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				itemstore.setVisible(true);
			}
		});
		popup.add(openstore);
		if( ! popup.getComponent(popup.getComponentCount()-1).getClass().getName().endsWith("Separator") ) popup.addSeparator();

		// Popup for group nodes
		if(currentNode instanceof EDInventoryRootNodeType){
			EDInventoryRootNodeType rootnode = (EDInventoryRootNodeType)currentNode;
			// add item
			if(rootnode.equals(EDInventoryRootNodeType.ITEMS)){
				JMenuItem menuitem = new JMenuItem("Add item");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ITEMType item = new ITEMType();
						item.setName("New Item");
						character.getItems().add(item);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,item, character.getItems().indexOf(item));
						tree.scrollPathToVisible(currentPath.pathByAddingChild(item));
					}
				});
				popup.add(menuitem);
			}
			// add weapon
			if(rootnode.equals(EDInventoryRootNodeType.WEAPONS)){
				JMenuItem menuitem = new JMenuItem("Add weapon");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						WEAPONType weapon = new WEAPONType();
						weapon.setName("New weapon");
						character.getWeapons().add(weapon);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,weapon, character.getWeapons().indexOf(weapon));
						tree.scrollPathToVisible(currentPath.pathByAddingChild(weapon));
					}
				});
				popup.add(menuitem);
			}
			//add threaditem
			if(rootnode.equals(EDInventoryRootNodeType.THREADITEMS)){
				JMenuItem menuitem = new JMenuItem("Add Thread Item");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADITEMType threaditem = new THREADITEMType();
						threaditem.setName("New Thread Item");
						character.getThreadItem().add(threaditem);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,threaditem, character.getThreadItem().indexOf(threaditem));
						tree.scrollPathToVisible(currentPath.pathByAddingChild(threaditem));
					}
				});
				popup.add(menuitem);
			}
			// add armor
			if(rootnode.equals(EDInventoryRootNodeType.ARMOR)){
				JMenuItem menuitem = new JMenuItem("Add Armor");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ARMORType armor = new ARMORType();
						armor.setName("New Armor");
						armor.setKind(ItemkindType.ARMOR);
						character.getProtection().getARMOROrSHIELD().add(armor);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,armor, character.getProtection().getARMOROrSHIELD().indexOf(armor));
						tree.scrollPathToVisible(currentPath.pathByAddingChild(armor));
					}
				});
				popup.add(menuitem);
				menuitem = new JMenuItem("Add Shield");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						SHIELDType shield = new SHIELDType();
						shield.setName("New Shield");
						shield.setKind(ItemkindType.SHIELD);
						character.getProtection().getARMOROrSHIELD().add(shield);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,shield, character.getProtection().getARMOROrSHIELD().indexOf(shield));
						tree.scrollPathToVisible(currentPath.pathByAddingChild(shield));
					}
				});
				popup.add(menuitem);
			}
			// add bloodcharm
			if(rootnode.equals(EDInventoryRootNodeType.BLOODCHARMS)){
				JMenuItem menuitem = new JMenuItem("Add Bloodcharms");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						MAGICITEMType bloodcharm = new MAGICITEMType();
						bloodcharm.setName("New Bloodcharm");
						character.getBloodCharmItem().add(bloodcharm);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,bloodcharm, character.getBloodCharmItem().indexOf(bloodcharm));
						tree.scrollPathToVisible(currentPath.pathByAddingChild(bloodcharm));
					}
				});
				popup.add(menuitem);
			}
			// add purse
			if(rootnode.equals(EDInventoryRootNodeType.PURSE)){
				JMenuItem menuitem = new JMenuItem("Add Purse");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						COINSType coins = new COINSType();
						coins.setKind(ItemkindType.COINS);
						character.getAllCoins().add(coins);
						coins.setName("Purse #"+(1+character.getAllCoins().indexOf(coins)));
						((ItemTreeModel) tree.getModel()).fireNewCoins(currentPath,character.getAllCoins());
						tree.scrollPathToVisible(currentPath.pathByAddingChild(coins));
					}
				});
				popup.add(menuitem);
			}
			// add common magic item
			if(rootnode.equals(EDInventoryRootNodeType.COMMONMAGICITEMS)){
				JMenuItem menuitem = new JMenuItem("Add Common Magic Item");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						MAGICITEMType magicitem = new MAGICITEMType();
						character.getMagicItem().add(magicitem);
						magicitem.setName("Magic Item #"+(1+character.getMagicItem().indexOf(magicitem)));
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,magicitem,character.getMagicItem().indexOf(magicitem));
						tree.scrollPathToVisible(currentPath.pathByAddingChild(magicitem));
					}
				});
				popup.add(menuitem);
			}
			// add pattern item
			if(rootnode.equals(EDInventoryRootNodeType.PATTERNITEMS)){
				JMenuItem menuitem = new JMenuItem("Add Pattern Item");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						PATTERNITEMType patternitem = new PATTERNITEMType();
						character.getPatternItem().add(patternitem);
						patternitem.setName("Pattern Item #"+(1+character.getPatternItem().indexOf(patternitem)));
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,patternitem,character.getPatternItem().indexOf(patternitem));
						tree.scrollPathToVisible(currentPath.pathByAddingChild(patternitem));
					}
				});
				popup.add(menuitem);
			}
		}
		if(currentNode instanceof THREADITEMType) {
			THREADITEMType threaditem = (THREADITEMType)currentNode;
			JMenuItem menuitem = new JMenuItem("Refresh Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					character.refesh();
					List<THREADRANKType> ranks = ((THREADITEMType)currentNode).getTHREADRANK();
					if( ranks.isEmpty() ) tree.scrollPathToVisible(currentPath);
					else tree.scrollPathToVisible(currentPath.pathByAddingChild(ranks.get(ranks.size()-1)));
				}
			});
			popup.add(menuitem);
			if( ! popup.getComponent(popup.getComponentCount()-1).getClass().getName().endsWith("Separator") ) popup.addSeparator();

			if( threaditem.getARMOR() == null ) {
				menuitem = new JMenuItem("Is Armor");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADITEMType threaditem = (THREADITEMType)currentNode;
						ARMORType armor = new ARMORType();
						armor.setName(threaditem.getName());
						if( threaditem.getKind().equals(ItemkindType.UNDEFINED) ) {
							armor.setKind(ItemkindType.ARMOR);
							threaditem.setKind(ItemkindType.ARMOR);
						} else armor.setKind(threaditem.getKind());
						threaditem.setARMOR(armor);
						int idx=ItemTreeModel.getThreadItemIndex(threaditem, 0);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,armor,idx);
						tree.scrollPathToVisible(currentPath.pathByAddingChild(armor));
					}
				});
				popup.add(menuitem);
			} else {
				menuitem = new JMenuItem("No Armor");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { clearThreadItemArmor(currentPath); }
				});
				popup.add(menuitem);
			}
			if( threaditem.getSHIELD() == null ) {
				menuitem = new JMenuItem("Is Shield");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADITEMType threaditem = (THREADITEMType)currentNode;
						SHIELDType shield = new SHIELDType();
						shield.setName(threaditem.getName());
						if( threaditem.getKind().equals(ItemkindType.UNDEFINED) ) {
							shield.setKind(ItemkindType.SHIELD);
							threaditem.setKind(ItemkindType.SHIELD);
						} else shield.setKind(threaditem.getKind());
						threaditem.setSHIELD(shield);
						int idx=ItemTreeModel.getThreadItemIndex(threaditem, 1);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,shield,idx);
						tree.scrollPathToVisible(currentPath.pathByAddingChild(shield));
					}
				});
				popup.add(menuitem);
			} else {
				menuitem = new JMenuItem("No Shield");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { clearThreadItemShield(currentPath); }
				});
				popup.add(menuitem);
			}
			if( threaditem.getWEAPON() == null ) {
				menuitem = new JMenuItem("Is Weapon");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADITEMType threaditem = (THREADITEMType)currentNode;
						WEAPONType weapon = new WEAPONType();
						weapon.setName(threaditem.getName());
						weapon.setKind(threaditem.getKind());
						threaditem.setWEAPON(weapon);
						int idx=ItemTreeModel.getThreadItemIndex(threaditem, 2);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,weapon,idx);
						tree.scrollPathToVisible(currentPath.pathByAddingChild(weapon));
					}
				});
				popup.add(menuitem);
			} else {
				menuitem = new JMenuItem("No Weapon");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { clearThreadItemWeapon(currentPath); }
				});
				popup.add(menuitem);
			}

			menuitem = new JMenuItem("Add Rank");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADITEMType item = (THREADITEMType) currentNode;
					List<THREADRANKType> threadranks = item.getTHREADRANK();
					int lastrank = threadranks.size()-1;
					THREADRANKType rank;
					if( threadranks.isEmpty() ) {
						rank = new THREADRANKType();
						rank.setEffect("");
						rank.setKeyknowledge("");
						rank.setDeed("");
						ARMORType armor = CharacterContainer.copyArmor(item.getARMOR(), false);
						CharacterContainer.makeMaxForge(armor);
						rank.setARMOR(armor);
						SHIELDType shield = (SHIELDType)CharacterContainer.copyArmor(item.getSHIELD(), false);
						CharacterContainer.makeMaxForge(shield);
						rank.setSHIELD(shield);
						WEAPONType weapon = CharacterContainer.copyWeapon(item.getWEAPON(), false);
						CharacterContainer.makeMaxForge(weapon);
						rank.setWEAPON(weapon);
					}
					else rank = CharacterContainer.copyThreadRank(threadranks.get(lastrank));
					threadranks.add(rank);
					int idx=ItemTreeModel.getThreadItemIndex(item, 3);
					idx += threadranks.indexOf(rank);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,rank, idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(rank));
				}
			});
			popup.add(menuitem);
			if( ! ((THREADITEMType)currentNode).getTHREADRANK().isEmpty() ) {
				menuitem = new JMenuItem("Remove Last Rank");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADITEMType item = (THREADITEMType) currentNode;
						int last=item.getTHREADRANK().size()-1;
						item.getTHREADRANK().remove(last);
						((ItemTreeModel) tree.getModel()).fireRemove(currentPath,item, last);
						if( last>0 ) tree.scrollPathToVisible(currentPath.pathByAddingChild(item.getTHREADRANK().get(last-1)));
					}
				});
				popup.add(menuitem);
			}
		}
		if( currentNode instanceof THREADRANKType ) {
			THREADRANKType threadrank = (THREADRANKType) currentNode;
			if( threadrank.getARMOR() == null ) {
				JMenuItem menuitem = new JMenuItem("Is Armor");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADRANKType rank = (THREADRANKType) currentNode;
						ARMORType armor = new ARMORType();
						armor.setName("New Armor");
						armor.setKind(ItemkindType.ARMOR);
						rank.setARMOR(armor);
						int idx=ItemTreeModel.getEffectIndex(rank, 0);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,armor,idx);
						tree.scrollPathToVisible(currentPath.pathByAddingChild(armor));
					}
				});
				popup.add(menuitem);
			} else {
				JMenuItem menuitem = new JMenuItem("No Armor");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { clearThreadRankArmor(currentPath); }
				});
				popup.add(menuitem);
			}
			if( threadrank.getSHIELD() == null ) {
				JMenuItem menuitem = new JMenuItem("Is Shield");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADRANKType rank = (THREADRANKType) currentNode;
						SHIELDType shield = new SHIELDType();
						shield.setName("New Shield");
						shield.setKind(ItemkindType.SHIELD);
						rank.setSHIELD(shield);
						int idx=ItemTreeModel.getEffectIndex(rank, 1);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,shield,idx);
						tree.scrollPathToVisible(currentPath.pathByAddingChild(shield));
					}
				});
				popup.add(menuitem);
			} else {
				JMenuItem menuitem = new JMenuItem("No Shield");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { clearThreadRankShield(currentPath); }
				});
				popup.add(menuitem);
			}
			if( threadrank.getWEAPON() == null ) {
				JMenuItem menuitem = new JMenuItem("Is Weapon");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADRANKType rank = (THREADRANKType) currentNode;
						WEAPONType weapon = new WEAPONType();
						weapon.setName("New Weapon");
						rank.setWEAPON(weapon);
						int idx=ItemTreeModel.getEffectIndex(rank, 2);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,weapon,idx);
						tree.scrollPathToVisible(currentPath.pathByAddingChild(weapon));
					}
				});
				popup.add(menuitem);
			} else {
				JMenuItem menuitem = new JMenuItem("No Weapon");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { clearThreadRankWeapon(currentPath); }
				});
				popup.add(menuitem);
			}
			JMenuItem menuitem = new JMenuItem("Add Defense");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					List<DEFENSEABILITYType> currentdefenses = rank.getDEFENSE();
					List<EffectlayerType> freeDefensekinds = new ArrayList<EffectlayerType>();
					for( EffectlayerType defensekind : EffectlayerType.values() ) {
						boolean notfound=true;
						for( DEFENSEABILITYType currentdefense : currentdefenses ) {
							if( currentdefense.getKind().equals(defensekind) ) notfound=false;
						}
						if( notfound ) freeDefensekinds.add(defensekind);
					}
					DEFENSEABILITYType defense = new DEFENSEABILITYType();
					if( freeDefensekinds.isEmpty() ) {
						for( DEFENSEABILITYType d : currentdefenses ) {
							if( defense.getBonus() > d.getBonus()) {
								defense.setKind(d.getKind());
								defense.setBonus(d.getBonus());
							}
						}
						defense.setBonus(defense.getBonus()+1);
					} else {
						defense.setKind(freeDefensekinds.get(0));
					}
					currentdefenses.add(defense);
					int idx=ItemTreeModel.getEffectIndex(rank, 3);
					idx += currentdefenses.indexOf(defense);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,defense,idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(defense));
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Add Spell");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					String spell = "Spell #"+rank.getSPELL().size();
					int spellidx = rank.getSPELL().size();
					rank.getSPELL().add(spell);
					int idx=spellidx+ItemTreeModel.getEffectIndex(rank, 4);
					StringNode spellnode = new StringNode(rank.getSPELL(),spellidx,StringNodeType.SPELL);
					spellnode.setString(spell);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,spellnode,idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(spellnode));
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Add Recovery Test");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					DISZIPINABILITYType disziplinability = new DISZIPINABILITYType();
					int abilityidx=rank.getRECOVERYTEST().size();
					rank.getRECOVERYTEST().add(disziplinability);
					int idx=abilityidx+ItemTreeModel.getEffectIndex(rank, 5);
					DisziplinAbilityNode abilitynode = new DisziplinAbilityNode(rank.getKARMASTEP(), abilityidx, DisziplinAbilityNodeType.RECOVERYTEST);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,abilitynode,idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(abilitynode));
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Add Talent");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					TALENTABILITYType talentability = new TALENTABILITYType();
					int abilityidx=rank.getTALENT().size();
					talentability.setName("Talent #"+abilityidx);
					rank.getTALENT().add(talentability);
					int idx=abilityidx+ItemTreeModel.getEffectIndex(rank, 6);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,talentability,idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(talentability));
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Add Karma Step");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					DISZIPINABILITYType disziplinability = new DISZIPINABILITYType();
					int abilityidx = rank.getKARMASTEP().size();
					rank.getKARMASTEP().add(disziplinability);
					int idx=abilityidx+ItemTreeModel.getEffectIndex(rank, 7);
					DisziplinAbilityNode abilitynode = new DisziplinAbilityNode(rank.getKARMASTEP(), abilityidx, DisziplinAbilityNodeType.KARMASTEP);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,abilitynode,idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(abilitynode));
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Add Karma Max");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					DISZIPINABILITYType disziplinability = new DISZIPINABILITYType();
					int abilityidx = rank.getMAXKARMA().size();
					rank.getMAXKARMA().add(disziplinability);
					int idx=abilityidx+ItemTreeModel.getEffectIndex(rank, 11);
					DisziplinAbilityNode abilitynode = new DisziplinAbilityNode(rank.getMAXKARMA(), abilityidx, DisziplinAbilityNodeType.MAXKARMA);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,abilitynode,idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(abilitynode));
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Add Ability");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					int abilityidx = rank.getABILITY().size();
					String ability = "Ability #"+abilityidx;
					rank.getABILITY().add(ability);
					StringNode abilitynode = new StringNode(rank.getABILITY(),abilityidx,StringNodeType.ABILITY);
					abilitynode.setString(ability);
					int idx=abilityidx+ItemTreeModel.getEffectIndex(rank, 8);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,abilitynode,idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(abilitynode));
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Add Spell Ability");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					DISZIPINABILITYType disziplinability = new DISZIPINABILITYType();
					int abilityidx=rank.getSPELLABILITY().size();
					rank.getSPELLABILITY().add(disziplinability);
					int idx=abilityidx+ItemTreeModel.getEffectIndex(rank, 9);
					DisziplinAbilityNode abilitynode = new DisziplinAbilityNode(rank.getKARMASTEP(), abilityidx, DisziplinAbilityNodeType.SPELLABILITY);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,abilitynode,idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(abilitynode));
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Add Initiative");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					DISZIPINABILITYType disziplinability = new DISZIPINABILITYType();
					int abilityidx=rank.getINITIATIVE().size();
					rank.getINITIATIVE().add(disziplinability);
					int idx=abilityidx+ItemTreeModel.getEffectIndex(rank, 10);
					DisziplinAbilityNode abilitynode = new DisziplinAbilityNode(rank.getKARMASTEP(), abilityidx, DisziplinAbilityNodeType.INITIATIVE);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,abilitynode,idx);
					tree.scrollPathToVisible(currentPath.pathByAddingChild(abilitynode));
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Remove This Rank");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					TreePath parentPath = currentPath.getParentPath();
					THREADITEMType item = (THREADITEMType) (parentPath.getLastPathComponent());
					int idx = item.getTHREADRANK().indexOf(rank);
					((ItemTreeModel) tree.getModel()).fireRemove(parentPath, rank, idx);
					item.getTHREADRANK().remove(rank);
				}
			});
			popup.add(menuitem);
			THREADITEMType threaditem = (THREADITEMType) (currentPath.getParentPath().getLastPathComponent());
			int idx = threaditem.getTHREADRANK().indexOf(threadrank);
			if( idx > 0 ) {
				menuitem = new JMenuItem("Move This Rank Up");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADRANKType[] rank = new THREADRANKType[2];
						int[] idx = new int[2];
						rank[0] = (THREADRANKType) currentNode;
						THREADITEMType threaditem = (THREADITEMType) (currentPath.getParentPath().getLastPathComponent());
						idx[1] = threaditem.getTHREADRANK().indexOf(rank[0]);
						idx[0] = idx[1]-1;
						rank[1] = threaditem.getTHREADRANK().get(idx[0]);
						threaditem.getTHREADRANK().set(idx[0], rank[0]);
						threaditem.getTHREADRANK().set(idx[1], rank[1]);
						character.refesh();
						tree.scrollPathToVisible(currentPath.getParentPath().pathByAddingChild(threaditem.getTHREADRANK().get(threaditem.getTHREADRANK().size()-1)));
					}
				});
				popup.add(menuitem);
			}
			if( idx < threaditem.getTHREADRANK().size()-1 ) {
				menuitem = new JMenuItem("Move This Rank Down");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						THREADRANKType[] rank = new THREADRANKType[2];
						int[] idx = new int[2];
						rank[1] = (THREADRANKType) currentNode;
						THREADITEMType threaditem = (THREADITEMType) (currentPath.getParentPath().getLastPathComponent());
						idx[0] = threaditem.getTHREADRANK().indexOf(rank[1]);
						idx[1] = idx[0]+1;
						rank[0] = threaditem.getTHREADRANK().get(idx[1]);
						threaditem.getTHREADRANK().set(idx[0], rank[0]);
						threaditem.getTHREADRANK().set(idx[1], rank[1]);
						character.refesh();
						tree.scrollPathToVisible(currentPath.getParentPath().pathByAddingChild(threaditem.getTHREADRANK().get(threaditem.getTHREADRANK().size()-1)));
					}
				});
				popup.add(menuitem);
			}
		}

		Object parent = currentPath.getParentPath().getLastPathComponent();
		if(parent instanceof EDInventoryRootNodeType){
			if( ! popup.getComponent(popup.getComponentCount()-1).getClass().getName().endsWith("Separator") ) popup.addSeparator();
			JMenuItem menuitem;
			final EDInventoryRootNodeType category=(EDInventoryRootNodeType)parent;
			if( (currentNode instanceof SHIELDType) && ((ITEMType)currentNode).getVirtual().equals(YesnoType.NO) ) {
				menuitem= new JMenuItem("Clone shield as thread item");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						SHIELDType shield = (SHIELDType)currentNode;
						THREADITEMType threaditem = new THREADITEMType();
						CharacterContainer.copyItem(shield,threaditem);
						threaditem.setSHIELD((SHIELDType)CharacterContainer.copyArmor(shield, false));
						threaditem.setVirtual(YesnoType.NO);
						character.getThreadItem().add(threaditem);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,threaditem, character.getThreadItem().indexOf(threaditem));
						scrollPathToVisible(threaditem);
					}
				});
				popup.add(menuitem);
			} else if( (currentNode instanceof ARMORType) && ((ITEMType)currentNode).getVirtual().equals(YesnoType.NO) ) {
				menuitem= new JMenuItem("Clone armor as thread item");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ARMORType armor = (ARMORType)currentNode;
						THREADITEMType threaditem = new THREADITEMType();
						CharacterContainer.copyItem(armor,threaditem);
						threaditem.setARMOR(CharacterContainer.copyArmor(armor, false));
						threaditem.setVirtual(YesnoType.NO);
						character.getThreadItem().add(threaditem);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,threaditem, character.getThreadItem().indexOf(threaditem));
						scrollPathToVisible(threaditem);
					}
				});
				popup.add(menuitem);
			} else if( (currentNode instanceof WEAPONType) && ((ITEMType)currentNode).getVirtual().equals(YesnoType.NO) ) {
				menuitem= new JMenuItem("Clone weapon as thread item");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						WEAPONType weapon = (WEAPONType)currentNode;
						THREADITEMType threaditem = new THREADITEMType();
						CharacterContainer.copyItem(weapon,threaditem);
						threaditem.setWEAPON(CharacterContainer.copyWeapon(weapon, false));
						threaditem.setVirtual(YesnoType.NO);
						character.getThreadItem().add(threaditem);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,threaditem, character.getThreadItem().indexOf(threaditem));
						scrollPathToVisible(threaditem);
					}
				});
				popup.add(menuitem);
			}
			menuitem = new JMenuItem("Export as ItemStore file");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ITEMS items=new ITEMS();
					items.setLang(LanguageType.EN);
					if( currentNode instanceof AMMUNITIONType ) items.getAMMUNITION().add((AMMUNITIONType)currentNode);
					else if( currentNode instanceof PATTERNITEMType) items.getPATTERNITEM().add((PATTERNITEMType)currentNode);
					else if( currentNode instanceof THREADITEMType) items.getTHREADITEM().add((THREADITEMType)currentNode);
					else if( currentNode instanceof WEAPONType) items.getWEAPON().add((WEAPONType)currentNode);
					else if( currentNode instanceof SHIELDType) items.getSHIELD().add((SHIELDType)currentNode);
					else if( currentNode instanceof ARMORType) items.getARMOR().add((ARMORType)currentNode);
					else if( currentNode instanceof MAGICITEMType ) {
						if( category.equals(EDInventoryRootNodeType.BLOODCHARMS) ) items.getBLOODCHARMITEM().add((MAGICITEMType)currentNode);
						else items.getMAGICITEM().add((MAGICITEMType)currentNode);
					}
					else if( currentNode instanceof ITEMType ) items.getITEM().add((ITEMType)currentNode);
					EDItemStore.saveItems(null, items);
				}
			});
			popup.add(menuitem);
			//remove
			if( ! popup.getComponent(popup.getComponentCount()-1).getClass().getName().endsWith("Separator") ) popup.addSeparator();
			menuitem = new JMenuItem("Remove Node");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object parent = currentPath.getParentPath().getLastPathComponent();
					List<?> temp = ((ItemTreeModel) tree.getModel()).getListForGroupNode((EDInventoryRootNodeType)parent);
					int i =  temp.indexOf(currentNode);
					int a = JOptionPane.showOptionDialog(tree,
							EDMainWindow.NLS.getString("Confirmation.RemoveNode.text"),
							EDMainWindow.NLS.getString("Confirmation.RemoveNode.title"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							EDMainWindow.OptionDialog_YesNoOptions,
							EDMainWindow.OptionDialog_YesNoOptions[0]);
					if( a == 0 ) {
						temp.remove(currentNode);
						((ItemTreeModel) tree.getModel()).fireRemove(currentPath.getParentPath(),currentNode,i );
					}
				}
			});
			popup.add(menuitem);
		} else if( parent instanceof THREADRANKType ) {
			if( currentNode instanceof SHIELDType ) {
				JMenuItem menuitem = new JMenuItem("No Shield");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { clearThreadRankShield(currentPath.getParentPath()); }
				});
				popup.add(menuitem);
			} else if( currentNode instanceof ARMORType ) {
				JMenuItem menuitem = new JMenuItem("No Armor");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { clearThreadRankArmor(currentPath.getParentPath()); }
				});
				popup.add(menuitem);
			} else if( currentNode instanceof WEAPONType ) {
				JMenuItem menuitem = new JMenuItem("No Armor");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { clearThreadRankWeapon(currentPath.getParentPath()); }
				});
				popup.add(menuitem);
			}
		}

		tree.add(popup);
		popup.show(tree, event.getX(), event.getY());
	}

	public void scrollPathToVisible(Object node) {
		if( tree == null ) return;
		int row=-1;
		TreePath path = null;
		while( path == null ) {
			row++;
			path=tree.getPathForRow(row);
		}
		while( path.getParentPath() != null ) path=path.getParentPath();
		if( node instanceof THREADITEMType ) path=path.pathByAddingChild(EDInventoryRootNodeType.THREADITEMS);
		else if( node instanceof ARMORType ) path=path.pathByAddingChild(EDInventoryRootNodeType.ARMOR);
		else if( node instanceof WEAPONType ) path=path.pathByAddingChild(EDInventoryRootNodeType.WEAPONS);
		else if( node instanceof MAGICITEMType ) {
			if( character.getBloodCharmItem().contains(node) ) path=path.pathByAddingChild(EDInventoryRootNodeType.BLOODCHARMS);
			if( character.getMagicItem().contains(node) ) path=path.pathByAddingChild(EDInventoryRootNodeType.COMMONMAGICITEMS);
		}
		else if( node instanceof ITEMType ) path=path.pathByAddingChild(EDInventoryRootNodeType.ITEMS);

		tree.scrollPathToVisible(path.pathByAddingChild(node));
		tree.expandPath(path.pathByAddingChild(node));
	}

	private void clearThreadItemShield(TreePath path) {
		((THREADITEMType)path.getLastPathComponent()).setSHIELD(null);
		refreshcharacter(path);
	}
	private void clearThreadRankShield(TreePath path) {
		((THREADRANKType)path.getLastPathComponent()).setSHIELD(null);
		refreshcharacter(path);
	}
	private void clearThreadItemArmor(TreePath path) {
		((THREADITEMType)path.getLastPathComponent()).setARMOR(null);
		refreshcharacter(path);
	}
	private void clearThreadRankArmor(TreePath path) {
		((THREADRANKType)path.getLastPathComponent()).setARMOR(null);
		refreshcharacter(path);
	}
	private void clearThreadItemWeapon(TreePath path) {
		((THREADITEMType)path.getLastPathComponent()).setWEAPON(null);
		refreshcharacter(path);
	}
	private void clearThreadRankWeapon(TreePath path) {
		((THREADRANKType)path.getLastPathComponent()).setWEAPON(null);
		refreshcharacter(path);
	}
	private void refreshcharacter(TreePath path) {
		character.refesh();
		tree.scrollPathToVisible(path);
	}
}
