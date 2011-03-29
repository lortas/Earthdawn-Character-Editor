package de.earthdawn.ui2;

import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.tree.DefaultMutableTreeNode;

import de.earthdawn.CharacterContainer;
import de.earthdawn.data.ARMORType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.THREADRANKType;

public class EDThreadItems extends JPanel {
	private JScrollPane scrollPane;
	private JTree tree;
	private CharacterContainer character;
	private DefaultMutableTreeNode topNode;
	
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
		scrollPane.setViewportView(tree);
		
	}
	
	private void initTree(){
		topNode.removeAllChildren();
            
		DefaultMutableTreeNode itemNode = null;
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
        			effectNode = new DefaultMutableTreeNode(new ThreadEffectInfo(a));
        			rankNode.add(effectNode);
        		}
        		i++;
        	}
        	topNode.add(itemNode);
        }
        tree.collapseRow(0);
        tree.expandRow(0);
        
	}
	
    private class ThreadItemInfo {
        public THREADITEMType threaditem;


        public ThreadItemInfo(THREADITEMType threaditem) {
        	this.threaditem = threaditem;
        }

        public String toString() {
        	return threaditem.getName() + " - " + threaditem.getDescription() ;
        }
    }
    
    private class ThreadRankInfo {
        public THREADRANKType threadrank;
        public int rank; 

        public ThreadRankInfo(int rank, THREADRANKType threadrank) {
        	this.threadrank = threadrank;
        	this.rank = rank;
        }

        public String toString() {
            return "Rank " + String.valueOf(rank) + ": " + threadrank.getEffect();
        }
    }
    
    private class ThreadEffectInfo{
    	public Object effect;
    	
    	
    	public ThreadEffectInfo(Object effect) {
			this.effect = effect;
		}


		public String toString() {
	    	String temp = new String("Effect not implemented!");
	    	if(effect.getClass() == ARMORType.class){
	    		temp = "Armor (Physical:" + ((ARMORType)effect).getPhysicalarmor() + ", Mystic" + + ((ARMORType)effect).getMysticarmor() + ")";
	    	}
	    	
	    	return temp;
        }
    }


}
