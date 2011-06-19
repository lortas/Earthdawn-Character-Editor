package de.earthdawn.ui2.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;

public class ItemTreeModel implements TreeModel {
	private CharacterContainer character;
	private HashMap<String, Object> displayedNodes;
	private ArrayList<String> displayKeys;
	
	public ItemTreeModel(CharacterContainer character) {
		super();
		this.character = character;
		if(this.character != null) {
			// set displayed nodes
			displayedNodes = new HashMap<String, Object>();
			displayedNodes.put("Items", character.getItems());
			displayedNodes.put("Blodcharms", character.getBloodCharmItem());
			displayedNodes.put("Weapons", character.getWeapons());
			displayedNodes.put("Armor", character.getProtection().getARMOROrSHIELD());
			displayedNodes.put("Threasd Items", character.getThreadItem());
			displayKeys = new ArrayList<String>(displayedNodes.keySet());
			
		}
	}

	public Object getParent(Object child){
		if(child instanceof THREADRANKType){
			for(THREADITEMType threaditem : character.getThreadItem()){
				if(threaditem.getTHREADRANK().indexOf(child) > -1){
					return threaditem;
				}
			}
		}
		
		return null;
	}

	@Override
	public void addTreeModelListener(TreeModelListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getChild(Object parent, int index) {
		if(parent instanceof CharacterContainer){
			return  displayKeys.get(index);
		}
		
		if(parent instanceof String){
			return ((List)displayedNodes.get(parent)).get(index);
		}
		
		if(parent instanceof THREADITEMType){
			return ((THREADITEMType)parent).getTHREADRANK().get(index);
		}
		
		if(parent instanceof THREADRANKType){
			return getEffectNodes((THREADRANKType)parent).get(index);
		}			
		
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if(parent instanceof CharacterContainer){
			return displayedNodes.size();
		}
		
		if(parent instanceof String){
			return ((List)displayedNodes.get(parent)).size();
		}
		
		if(parent instanceof THREADITEMType){
			return ((THREADITEMType)parent).getTHREADRANK().size();
		}
		
		if(parent instanceof THREADRANKType){
			return getEffectNodes((THREADRANKType)parent).size();
		}		

		return 0;	
		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if(parent instanceof CharacterContainer){
			return displayKeys.indexOf(child);
		}
		
		if(parent instanceof String){
			return ((List)displayedNodes.get(parent)).indexOf(child);
		}
		
		if(parent instanceof THREADITEMType){
			return ((THREADITEMType)parent).getTHREADRANK().indexOf(child);
		}

		if(parent instanceof THREADRANKType){
			return getEffectNodes((THREADRANKType)parent).indexOf(child);
		}
		
		
		return 0;
	}

	@Override
	public Object getRoot() {	
		return character;
	}

	@Override
	public boolean isLeaf(Object node) {
		
		if(node == character){
			return false;
		}
		if(node instanceof String){
			return ((List)displayedNodes.get(node)).isEmpty();
		}
		
		if(node instanceof THREADITEMType){
			return ((THREADITEMType)node).getTHREADRANK().isEmpty();
		}
		
		if(node instanceof THREADRANKType){
			return getEffectNodes((THREADRANKType)node).isEmpty();
		}
		
		return true;
		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
	
	private List getEffectNodes(THREADRANKType rank){
		ArrayList<Object> list = new ArrayList<Object>();
		if(rank.getARMOR() != null){
			list.add(rank.getARMOR());
		}
		if(rank.getWEAPON() != null){
			list.add(rank.getWEAPON());
		}
		if(rank.getSHIELD() != null){
			list.add(rank.getSHIELD());
		}
		list.addAll(rank.getSPELL());
		list.addAll(rank.getABILITY());
		list.addAll(rank.getRECOVERYTEST());
		list.addAll(rank.getTALENT());
		list.addAll(rank.getKARMASTEP());
		list.addAll(rank.getSPELLABILITY());
		list.addAll(rank.getINITIATIVE());
		list.addAll(rank.getDEFENSE());
		return list;
		
	}

}
