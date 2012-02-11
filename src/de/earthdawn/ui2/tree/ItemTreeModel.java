package de.earthdawn.ui2.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import de.earthdawn.CharacterContainer;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.COINSType;
import de.earthdawn.data.DEFENSEType;
import de.earthdawn.data.RECOVERYType;
import de.earthdawn.data.SHIELDType;
import de.earthdawn.data.SPELLType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;
import de.earthdawn.data.WEAPONType;

public class ItemTreeModel  implements TreeModel {
	private CharacterContainer character;
	private HashMap<String, Object> displayedNodes;
	private ArrayList<String> displayKeys;
	protected ArrayList<TreeModelListener> listeners  = new ArrayList<TreeModelListener>();

	public List<?> getListForGroupNode(String groupkey){
		return (List<?>)displayedNodes.get(groupkey);
	}

	public ItemTreeModel(CharacterContainer character) {
		super();

		this.character = character;
		if(this.character != null) {
			// set displayed nodes
			displayedNodes = new HashMap<String, Object>();
			displayedNodes.put("Items", character.getItems());
			displayedNodes.put("Bloodcharms", character.getBloodCharmItem());
			displayedNodes.put("Weapons", character.getWeapons());
			displayedNodes.put("Armor", character.getProtection().getARMOROrSHIELD());
			displayedNodes.put("Thread Items", character.getThreadItem());
			displayedNodes.put("Purse", character.getAllCoins());
			displayKeys = new ArrayList<String>(displayedNodes.keySet());
		}
	}

	public void fireNewCoins(TreePath parent, List<COINSType> coins) {
		displayedNodes.put("Purse", coins);
		TreeModelEvent event = new TreeModelEvent(this,parent,null,null);
		for( TreeModelListener listener : listeners ) listener.treeStructureChanged(event);
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
		listeners.add(arg0);
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


	private List getEffectNodes(THREADRANKType rank){
		ArrayList<Object> list = new ArrayList<Object>();
		if( rank.getARMOR() != null )  list.add(rank.getARMOR());
		if( rank.getSHIELD() != null ) list.add(rank.getSHIELD());
		if( rank.getWEAPON() != null ) list.add(rank.getWEAPON());
		list.addAll(rank.getDEFENSE());
		list.addAll(rank.getSPELL());
		list.addAll(rank.getABILITY());
		list.addAll(rank.getRECOVERYTEST());
		list.addAll(rank.getTALENT());
		list.addAll(rank.getKARMASTEP());
		list.addAll(rank.getSPELLABILITY());
		list.addAll(rank.getINITIATIVE());
		return list;
	}

	public static int getEffectIndex(THREADRANKType rank, int effect) {
		int idx =0;
		if( effect==0 ) return idx;
		if( rank.getARMOR() != null ) idx++;
		if( effect==1 ) return idx;
		if( rank.getSHIELD() != null ) idx++;
		if( effect==2 ) return idx;
		if( rank.getWEAPON() != null ) idx++;
		if( effect==3 ) return idx;
		idx += rank.getDEFENSE().size();
		if( effect==4 ) return idx;
		idx += rank.getSPELL().size();
		if( effect==5 ) return idx;
		idx += rank.getRECOVERYTEST().size();
		if( effect==6 ) return idx;
		idx += rank.getTALENT().size();
		if( effect==7 ) return idx;
		idx += rank.getKARMASTEP().size();
		if( effect==8 ) return idx;
		idx += rank.getABILITY().size();
		if( effect==9 ) return idx;
		idx += rank.getSPELLABILITY().size();
		return idx;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
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
