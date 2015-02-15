package de.earthdawn.ui2.tree;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.COINSType;
import de.earthdawn.data.DEFENSEABILITYType;
import de.earthdawn.data.MAGICITEMType;
import de.earthdawn.data.ITEMType;
import de.earthdawn.data.PATTERNITEMType;
import de.earthdawn.data.SHIELDType;
import de.earthdawn.data.TALENTABILITYType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;
import de.earthdawn.data.WEAPONType;
import de.earthdawn.data.YesnoType;

public class ItemTreeCellEditor extends  DefaultTreeCellRenderer implements TreeCellEditor {
	private static final long serialVersionUID = 4228894595876362173L;
	private AbstractNodePanel<?> editorPanel;
	@SuppressWarnings("rawtypes")
	private Map<Class,Class> registerEditors;
	
	@SuppressWarnings("rawtypes")
	public ItemTreeCellEditor(){
		registerEditors = new HashMap<Class,Class>();
		registerEditors.put(ITEMType.class,ItemNodePanel.class);
		registerEditors.put(WEAPONType.class,WeaponNodePanel.class);
		registerEditors.put(ARMORType.class,ArmorNodePanel.class);
		registerEditors.put(SHIELDType.class,ShieldNodePanel.class);
		registerEditors.put(MAGICITEMType.class,MagicitemNodePanel.class);
		registerEditors.put(PATTERNITEMType.class,PatternitemNodePanel.class);
		registerEditors.put(THREADITEMType.class,ThreadItemNodePanel.class);
		registerEditors.put(COINSType.class,CoinsNodePanel.class);
		registerEditors.put(THREADRANKType.class,ThreadRankNodePanel.class);
		registerEditors.put(DEFENSEABILITYType.class, DefenseAbilityNodePanel.class);
		registerEditors.put(StringNode.class, StringNodePanel.class);
		registerEditors.put(TALENTABILITYType.class,ThreadRankTalentNodePanel.class);
		registerEditors.put(DisziplinAbilityNode.class, DisziplinAbilityNodePanel.class);
	}
	
	@Override
	public void addCellEditorListener(CellEditorListener arg0) {
	}

	@Override
	public void cancelCellEditing() {
	}

	@Override
	public Object getCellEditorValue() {
		return editorPanel.getNodeObject();
	}

	@Override
	public boolean isCellEditable(EventObject event) {
		boolean returnValue = false;
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) event;
			if (mouseEvent.getClickCount() > 1){
				JTree tree = (JTree)event.getSource();  
				TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
				if (path != null) {
					Object node = path.getLastPathComponent();
					if (node != null) {
						returnValue = (registerEditors.containsKey(node.getClass()));
						if(returnValue && (node instanceof ITEMType)) {
							ITEMType item = (ITEMType)node;
							returnValue = item.getVirtual().equals(YesnoType.NO);
						}
					}
				}
			}
		}
		return returnValue;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener arg0) {
	}

	@Override
	public boolean shouldSelectCell(EventObject arg0) {
		return false;
	}

	@Override
	public boolean stopCellEditing() {
		editorPanel.updateObject();
		return true;
	}

	@Override
	public Component getTreeCellEditorComponent(final JTree tree, Object value, 
			boolean selected, boolean expanded, boolean leaf, int row) {
		
		if (registerEditors.containsKey(value.getClass())){
			try {
				Class<?> editorclass = registerEditors.get(value.getClass());
				Constructor<?> constructor = editorclass.getConstructor(value.getClass());
				editorPanel = (AbstractNodePanel<?>) constructor.newInstance(value);
				return editorPanel;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}