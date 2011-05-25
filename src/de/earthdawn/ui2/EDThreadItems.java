package de.earthdawn.ui2;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;

public class EDThreadItems extends JPanel {
	private JScrollPane scrollPane;
	private JTree tree;
	private JPopupMenu popupMenu;
	private JMenuItem menuItem;
	private CharacterContainer character;
	private DefaultMutableTreeNode topNode;
	private JPopupMenu popupMenuTree;
	private JMenuItem menuItemAdd;
	private JMenuItem menuItemRemove;
	private JMenuItem menuItemEdit;
	private JMenuItem menuItemActivate;
	
	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {	
		this.character = character;
		initTree();
		System.out.println("ads");
	}
	
	/**
	 * Create the panel.
	 */
	public EDThreadItems() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		scrollPane = new JScrollPane();
		add(scrollPane);
		
		
		topNode =
            new DefaultMutableTreeNode("Thread Items");
		tree = new JTree(topNode);
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
		
		menuItemRemove = new JMenuItem("Remove");
		popupMenuTree.add(menuItemRemove);
		
		menuItemEdit = new JMenuItem("Edit");
		popupMenuTree.add(menuItemEdit);
		
		menuItemActivate = new JMenuItem("Activate");
		popupMenuTree.add(menuItemActivate);
		
	}
	
	private void initTree(){
		topNode = new DefaultMutableTreeNode("Thread Items");
		tree = new JTree(topNode);
		tree.setEditable(true);
		tree.setInvokesStopCellEditing(true); 
     
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				do_tree_mouseReleased(arg0);
			}
		});
		tree.setCellEditor(new EDItemTreeCellEditor());
		scrollPane.setViewportView(tree);
            
		DefaultMutableTreeNode itemNode = null;
        DefaultMutableTreeNode rankNode = null;
        DefaultMutableTreeNode effectNode = null;
        
        for(THREADITEMType item  : character.getThreadItem()){
        	itemNode = new DefaultMutableTreeNode(new ThreadItemInfo(item));
        	int i = 1;
        	for(THREADRANKType rank : item.getTHREADRANK()){
        		rankNode = new DefaultMutableTreeNode(new ThreadRankInfo(i, rank));
        		System.out.println("Rank" +i);
        		itemNode.add(rankNode);
        		ARMORType a = rank.getARMOR();
        		if (a != null){
        			effectNode = new DefaultMutableTreeNode(new ThreadEffectInfo(a));
        			rankNode.add(effectNode);
        			
        		}
        		i++;
        	}
        	topNode.add(itemNode);
        }
        expandAll(tree);
        
        
	}
	
	public void expandAll(JTree tree) {
		int row = 0;
		while (row < tree.getRowCount()) {
			tree.expandRow(row);
			row++;
		}
	}

	
    private class ThreadItemInfo {
        public THREADITEMType threaditem;


        public ThreadItemInfo(THREADITEMType threaditem) {
        	this.threaditem = threaditem;
        }

        @Override
		public String toString() {
        	return threaditem.getName() + " - " + threaditem.getDescription() ;
        }
    }
    
    public class ThreadRankInfo {
        public THREADRANKType threadrank;
        public int rank; 

        public ThreadRankInfo(int rank, THREADRANKType threadrank) {
        	this.threadrank = threadrank;
        	this.rank = rank;
        }

        @Override
		public String toString() {
            return "Rank " + String.valueOf(rank) + ": " + threadrank.getEffect();
        }
    }
    
    private class ThreadEffectInfo{
    	public Object effect;
    	
    	
    	public ThreadEffectInfo(Object effect) {
			this.effect = effect;
		}


		@Override
		public String toString() {
	    	String temp = new String("Effect not implemented!");
	    	if(effect.getClass() == ARMORType.class){
	    		temp = "Armor (Physical:" + ((ARMORType)effect).getPhysicalarmor() + ", Mystic" + + ((ARMORType)effect).getMysticarmor() + ")";
	    	}
	    	
	    	return temp;
        }
    }


	protected void do_tree_mouseReleased(MouseEvent arg0) {
		 if (arg0.isPopupTrigger()) {
		      // das unter der Maus liegende Element selektieren
		      TreePath selPath = this.tree.getPathForLocation(arg0.getX(), arg0.getY());

		      tree.makeVisible(selPath);
		      tree.scrollPathToVisible(selPath);
		      tree.setSelectionPath(selPath);
		      // Popup-Menu zeigen
		      popupMenuTree.show(arg0.getComponent(), arg0.getX(), arg0.getY());
		    }

	}
	
	protected void do_menuItemAdd_actionPerformed(ActionEvent arg0) {
		
		System.out.println("Add");
		if (tree.getSelectionPath().getPathCount() == 1){
			THREADITEMType newitem = new THREADITEMType();
			newitem.setName("New Item");
			newitem.setDescription("Discription");
			character.getThreadItem().add(newitem);
		}
		if (tree.getSelectionPath().getPathCount() == 2){
			 
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
			ThreadItemInfo tii = (ThreadItemInfo)node.getUserObject();
			THREADITEMType threaditem = tii.threaditem;
			THREADRANKType newrank = new THREADRANKType();
			newrank.setEffect("New Rank");
			threaditem.getTHREADRANK().add(0, newrank);
			

		}
		System.out.println();
		initTree();
	}
	
	
	public class EDItemTreeCellEditor extends AbstractCellEditor  implements TreeCellEditor {
		
		DefaultMutableTreeNode node;
		ThreadItemInfoPanel threaditeminfopanel;
		ThreadRankInfoPanel threadrankinfopanel;
		Object userObject;
		
		public EDItemTreeCellEditor() {
			
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
			if  (userObject instanceof ThreadItemInfo){
				((ThreadItemInfo)node.getUserObject()).threaditem.setName(threaditeminfopanel.textFieldName.getText());
				((ThreadItemInfo)node.getUserObject()).threaditem.setDescription(threaditeminfopanel.textFieldDescription.getText());
			}
			if  (userObject instanceof ThreadRankInfo){
				((ThreadRankInfo)node.getUserObject()).threadrank.setEffect(threadrankinfopanel.textFieldEffect.getText());
			}
			
			
			System.out.println("Stopped");
			return true;
		}

		@Override
		public Component getTreeCellEditorComponent(final JTree tree, Object value, 
				boolean selected, boolean expanded, boolean leaf, int row) {
			
			node = (DefaultMutableTreeNode)value;
			if  (userObject instanceof ThreadItemInfo){
				threaditeminfopanel = new ThreadItemInfoPanel((ThreadItemInfo)node.getUserObject());
				threaditeminfopanel.setOpaque(false);
				return threaditeminfopanel;
			}
			if  (userObject instanceof ThreadRankInfo){
				threadrankinfopanel = new ThreadRankInfoPanel((ThreadRankInfo)node.getUserObject());
				threadrankinfopanel.setOpaque(false);
				return threadrankinfopanel;
			}
			
			return null;
			
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
							returnValue = (userObject instanceof ThreadItemInfo) ||
								(userObject instanceof ThreadRankInfo);
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
	}
	
	public class ThreadItemInfoPanel extends JPanel {
		private JTextField textFieldName;
		private JLabel lblName;
		private JLabel lblDescription;
		private JTextField textFieldDescription;
		public ThreadItemInfoPanel(ThreadItemInfo tii) {
			setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

			
			lblName = new JLabel("Name");
			add(lblName);
			System.out.println("name: " + tii.threaditem.getName());
			textFieldName = new JTextField(tii.threaditem.getName());
			add(textFieldName);
			
			textFieldName.setColumns(10);
			
			lblDescription = new JLabel("Description");
			add(lblDescription);
			
			textFieldDescription = new JTextField(tii.threaditem.getDescription());
			add(textFieldDescription);
			textFieldDescription.setColumns(30);
			
			
		}

	}
	
	public class ThreadRankInfoPanel extends JPanel {
		private JLabel lbRank;
		private JTextField textFieldEffect;
		public ThreadRankInfoPanel(ThreadRankInfo tri) {
			setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			
			lbRank = new JLabel("Rank" + tri.rank);
			add(lbRank);
			
			textFieldEffect = new JTextField(tri.threadrank.getEffect());
			add(textFieldEffect);
			textFieldEffect.setColumns(50);
		}

	}

	
}
