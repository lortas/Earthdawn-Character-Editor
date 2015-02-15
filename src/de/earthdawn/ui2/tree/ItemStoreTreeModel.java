package de.earthdawn.ui2.tree;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import de.earthdawn.data.ITEMS;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;
import de.earthdawn.ui2.EDInventoryRootNodeType;

public class ItemStoreTreeModel implements TreeModel {
	private ITEMS itemstore;
	private Map<EDInventoryRootNodeType, Object> displayedNodes;
	private ArrayList<EDInventoryRootNodeType> displayKeys;
	protected ArrayList<TreeModelListener> listeners  = new ArrayList<TreeModelListener>();

	public List<?> getListForGroupNode(EDInventoryRootNodeType groupkey){
		return (List<?>)displayedNodes.get(groupkey);
	}

	public ItemStoreTreeModel(ITEMS itemstore) {
		super();

		this.itemstore = itemstore;
		displayedNodes = new TreeMap<EDInventoryRootNodeType, Object>();
		displayKeys = new ArrayList<EDInventoryRootNodeType>();
		if(this.itemstore != null) {
			displayedNodes.put(EDInventoryRootNodeType.ITEMS, itemstore.getITEM());
			displayKeys.add(EDInventoryRootNodeType.ITEMS);
			displayedNodes.put(EDInventoryRootNodeType.COMMONMAGICITEMS, itemstore.getMAGICITEM());
			displayKeys.add(EDInventoryRootNodeType.COMMONMAGICITEMS);
			displayedNodes.put(EDInventoryRootNodeType.BLOODCHARMS, itemstore.getBLOODCHARMITEM());
			displayKeys.add(EDInventoryRootNodeType.BLOODCHARMS);
			displayedNodes.put(EDInventoryRootNodeType.PATTERNITEMS, itemstore.getPATTERNITEM());
			displayKeys.add(EDInventoryRootNodeType.PATTERNITEMS);
			displayedNodes.put(EDInventoryRootNodeType.WEAPONS, itemstore.getWEAPON());
			displayKeys.add(EDInventoryRootNodeType.WEAPONS);
			displayedNodes.put(EDInventoryRootNodeType.ARMOR, itemstore.getARMOR());
			displayKeys.add(EDInventoryRootNodeType.ARMOR);
			displayedNodes.put(EDInventoryRootNodeType.SHIELD, itemstore.getSHIELD());
			displayKeys.add(EDInventoryRootNodeType.SHIELD);
			displayedNodes.put(EDInventoryRootNodeType.THREADITEMS, itemstore.getTHREADITEM());
			displayKeys.add(EDInventoryRootNodeType.THREADITEMS);
		}
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
		if(parent instanceof EDInventoryRootNodeType){
			return ((List<?>)displayedNodes.get(parent)).get(index);
		}
		if(parent instanceof THREADITEMType){
			return ItemTreeModel.getThreadItemNodes((THREADITEMType)parent).get(index);
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
		if(parent instanceof EDInventoryRootNodeType){
			return ((List<?>)displayedNodes.get(parent)).size();
		}
		if(parent instanceof THREADITEMType){
			return ItemTreeModel.getThreadItemNodes((THREADITEMType)parent).size();
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
		if(parent instanceof EDInventoryRootNodeType){
			return ((List<?>)displayedNodes.get(parent)).indexOf(child);
		}
		if(parent instanceof THREADITEMType){
			return ItemTreeModel.getThreadItemNodes((THREADITEMType)parent).indexOf(child);
		}
		if(parent instanceof THREADRANKType){
			return ItemTreeModel.getEffectNodes((THREADRANKType)parent).indexOf(child);
		}
		return 0;
	}

	@Override
	public Object getRoot() { return itemstore; }

	@Override
	public boolean isLeaf(Object node) {
		if(node == itemstore) return false;
		if(node instanceof EDInventoryRootNodeType) return ((List<?>)displayedNodes.get(node)).isEmpty();
		if(node instanceof THREADITEMType) return ItemTreeModel.getThreadItemNodes((THREADITEMType)node).isEmpty();
		if(node instanceof THREADRANKType) return ItemTreeModel.getEffectNodes((THREADRANKType)node).isEmpty();
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
