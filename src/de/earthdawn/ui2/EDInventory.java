package de.earthdawn.ui2;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
import de.earthdawn.ECEWorker;
import de.earthdawn.ui2.tree.DisziplinAbilityNode;
import de.earthdawn.ui2.tree.DisziplinAbilityNodeType;
import de.earthdawn.ui2.tree.ItemStoreTreeModel;
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
	}

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("templates/inventory_background.jpg");
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
	}

	protected void do_tree_mouseReleased(MouseEvent event) {
		if( event.getButton() < 2 ) return;
		currentPath = tree.getPathForLocation(event.getX(), event.getY());
		if( currentPath == null ) return;
		currentNode = currentPath.getLastPathComponent();
		if( currentNode == null ) return;
		JPopupMenu popup = new JPopupMenu();

		// Popup for group nodes
		if(currentNode instanceof String){
			String str = (String)currentNode;
			// add item
			if(str.equals("Items")){
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
			if(str.equals("Weapons")){
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
			if(str.equals("Thread Items")){
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
			if(str.equals("Armor")){
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
			if(str.equals("Bloodcharms")){
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
			if(str.equals("Purse")){
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
			JMenuItem menuitem = new JMenuItem("Open store");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					itemstore.setVisible(true);
				}
			});
			popup.add(menuitem);
		}
		if(currentNode instanceof THREADITEMType) {
			JMenuItem menuitem = new JMenuItem("Refresh Character");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					(new ECEWorker()).verarbeiteCharakter(character.getEDCHARACTER());
					character.refesh();
				}
			});
			popup.add(menuitem);
			menuitem = new JMenuItem("Add Rank");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADITEMType item = (THREADITEMType) currentNode;
					THREADRANKType rank = new THREADRANKType();
					rank.setEffect("");
					rank.setKeyknowledge("");
					item.getTHREADRANK().add(rank);
					((ItemTreeModel) tree.getModel()).fireAdd(currentPath,item, item.getTHREADRANK().indexOf(rank));
					tree.scrollPathToVisible(currentPath.pathByAddingChild(rank));
				}
			});
			popup.add(menuitem);
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
						int idx=ItemTreeModel.getEffectIndex(rank, 0);
						rank.setARMOR(armor);
						((ItemTreeModel) tree.getModel()).fireAdd(currentPath,armor,idx);
						tree.scrollPathToVisible(currentPath.pathByAddingChild(armor));
					}
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
			}
			JMenuItem menuitem = new JMenuItem("Add Defense");
			menuitem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					THREADRANKType rank = (THREADRANKType) currentNode;
					DEFENSEABILITYType defense = new DEFENSEABILITYType();
					rank.getDEFENSE().add(defense);
					int idx=ItemTreeModel.getEffectIndex(rank, 3);
					idx += rank.getDEFENSE().indexOf(defense);
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
		}

		//remove
		Object parent = currentPath.getParentPath().getLastPathComponent();
		if(parent instanceof String){
			List<?> temp = ((ItemTreeModel) tree.getModel()).getListForGroupNode((String)parent);
			if(temp != null){
				JMenuItem menuitem = new JMenuItem("Remove");
				menuitem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Object parent = currentPath.getParentPath().getLastPathComponent();
						List<?> temp = ((ItemTreeModel) tree.getModel()).getListForGroupNode((String)parent);
						int i =  temp.indexOf(currentNode);
						String[] options = {"Yes","No"};
						int a = JOptionPane.showOptionDialog(tree,
								"Do really want to remove this node?",
								"Change Portrait?",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[0]);
						if( a == 0 ) {
							temp.remove(currentNode);
							((ItemTreeModel) tree.getModel()).fireRemove(currentPath.getParentPath(),currentNode,i );
						}
					}
				});
				popup.add(menuitem);	
			}
		}

		tree.add(popup);
		popup.show(tree, event.getX(), event.getY());
	}
}
