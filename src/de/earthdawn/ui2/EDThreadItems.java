package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.config.ECECapabilities;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.CAPABILITYType;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.ItemkindType;
import de.earthdawn.data.TALENTABILITYType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;

public class EDThreadItems extends JPanel {
	private JScrollPane scrollPane;
	private JTree tree;
	private JPopupMenu popupMenu;
	private JMenuItem menuItem;
	private CharacterContainer character;
	
	private DefaultMutableTreeNode nodeRoot;
	private DefaultMutableTreeNode nodeThreadItems;
	private DefaultMutableTreeNode nodeMundaneItems;
	private DefaultMutableTreeNode nodeBloodCharmItems;
	private DefaultMutableTreeNode nodeArmorShield;
	
	private JPopupMenu popupMenuTree;
	private JMenuItem menuItemAdd;
	private JMenuItem menuItemRemove;


	private JMenu mnAddEffect;
	private JMenuItem mntmTalentEffect;
	private JMenuItem mntmArmorEffect;
	private JMenuItem menuItem_3;
	private int intWidth = 30;
	private int intHeight = 30;
	private JMenuItem mntmActivar;

	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {	
		this.character = character;
		initTree();
		
	}

	/**
	 * Create the panel.
	 */
	public EDThreadItems() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		scrollPane = new JScrollPane();
		add(scrollPane);


		nodeThreadItems =
			new DefaultMutableTreeNode("Thread items");
		tree = new JTree(nodeThreadItems);
		tree.setEditable(true);

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		scrollPane.setViewportView(tree);

		popupMenuTree = new JPopupMenu();
		add(popupMenuTree);

		menuItemAdd = new JMenuItem("Add");
		menuItemAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				do_menuItemAdd_actionPerformed(arg0);
			}
		});

		popupMenuTree.add(menuItemAdd);

		mnAddEffect = new JMenu("Add effect");
		popupMenuTree.add(mnAddEffect);

		mntmArmorEffect = new JMenuItem("Armor effect");
		mntmArmorEffect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmArmorEffect_actionPerformed(arg0);
			}
		});
		mnAddEffect.add(mntmArmorEffect);

		mntmTalentEffect = new JMenuItem("Talent effect");
		mntmTalentEffect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmTalentEffect_actionPerformed(arg0);
			}
		});
		mnAddEffect.add(mntmTalentEffect);

		menuItem_3 = new JMenuItem("New menu item");
		mnAddEffect.add(menuItem_3);

		menuItemRemove = new JMenuItem("Remove");
		menuItemRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_menuItemRemove_actionPerformed(arg0);
			}
		});
		popupMenuTree.add(menuItemRemove);
		
		mntmActivar = new JMenuItem("Activate");
		mntmActivar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmActivar_actionPerformed(arg0);
			}
		});
		popupMenuTree.add(mntmActivar);

	}

	private void initTree(){
		nodeRoot = new DefaultMutableTreeNode("Inventory");
		nodeThreadItems = new DefaultMutableTreeNode("Thread items");
		nodeMundaneItems = new DefaultMutableTreeNode("Mundane items");
		nodeBloodCharmItems = new DefaultMutableTreeNode("Bloodcharms");
		nodeArmorShield = new DefaultMutableTreeNode("Armor/Shield");
		
		
		nodeRoot.add(nodeMundaneItems);
		nodeRoot.add(nodeArmorShield);
		nodeRoot.add(nodeBloodCharmItems);
		nodeRoot.add(nodeThreadItems);
		
		tree = new JTree(nodeRoot);
		tree.setRootVisible(false);
		
		
		tree.setEditable(true);
		tree.setInvokesStopCellEditing(true); 

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		tree.setCellEditor(new EDItemTreeCellEditor());
		tree.setCellRenderer(new EDItemTreeCellEditor());
		scrollPane.setViewportView(tree);
		// normal items
		DefaultMutableTreeNode itemNode = null;
		for(ITEMType item : character.getItems()){
			itemNode = new DefaultMutableTreeNode(item.getName());
			nodeMundaneItems.add(itemNode);
		}
		
		// threaditems
		DefaultMutableTreeNode rankNode = null;
		DefaultMutableTreeNode effectNode = null;

		for(THREADITEMType item  : character.getThreadItem()){
			itemNode = new DefaultMutableTreeNode(new ThreadItemInfo(item));
			int i = 1;
			for(THREADRANKType rank : item.getTHREADRANK()){
				rankNode = new DefaultMutableTreeNode(new ThreadRankInfo(i, rank));
				
				itemNode.add(rankNode);
				ARMORType a = rank.getARMOR();
				if (a != null){
					effectNode = new DefaultMutableTreeNode(new ThreadEffectArmor(a));
					rankNode.add(effectNode);

				}
				for(TALENTABILITYType talent : rank.getTALENT()){
					effectNode = new DefaultMutableTreeNode(new ThreadEffectTalent(talent));
					rankNode.add(effectNode);
				}
				i++;
			}
			nodeThreadItems.add(itemNode);
		}
		
		//expandAll(tree);


	}

	public void expandAll(JTree tree) {
		int row = 0;
		while (row < 2) {
			tree.expandRow(row);
			row++;
		}
	}





	protected void do_tree_mouseReleased(MouseEvent arg0) {
		if (arg0.getButton() == arg0.BUTTON3) {

			// das unter der Maus liegende Element selektieren
			TreePath selPath = this.tree.getPathForLocation(arg0.getX(), arg0.getY());

			tree.makeVisible(selPath);
			tree.scrollPathToVisible(selPath);
			tree.setSelectionPath(selPath);
			// Popup-Menu zeigen
			popupMenuTree.show(arg0.getComponent(), arg0.getX(), arg0.getY());
		}

	}


	protected void do_menuItemRemove_actionPerformed(ActionEvent arg0) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
		Object userObject = node.getUserObject();
		if (userObject instanceof ThreadItemInfo){
			character.getThreadItem().remove(((ThreadItemInfo)userObject).getUserObject());
		}
		if (userObject instanceof ThreadRankInfo){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			ThreadItemInfo parentItem = (ThreadItemInfo)parent.getUserObject();	
			THREADITEMType item = (THREADITEMType)parentItem.getUserObject();
			item.getTHREADRANK().remove(((IntNode)userObject).getUserObject());
		}
		if (userObject instanceof ThreadEffectArmor){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			ThreadRankInfo parentRank = (ThreadRankInfo)parent.getUserObject();	
			THREADRANKType rank = (THREADRANKType)parentRank.getUserObject();
			if (userObject instanceof IntEffectNode){
				rank.setARMOR(null);
			}
		}
		initTree();

	}
	
	protected void do_mntmActivar_actionPerformed(ActionEvent arg0) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
		Object userObject = node.getUserObject();
		if (userObject instanceof ThreadRankInfo){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			ThreadItemInfo parentItem = (ThreadItemInfo)parent.getUserObject();	
			ThreadRankInfo rankinfo = (ThreadRankInfo)userObject;
			parentItem.threaditem.setWeaventhreadrank(rankinfo.rank); 
		}	
		initTree();
	}

	protected void do_menuItemAdd_actionPerformed(ActionEvent arg0) {

		System.out.println("Add");
		if (tree.getSelectionPath().getPathCount() == 2){
			THREADITEMType newitem = new THREADITEMType();
			newitem.setName("New Item");
			newitem.setDescription("Discription");
			character.getThreadItem().add(newitem);
		}
		if (tree.getSelectionPath().getPathCount() == 3){

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
			ThreadItemInfo tii = (ThreadItemInfo)node.getUserObject();
			THREADITEMType threaditem = tii.threaditem;
			THREADRANKType newrank = new THREADRANKType();
			newrank.setEffect("New Rank");
			threaditem.getTHREADRANK().add(0, newrank);


		}

		initTree();
	}

	protected void do_mntmArmorEffect_actionPerformed(ActionEvent arg0) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
		Object userObject = node.getUserObject();
		if (userObject instanceof ThreadRankInfo){
			THREADRANKType rank = (THREADRANKType)((ThreadRankInfo)userObject).getUserObject();
			rank.setARMOR(new ARMORType());
		}
		initTree();
	}

	protected void do_mntmTalentEffect_actionPerformed(ActionEvent arg0) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
		Object userObject = node.getUserObject();
		if (userObject instanceof ThreadRankInfo){
			THREADRANKType rank = (THREADRANKType)((ThreadRankInfo)userObject).getUserObject();
			TALENTABILITYType talent = new TALENTABILITYType();
			ECECapabilities capabilities = new ECECapabilities(ApplicationProperties.create().getCapabilities().getSKILLOrTALENT());
			talent.setName(capabilities.getTalents().get(1).getName());
			rank.getTALENT().add(talent);

		}
		initTree();		
	}




	public class EDItemTreeCellEditor extends  DefaultTreeCellRenderer   implements TreeCellEditor {

		DefaultMutableTreeNode node;
		JLabel renderComponent;
		ImageIcon rootIconClosed; 
		ImageIcon rootIconOpen; 
		Icon defaultIconClosed; 
		Icon defaultIconOpen; 
		Icon defaultIconLeaf; 
		HashMap<String, ImageIcon> treeIcons;

		Object userObject;

		private String stripExtension(String pResourceName ) {
			final int i = pResourceName.lastIndexOf('.');
			if (i < 0) {
				return pResourceName;
			}
			final String withoutExtension = pResourceName.substring(0, i);
			return withoutExtension;
		}

		public EDItemTreeCellEditor() {
			System.out.println("Init");
			File dir = new File("./icons");

			treeIcons = new HashMap();
			for(String strFilename : dir.list()){
				System.out.println("Filename:" + stripExtension(strFilename));
				ImageIcon orgIcon = new ImageIcon("./icons/" + strFilename);
				treeIcons.put(stripExtension(strFilename),  scale(orgIcon.getImage()));
			}
		}

		private ImageIcon scale(Image src) {

			int type = BufferedImage.TRANSLUCENT;
			BufferedImage dst = new BufferedImage(intWidth, intHeight, type);
			Graphics2D g2 = dst.createGraphics();
			g2.drawImage(src, 0, 0, intWidth, intHeight, this);
			g2.dispose();
			return new ImageIcon(dst);
		}



		@Override
		public void cancelCellEditing() {
			System.out.println("cancelCellEditing");

		}

		@Override
		public Object getCellEditorValue() {


			return node.getUserObject();
		}




		@Override
		public boolean stopCellEditing() {
			((IntNode)node.getUserObject()).updateUserObject();
			System.out.println("Stopped");
			return true;
		}

		@Override
		public Component getTreeCellEditorComponent(final JTree tree, Object value, 
				boolean selected, boolean expanded, boolean leaf, int row) {
			node = (DefaultMutableTreeNode)value;			
			return ((IntNode)userObject).getEditor();	
		}


		@Override
		public void addCellEditorListener(CellEditorListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isCellEditable(EventObject event) {
			boolean returnValue = false;
			if (event instanceof MouseEvent) {
				MouseEvent mouseEvent = (MouseEvent) event;
				if (mouseEvent.getClickCount() > 1){
					TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
					if (path != null) {
						Object node = path.getLastPathComponent();
						if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
							DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
							userObject = treeNode.getUserObject();
							//returnValue = (userObject instanceof ThreadItemInfo) ||
							//	(userObject instanceof ThreadRankInfo);
							returnValue = (userObject instanceof IntNode);

						}
					}
				}
			}
			return returnValue;	
		}

		@Override
		public void removeCellEditorListener(CellEditorListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean shouldSelectCell(EventObject arg0) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			Object userObject = node.getUserObject();
			renderComponent = new JLabel();
			renderComponent.setIcon((ImageIcon)treeIcons.get("UNDEFINED"));
			renderComponent.setText(node.getUserObject().toString());
			Font f = renderComponent.getFont();
			renderComponent.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

			// Set container icons
			
			if(node.getLevel() == 1){
				if(expanded){
					renderComponent.setIcon((ImageIcon)treeIcons.get("CHEST_OPEN"));
				}
				else{
					renderComponent.setIcon((ImageIcon)treeIcons.get("CHEST_CLOSED"));
				}
				if(node.getUserObject().toString().equals("Bloodcharms")){
					renderComponent.setIcon((ImageIcon)treeIcons.get("BLOOD_CHARM"));
				}
				if(node.getUserObject().toString().equals("Armor/Shield")){
					renderComponent.setIcon((ImageIcon)treeIcons.get("HELM"));
				}			
				if(node.getUserObject().toString().equals("Mundane items")){
					renderComponent.setIcon((ImageIcon)treeIcons.get("BACKPACK"));
				}		
				
				
			}
			
			
			// Set icon for Threaditems
			if(userObject instanceof ThreadItemInfo){
				String strKind = ((ThreadItemInfo)userObject).threaditem.getKind().toString();

				if(treeIcons.containsKey(strKind)){
					renderComponent.setIcon((ImageIcon)treeIcons.get(strKind));
				}
			}

			if(userObject instanceof ThreadRankInfo){
				int intRank = ((ThreadRankInfo)userObject).rank;
				// Icon
				if(treeIcons.containsKey("RANK" + intRank)){
					renderComponent.setIcon((ImageIcon)treeIcons.get("RANK" + intRank));	
				}
				else{
					renderComponent.setIcon((ImageIcon)treeIcons.get("RANKn"));
				}
				//Active rank bold
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
				ThreadItemInfo parentItem = (ThreadItemInfo)parent.getUserObject();	
				THREADITEMType item = (THREADITEMType)parentItem.getUserObject();
				if(item.getWeaventhreadrank() == intRank){
					renderComponent.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
				}
				
			}



			return renderComponent;
		}
	}

	//*****************************************************************************************
	// Utilclasses	
	//*****************************************************************************************
	interface IntNode{
		public JPanel editor = null;   

		public JPanel getEditor();
		public JPanel getRenderer();
		public Object getUserObject();
		public String toString();
		public void updateUserObject();
	}

	interface IntEffectNode{
		public String getEffectType();
	}

	private class ThreadItemInfo implements IntNode {
		public THREADITEMType threaditem;
		public JPanel editor = null; 
		private JTextField textFieldName;
		private JLabel lblName;
		private JLabel lblItemType;
		private JComboBox comboBoxItemType;
		private JLabel lblDescription;
		private JTextField textFieldDescription;
		private JSpinner spinnerLpcostgrowth;

		public ThreadItemInfo(THREADITEMType threaditem) {
			this.threaditem = threaditem;

		}

		@Override
		public String toString() {
			return threaditem.getName() + " - " + threaditem.getDescription() ;
		}

		@Override
		public JPanel getEditor() {
			JPanel editor = new JPanel();
			editor.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			lblItemType = new JLabel("Itemtype");
			editor.add(lblItemType);

			comboBoxItemType = new JComboBox(ItemkindType.values());
			editor.add(comboBoxItemType);
			comboBoxItemType.setSelectedItem(threaditem.getKind());

			lblName = new JLabel("Name");
			editor.add(lblName);

			textFieldName = new JTextField(threaditem.getName());
			editor.add(textFieldName);

			textFieldName.setColumns(10);

			lblDescription = new JLabel("Description");
			editor.add(lblDescription);

			textFieldDescription = new JTextField(threaditem.getDescription());
			editor.add(textFieldDescription);
			textFieldDescription.setColumns(15);
			
			JLabel lblTemp = new JLabel("LP-Cost");
			editor.add(lblTemp);
			
			spinnerLpcostgrowth = new JSpinner();
			spinnerLpcostgrowth.setModel(new SpinnerNumberModel(threaditem.getLpcostgrowth(), 1, 9, 1));
			editor.add(spinnerLpcostgrowth);
			
			
			
			editor.setOpaque(false);
			
			
			return editor;
		}

		@Override
		public JPanel getRenderer() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getUserObject() {
			return threaditem;
		}

		@Override
		public void updateUserObject() {
			threaditem.setName(textFieldName.getText());
			threaditem.setDescription(textFieldDescription.getText());
			threaditem.setKind((ItemkindType) comboBoxItemType.getSelectedItem());
			threaditem.setLpcostgrowth((Integer)spinnerLpcostgrowth.getValue());
		}
	}

	public class ThreadRankInfo implements IntNode {
		public THREADRANKType threadrank;
		public int rank; 
		public JPanel editor = null; 
		public JTextField textFieldEffect = null;

		public ThreadRankInfo(int rank, THREADRANKType threadrank) {
			this.threadrank = threadrank;
			this.rank = rank;
		}

		@Override
		public String toString() {
			return "Rank " + String.valueOf(rank) + ": " + threadrank.getEffect();
		}

		@Override
		public JPanel getEditor() {
			editor = new JPanel(); 
			JLabel lbRank;


			editor.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			lbRank = new JLabel("Rank" + rank);
			editor.add(lbRank);

			textFieldEffect = new JTextField(threadrank.getEffect());
			editor.add(textFieldEffect);
			textFieldEffect.setColumns(50);
			editor.setOpaque(false);
			return editor;
		}

		@Override
		public JPanel getRenderer() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getUserObject() {
			return threadrank;
		}

		@Override
		public void updateUserObject() {
			threadrank.setEffect(textFieldEffect.getText());

		}
	}



	public class ThreadEffectArmor implements IntNode,IntEffectNode {
		public ARMORType armoreffect;
		public JPanel editor = null; 
		private JLabel lbPhysicArmor;
		private JTextField textFieldPhysicArmor;
		private JLabel lblMysticArmor;
		private JTextField textFieldMysticArmor;
		private JLabel lblPenalty;
		private JTextField textFieldPenalty;

		public ThreadEffectArmor(ARMORType armoreffect) {
			super();
			this.armoreffect = armoreffect;
		}

		@Override
		public JPanel getEditor() {
			System.out.println("getEditor");
			JPanel editor = new JPanel();
			editor.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			lbPhysicArmor = new JLabel("Physical Armor");
			editor.add(lbPhysicArmor);

			textFieldPhysicArmor = new JTextField();

			textFieldPhysicArmor.setDocument(new NumericDocument());
			textFieldPhysicArmor.setText(String.valueOf((armoreffect.getPhysicalarmor())));
			editor.add(textFieldPhysicArmor);
			textFieldPhysicArmor.setColumns(10);

			lblMysticArmor = new JLabel("Mystic Armor");
			editor.add(lblMysticArmor);

			textFieldMysticArmor = new JTextField();
			textFieldMysticArmor.setText(String.valueOf((armoreffect.getMysticarmor())));
			editor.add(textFieldMysticArmor);
			textFieldMysticArmor.setColumns(10);

			lblPenalty = new JLabel("Penalty");
			editor.add(lblPenalty);

			textFieldPenalty = new JTextField();
			textFieldPenalty.setText(String.valueOf((armoreffect.getPenalty())));
			editor.add(textFieldPenalty);
			textFieldPenalty.setColumns(10);

			editor.setOpaque(false);

			return editor;
		}

		@Override
		public JPanel getRenderer() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getUserObject() {
			return armoreffect;
		}

		@Override
		public void updateUserObject() {
			armoreffect.setPhysicalarmor(new Integer(textFieldPhysicArmor.getText()));
			armoreffect.setMysticarmor(new Integer(textFieldMysticArmor.getText()));
			armoreffect.setPenalty(new Integer(textFieldPenalty.getText()));
		}

		@Override
		public String toString() {
			return "Armor (Physical:" + armoreffect.getPhysicalarmor() + ", Mystic:" +  armoreffect.getMysticarmor() + ", Penalty:" +  armoreffect.getPenalty() + ")";
		}

		@Override
		public String getEffectType() {
			return "Armor";
		}

	}

	public class ThreadEffectTalent implements IntNode,IntEffectNode {
		public TALENTABILITYType talent;
		public JPanel editor = null; 
		private JLabel lbTalent;
		private JLabel lblBonus;
		private JTextField textFieldBonus;
		private JComboBox comboBoxTalent;

		public ThreadEffectTalent(TALENTABILITYType talent) {
			super();
			this.talent = talent;
		}

		@Override
		public JPanel getEditor() {
			System.out.println("getEditor");
			JPanel editor = new JPanel();
			editor.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			lbTalent = new JLabel("Talent");
			editor.add(lbTalent);

			comboBoxTalent = new JComboBox();
			editor.add(comboBoxTalent);
			ECECapabilities capabilities = new ECECapabilities(ApplicationProperties.create().getCapabilities().getSKILLOrTALENT());
			for (CAPABILITYType capability : capabilities.getTalents()) comboBoxTalent.addItem(capability.getName());
			comboBoxTalent.setSelectedItem(talent.getName());

			/*lblBonus = new JLabel("Bonus");
				editor.add(lblBonus);

				textFieldBonus = new JTextField();
				textFieldBonus.setText(talent.getB)
				editor.add(textFieldBonus);
				textFieldBonus.setColumns(10);*/

			editor.setOpaque(false);

			return editor;
		}

		@Override
		public JPanel getRenderer() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getUserObject() {
			return talent;
		}

		@Override
		public void updateUserObject() {
			talent.setName((String)comboBoxTalent.getSelectedItem());
		}

		@Override
		public String toString() {
			return "Talent: " + talent.getName();
		}

		@Override
		public String getEffectType() {
			return "Talent";
		}

	}	





}
