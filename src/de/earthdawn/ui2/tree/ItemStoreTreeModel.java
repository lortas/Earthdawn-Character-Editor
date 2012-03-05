package de.earthdawn.ui2.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import de.earthdawn.data.ITEMS;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;

public class ItemStoreTreeModel implements TreeModel {
	private ITEMS itemstore;
	private HashMap<String, Object> displayedNodes;
	private ArrayList<String> displayKeys;
	protected ArrayList<TreeModelListener> listeners  = new ArrayList<TreeModelListener>();

	public List<?> getListForGroupNode(String groupkey){
		return (List<?>)displayedNodes.get(groupkey);
	}

	public ItemStoreTreeModel(ITEMS itemstore) {
		super();

		this.itemstore = itemstore;
		this.displayedNodes = new HashMap<String, Object>();
		if(this.itemstore != null) {
			displayedNodes.put("Items", itemstore.getITEM());
			displayedNodes.put("Common Magic Items", itemstore.getMAGICITEM());
			displayedNodes.put("Bloodcharms", itemstore.getBLOODCHARMITEM());
			displayedNodes.put("Weapons", itemstore.getWEAPON());
			displayedNodes.put("Armor", itemstore.getARMOR());
			displayedNodes.put("Shield", itemstore.getSHIELD());
			displayedNodes.put("Thread Items", itemstore.getTHREADITEM());
		}
		this.displayKeys = new ArrayList<String>(displayedNodes.keySet());
	}

	public Object getParent(Object child){
		if(child instanceof THREADRANKType){
			for(THREADITEMType threaditem : itemstore.getTHREADITEM()){
				if(threaditem.getTHREADRANK().indexOf(child) > -1){
					return threaditem;
				}
			}
		}
		return null;
	}

	@Override
	public void addTreeModelListener(TreeModelListener arg0) {
		listeners.add(arg0);
	}

	@Override
	public Object getChild(Object parent, int index) {
		if(parent instanceof ITEMS){
			return  displayKeys.get(index);
		}
		if(parent instanceof String){
			return ((List<?>)displayedNodes.get(parent)).get(index);
		}
		if(parent instanceof THREADITEMType){
			return ((THREADITEMType)parent).getTHREADRANK().get(index);
		}
		if(parent instanceof THREADRANKType){
			return ItemTreeModel.getEffectNodes((THREADRANKType)parent).get(index);
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if(parent instanceof ITEMS){
			return displayedNodes.size();
		}
		if(parent instanceof String){
			return ((List<?>)displayedNodes.get(parent)).size();
		}
		if(parent instanceof THREADITEMType){
			return ((THREADITEMType)parent).getTHREADRANK().size();
		}
		if(parent instanceof THREADRANKType){
			return ItemTreeModel.getEffectNodes((THREADRANKType)parent).size();
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if(parent instanceof ITEMS){
			return displayKeys.indexOf(child);
		}
		if(parent instanceof String){
			return ((List<?>)displayedNodes.get(parent)).indexOf(child);
		}
		if(parent instanceof THREADITEMType){
			return ((THREADITEMType)parent).getTHREADRANK().indexOf(child);
		}
		if(parent instanceof THREADRANKType){
			return ItemTreeModel.getEffectNodes((THREADRANKType)parent).indexOf(child);
		}
		return 0;
	}

	@Override
	public Object getRoot() {	
		return itemstore;
	}

	@Override
	public boolean isLeaf(Object node) {

		if(node == itemstore){
			return false;
		}

		if(node instanceof String){
			return ((List<?>)displayedNodes.get(node)).isEmpty();
		}

		if(node instanceof THREADITEMType){
			return ((THREADITEMType)node).getTHREADRANK().isEmpty();
		}

		if(node instanceof THREADRANKType){
			return ItemTreeModel.getEffectNodes((THREADRANKType)node).isEmpty();
		}

		return true;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	public void fireAdd( TreePath parent, Object child, int index ){
		TreeModelEvent event = new TreeModelEvent( 
				this,
				parent,
				new int[] {index},
				new Object[]{ child } );

		for( TreeModelListener listener : listeners ){
			listener.treeNodesInserted( event );
		}
	}

	public void fireRemove( TreePath parent, Object child, int index ){
		TreeModelEvent event = new TreeModelEvent( 
				this,
				parent,
				new int[] {index},
				new Object[]{ child } );
		System.out.println("fire");
		for( TreeModelListener listener : listeners ){
			listener.treeNodesRemoved( event );
		}
	}
}
