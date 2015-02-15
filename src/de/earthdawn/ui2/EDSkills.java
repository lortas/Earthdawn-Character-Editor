package de.earthdawn.ui2;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import de.earthdawn.CharacterContainer;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.LAYOUTSIZESType;
import de.earthdawn.data.OPTIONALRULEType;
import de.earthdawn.data.RANKType;
import de.earthdawn.data.SKILLType;
import de.earthdawn.data.YesnoType;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.ListSelectionModel;

public class EDSkills extends JPanel {

	private static final long serialVersionUID = -8901862501177055512L;
	public static final ApplicationProperties PROPERTIES=ApplicationProperties.create();
	private CharacterContainer character;
	private JToolBar toolBar;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnAddSkill;
	private JButton btnRemoveSkill;
	private JButton btnToggleDefaultSkill;
	private BufferedImage backgroundimage = null;

	@Override
	protected void paintComponent(Graphics g) {
		if( backgroundimage == null ) {
			File file = new File("images/background/skills.jpg");
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
	public EDSkills() {
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));

		toolBar = new JToolBar();
		toolBar.setOpaque(false);
		add(toolBar, BorderLayout.NORTH);

		btnAddSkill = new JButton("Add Skill");
		btnAddSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnAddSkill_actionPerformed(arg0);
			}
		});
		toolBar.add(btnAddSkill);

		btnRemoveSkill = new JButton("Remove Skill");
		btnRemoveSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnRemoveSkill_actionPerformed(arg0);
			}
		});
		toolBar.add(btnRemoveSkill);

		btnToggleDefaultSkill = new JButton("Toggle Default Skills");
		btnToggleDefaultSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnToggleDefaultSkill_actionPerformed(arg0);
			}
		});
		toolBar.add(btnToggleDefaultSkill);

		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable(){
			private static final long serialVersionUID = -3036298052577825970L;
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
			{
				Component component = super.prepareRenderer(renderer, row, column);
				if( component instanceof JComponent )
					((JComponent)component).setOpaque(false);
				return component;
			}
		};
		table.setOpaque(false);
		InputMapUtil.setupInputMap(table);	

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new SkillsTableModel(character,false));
		JComboBox<Integer> comboBoxZeroToTen = new JComboBox<Integer>();
		for(int i=0; i<=10;i++) comboBoxZeroToTen.addItem(i);
		JComboBox<Integer> comboBoxZeroToSix = new JComboBox<Integer>();
		for(int i=0; i<=6;i++) comboBoxZeroToSix.addItem(i);
		table.getColumnModel().getColumn(3).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToSix));
		table.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToTen));
		scrollPane.setViewportView(table);
		scrollPane.getViewport().setOpaque(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
	}

	public void setCharacter(final CharacterContainer character) {
		this.character = character;
		((SkillsTableModel)table.getModel()).setCharacter(character);
		JComboBox<Integer> comboBoxZeroToTen = new JComboBox<Integer>();
		for(int i=0; i<=10;i++) comboBoxZeroToTen.addItem(i);
		JComboBox<Integer> comboBoxZeroToSix = new JComboBox<Integer>();
		for(int i=0; i<=6;i++) comboBoxZeroToSix.addItem(i);
		table.getColumnModel().getColumn(3).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToSix));
		table.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(comboBoxZeroToTen));
		try {
			int c=0;
			for( LAYOUTSIZESType width : PROPERTIES.getGuiLayoutTabel("skillselection") ) {
				TableColumn col = table.getColumnModel().getColumn(c);
				if( width.getMin() != null ) col.setMinWidth(width.getMin());
				if( width.getMax() != null ) col.setMaxWidth(width.getMax());
				if( width.getPreferred() != null ) col.setPreferredWidth(width.getPreferred());
				c++;
			}
		} catch(IndexOutOfBoundsException e) {
			System.err.println("layout spellselection : "+e.getLocalizedMessage());
		}
	}

	public CharacterContainer getCharacter() {
		return character;
	}

	protected void do_btnAddSkill_actionPerformed(ActionEvent arg0) {
		EDCapabilitySelectDialog dialog = new EDCapabilitySelectDialog(EDCapabilitySelectDialog.SELECT_SKILLS,0,character.getSkills(),new Rectangle(100, 100, 450, 300) );
		dialog.setVisible(true);
		Map<String, SKILLType> selected = dialog.getSelectedCapabilitytMap();
		for(Object key : selected.keySet()){
			SKILLType cap = selected.get(key);
			SKILLType skill = new SKILLType();
			skill.setAction(cap.getAction());
			skill.setAttribute(cap.getAttribute());
			skill.setDefault(cap.getDefault());
			skill.setStrain(cap.getStrain());
			skill.setName(cap.getName());
			skill.getLIMITATION().addAll(cap.getLIMITATION());
			RANKType rank = new RANKType();
			rank.setRank(1);
			skill.setRANK(rank);
			character.addSkill(skill);
			character.refesh();
		}
	}

	protected void do_btnRemoveSkill_actionPerformed(ActionEvent arg0) {
		ArrayList<SKILLType> skillsForRemoval = new ArrayList<SKILLType> ();
		for(int row :table.getSelectedRows()){
			SKILLType skill = character.getSkills().get(row);
			skillsForRemoval.add(skill);
		}
		character.removeSkill(skillsForRemoval);
		character.refesh();
	}

	protected void do_btnToggleDefaultSkill_actionPerformed(ActionEvent arg0) {
		OPTIONALRULEType showdefaultskills = PROPERTIES.getOptionalRules().getSHOWDEFAULTSKILLS();
		YesnoType used = showdefaultskills.getUsed();
		if( used.equals(YesnoType.YES) ) {
			showdefaultskills.setUsed(YesnoType.NO);
			btnToggleDefaultSkill.setText("Show Default Skills");
		}
		else {
			showdefaultskills.setUsed(YesnoType.YES);
			btnToggleDefaultSkill.setText("Hide Default Skills");
		}
		character.refesh();
	}
}
