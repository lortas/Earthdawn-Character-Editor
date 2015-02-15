package de.earthdawn.ui2.tree;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import de.earthdawn.CharacterContainer;
import de.earthdawn.data.COINSType;
import de.earthdawn.data.DISZIPINABILITYType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;
import de.earthdawn.ui2.EDInventoryRootNodeType;
import de.earthdawn.ui2.tree.StringNodeType;

public class ItemTreeModel  implements TreeModel {
	private CharacterContainer character;
	private Map<EDInventoryRootNodeType, Object> displayedNodes;
	private ArrayList<EDInventoryRootNodeType> displayKeys;
	protected ArrayList<TreeModelListener> listeners  = new ArrayList<TreeModelListener>();

	public List<?> getListForGroupNode(EDInventoryRootNodeType groupkey){
		return (List<?>)displayedNodes.get(groupkey);
	}

	public ItemTreeModel(CharacterContainer character) {
		super();

		this.character = character;
		displayedNodes = new TreeMap<EDInventoryRootNodeType, Object>();
		displayKeys = new ArrayList<EDInventoryRootNodeType>();
		if(this.character != null) {
			displayedNodes.put(EDInventoryRootNodeType.ITEMS, character.getItems());
			displayKeys.add(EDInventoryRootNodeType.ITEMS);
			displayedNodes.put(EDInventoryRootNodeType.COMMONMAGICITEMS, character.getMagicItem());
			displayKeys.add(EDInventoryRootNodeType.COMMONMAGICITEMS);
			displayedNodes.put(EDInventoryRootNodeType.BLOODCHARMS, character.getBloodCharmItem());
			displayKeys.add(EDInventoryRootNodeType.BLOODCHARMS);
			displayedNodes.put(EDInventoryRootNodeType.WEAPONS, character.getWeapons());
			displayKeys.add(EDInventoryRootNodeType.WEAPONS);
			displayedNodes.put(EDInventoryRootNodeType.ARMOR, character.getProtection().getARMOROrSHIELD());
			displayKeys.add(EDInventoryRootNodeType.ARMOR);
			displayedNodes.put(EDInventoryRootNodeType.THREADITEMS, character.getThreadItem());
			displayKeys.add(EDInventoryRootNodeType.THREADITEMS);
			displayedNodes.put(EDInventoryRootNodeType.PATTERNITEMS, character.getPatternItem());
			displayKeys.add(EDInventoryRootNodeType.PATTERNITEMS);
			displayedNodes.put(EDInventoryRootNodeType.PURSE, character.getAllCoins());
			displayKeys.add(EDInventoryRootNodeType.PURSE);
		}
	}

	public void fireNewCoins(TreePath parent, List<COINSType> coins) {
		displayedNodes.put(EDInventoryRootNodeType.PURSE, coins);
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
		if(parent instanceof EDInventoryRootNodeType){
			return ((List<?>)displayedNodes.get(parent)).get(index);
		}
		if(parent instanceof THREADITEMType){
			return getThreadItemNodes((THREADITEMType)parent).get(index);
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
		if(parent instanceof EDInventoryRootNodeType){
			return ((List<?>)displayedNodes.get(parent)).size();
		}
		if(parent instanceof THREADITEMType){
			return getThreadItemNodes((THREADITEMType)parent).size();
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
		if(parent instanceof EDInventoryRootNodeType){
			return ((List<?>)displayedNodes.get(parent)).indexOf(child);
		}
		if(parent instanceof THREADITEMType){
			return getThreadItemNodes((THREADITEMType)parent).indexOf(child);
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
		if(node == character)              return false;
		if(node instanceof EDInventoryRootNodeType) return ((List<?>)displayedNodes.get(node)).isEmpty();
		if(node instanceof THREADITEMType) return getThreadItemNodes((THREADITEMType)node).isEmpty();
		if(node instanceof THREADRANKType) return getEffectNodes((THREADRANKType)node).isEmpty();
		return true;
	}

	public static List<?> getEffectNodes(THREADRANKType rank){
		ArrayList<Object> list = new ArrayList<Object>();
		if( rank.getARMOR() != null )  list.add(rank.getARMOR());
		if( rank.getSHIELD() != null ) list.add(rank.getSHIELD());
		if( rank.getWEAPON() != null ) list.add(rank.getWEAPON());
		list.addAll(rank.getDEFENSE());
		int idx=0;
		List<String> spell = rank.getSPELL();
		for( String s : spell ) {
			StringNode stringnode = new StringNode(spell,idx,StringNodeType.SPELL);
			stringnode.setString(s);
			list.add(stringnode);
			idx++;
		}
		idx=0;
		List<DISZIPINABILITYType> recoverytest = rank.getRECOVERYTEST();
		for( DISZIPINABILITYType d : recoverytest ) {
			DisziplinAbilityNode disziplinabilitynode = new DisziplinAbilityNode(recoverytest,idx,DisziplinAbilityNodeType.RECOVERYTEST);
			disziplinabilitynode.setSiziplinAbility(d);
			list.add(disziplinabilitynode);
			idx++;
		}
		list.addAll(rank.getTALENT());
		idx=0;
		List<DISZIPINABILITYType> karmastep = rank.getKARMASTEP();
		for( DISZIPINABILITYType d : karmastep ) {
			DisziplinAbilityNode disziplinabilitynode = new DisziplinAbilityNode(karmastep,idx,DisziplinAbilityNodeType.KARMASTEP);
			disziplinabilitynode.setSiziplinAbility(d);
			list.add(disziplinabilitynode);
			idx++;
		}
		idx=0;
		List<String> ability = rank.getABILITY();
		for( String s : ability ) {
			StringNode stringnode = new StringNode(ability,idx,StringNodeType.ABILITY);
			stringnode.setString(s);
			list.add(stringnode);
			idx++;
		}
		idx=0;
		List<DISZIPINABILITYType> spellability = rank.getSPELLABILITY();
		for( DISZIPINABILITYType d : spellability ) {
			DisziplinAbilityNode disziplinabilitynode = new DisziplinAbilityNode(spellability,idx,DisziplinAbilityNodeType.SPELLABILITY);
			disziplinabilitynode.setSiziplinAbility(d);
			list.add(disziplinabilitynode);
			idx++;
		}
		idx=0;
		List<DISZIPINABILITYType> initiative = rank.getINITIATIVE();
		for( DISZIPINABILITYType d : initiative ) {
			DisziplinAbilityNode disziplinabilitynode = new DisziplinAbilityNode(initiative,idx,DisziplinAbilityNodeType.INITIATIVE);
			disziplinabilitynode.setSiziplinAbility(d);
			list.add(disziplinabilitynode);
			idx++;
		}
		idx=0;
		List<DISZIPINABILITYType> maxkarma = rank.getMAXKARMA();
		for( DISZIPINABILITYType d : maxkarma ) {
			DisziplinAbilityNode disziplinabilitynode = new DisziplinAbilityNode(maxkarma,idx,DisziplinAbilityNodeType.MAXKARMA);
			disziplinabilitynode.setSiziplinAbility(d);
			list.add(disziplinabilitynode);
			idx++;
		}
		return list;
	}

	public static List<?> getThreadItemNodes(THREADITEMType item){
		ArrayList<Object> list = new ArrayList<Object>();
		if( item.getARMOR() != null )  list.add(item.getARMOR());
		if( item.getSHIELD() != null ) list.add(item.getSHIELD());
		if( item.getWEAPON() != null ) list.add(item.getWEAPON());
		list.addAll(item.getTHREADRANK());
		return list;
	}

	public static int getEffectIndex(THREADRANKType rank, int effect) {
		int idx =0;
		if( effect==0 ) return idx;  // 0  : ARMOR
		if( rank.getARMOR() != null ) idx++;
		if( effect==1 ) return idx;  // 1  : SHIELD
		if( rank.getSHIELD() != null ) idx++;
		if( effect==2 ) return idx;  // 2  : WEAPON
		if( rank.getWEAPON() != null ) idx++;
		if( effect==3 ) return idx;  // 3  : DEFENSE
		idx += rank.getDEFENSE().size();
		if( effect==4 ) return idx;  // 4  : SPELL
		idx += rank.getSPELL().size();
		if( effect==5 ) return idx;  // 5  : RECOVERYTEST
		idx += rank.getRECOVERYTEST().size();
		if( effect==6 ) return idx;  // 6  : TALENT
		idx += rank.getTALENT().size();
		if( effect==7 ) return idx;  // 7  : KARMASTEP
		idx += rank.getKARMASTEP().size();
		if( effect==8 ) return idx;  // 8  : ABILITY
		idx += rank.getABILITY().size();
		if( effect==9 ) return idx;  // 9  : SPELLABILITY
		idx += rank.getSPELLABILITY().size();
		if( effect==10 ) return idx; // 10 : INITIATIVE
		idx += rank.getMAXKARMA().size();
		return idx;                  // 11 : MAX KARMA
	}

	public static int getThreadItemIndex(THREADITEMType item, int effect) {
		int idx =0;
		if( effect==0 ) return idx;  // 0  : ARMOR
		if( item.getARMOR() != null ) idx++;
		if( effect==1 ) return idx;  // 1  : SHIELD
		if( item.getSHIELD() != null ) idx++;
		if( effect==2 ) return idx;  // 2  : WEAPON
		if( item.getWEAPON() != null ) idx++;
		return idx;                  // 3  : RANK
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
		for( TreeModelListener listener : listeners ) listener.treeNodesInserted( event );
	}

	public void fireRemove( TreePath parent, Object child, int index ){
		TreeModelEvent event = new TreeModelEvent( 
				this,
				parent,
				new int[] {index},
				new Object[]{ child } );
		for( TreeModelListener listener : listeners ) listener.treeNodesRemoved( event );
	}

	public void fireChange( TreePath parent, Object[] child, int[] index ){
		TreeModelEvent event = new TreeModelEvent( this, parent, index, child );
		for( TreeModelListener listener : listeners ) listener.treeNodesChanged( event );
	}
}
