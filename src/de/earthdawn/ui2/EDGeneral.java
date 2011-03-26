package de.earthdawn.ui2;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEWorker;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.GenderType;
import de.earthdawn.data.NAMEGIVERABILITYType;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class EDGeneral extends JPanel {
	
	private CharacterContainer character;
	
	private JLabel lblCharactername;
	private JTextField textFieldName;
	private JLabel lblRace;
	private JComboBox comboBoxRace;
	private JLabel lblAge;
	private JLabel lblSize;
	private JLabel lblWeight;
	private JTextField textFieldAge;
	private JTextField textFieldSize;
	private JTextField textFieldWeight;
	private JLabel lblSex;
	private JLabel lblSkincolor;
	private JLabel lblEyecolor;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	private JLabel lblHaircolor;
	private JTextField textFieldSkincolor;
	private JTextField textFieldEyecolor;
	private JTextField textFieldHaircolor;

	/**
	 * Create the panel.
	 */
	public EDGeneral() {
		
		lblCharactername = new JLabel("Charactername");
		
		textFieldName = new JTextField();
		textFieldName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldName_caretUpdate(arg0);
			}
		});


		textFieldName.setColumns(10);
		
		lblRace = new JLabel("Race");
		
		comboBoxRace = new JComboBox();
		comboBoxRace.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_comboBoxRace_itemStateChanged(arg0);
			}
		});

		
		for (NAMEGIVERABILITYType n : ApplicationProperties.create().getNamegivers().getNAMEGIVER()) {
			comboBoxRace.addItem(n.getName());
		}
		
		lblAge = new JLabel("Age");
		
		lblSize = new JLabel("Size");
		
		lblWeight = new JLabel("Weight");
		
		textFieldAge = new JTextField();
		textFieldAge.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldAge_caretUpdate(arg0);
			}
		});
		textFieldAge.setColumns(10);
		
		textFieldSize = new JTextField();
		textFieldSize.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldSize_caretUpdate(arg0);
			}
		});
		textFieldSize.setColumns(10);
		
		textFieldWeight = new JTextField();
		textFieldWeight.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldWeight_caretUpdate(arg0);
			}
		});
		textFieldWeight.setColumns(10);
		
		lblSex = new JLabel("Sex");
		
		lblSkincolor = new JLabel("Skincolor");
		
		lblEyecolor = new JLabel("Eyecolor");
		
		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_rdbtnMale_itemStateChanged(arg0);
			}
		});
		
		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_rdbtnFemale_itemStateChanged(arg0);
			}
		});
		
		lblHaircolor = new JLabel("Haircolor");
		
		textFieldSkincolor = new JTextField();
		textFieldSkincolor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldSkincolor_caretUpdate(arg0);
			}
		});
		textFieldSkincolor.setColumns(10);
		
		textFieldEyecolor = new JTextField();
		textFieldEyecolor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldEyecolor_caretUpdate(arg0);
			}
		});
		textFieldEyecolor.setColumns(10);
		
		textFieldHaircolor = new JTextField();
		textFieldHaircolor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldHaircolor_caretUpdate(arg0);
			}
		});
		textFieldHaircolor.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCharactername)
						.addComponent(lblSex)
						.addComponent(lblAge)
						.addComponent(lblSize)
						.addComponent(lblWeight)
						.addComponent(lblRace))
					.addGap(6)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textFieldWeight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(rdbtnMale)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(rdbtnFemale)
								.addContainerGap(186, Short.MAX_VALUE))
							.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
											.addComponent(textFieldAge)
											.addComponent(textFieldSize))
										.addGap(30)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(lblSkincolor)
											.addComponent(lblEyecolor)
											.addComponent(lblHaircolor))
										.addGap(18)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(textFieldHaircolor, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
											.addComponent(textFieldEyecolor, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
											.addComponent(textFieldSkincolor, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.RELATED))
									.addComponent(textFieldName, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
									.addComponent(comboBoxRace, Alignment.LEADING, 0, 259, Short.MAX_VALUE))
								.addGap(29)))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCharactername)
						.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBoxRace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRace))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(lblSex))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(7)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(rdbtnMale)
								.addComponent(rdbtnFemale))))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblAge)
							.addGap(14)
							.addComponent(lblSize))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldAge, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSkincolor)
								.addComponent(textFieldSkincolor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblEyecolor)
								.addComponent(textFieldEyecolor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldWeight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblWeight)
								.addComponent(lblHaircolor)
								.addComponent(textFieldHaircolor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(190))
		);
		setLayout(groupLayout);

	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		textFieldName.setText(character.getName());
		textFieldAge.setText(new Integer(character.getAppearance().getAge()).toString());
		textFieldSize.setText(new Integer(character.getAppearance().getHeight()).toString());
		textFieldWeight.setText(new Integer(character.getAppearance().getWeight()).toString());
		
		textFieldSkincolor.setText(character.getAppearance().getSkin());
		textFieldEyecolor.setText(character.getAppearance().getEyes());
		textFieldHaircolor.setText(character.getAppearance().getHair());
		
		if(character.getAppearance().getGender().value().equals("male")){
	    	rdbtnMale.getModel().setSelected(true);
		}
		else{
			rdbtnFemale.getModel().setSelected(true);
		}
			
		
		
		comboBoxRace.setSelectedItem(character.getAppearance().getRace());
	}

	public CharacterContainer getCharacter() {
		return character;
	}

	protected void do_comboBoxRace_itemStateChanged(ItemEvent arg0) {
		if(character != null){
			if(arg0.getStateChange() == 1) {
				character.getAppearance().setRace((String)arg0.getItem());
				ECEWorker worker = new ECEWorker();
			    worker.verarbeiteCharakter(character.getEDCHARACTER());
			}
		}
	}
	
	
	protected void do_rdbtnMale_itemStateChanged(ItemEvent arg0) {
		rdbtnFemale.getModel().setSelected(!((JRadioButton)arg0.getItem()).getModel().isSelected());
	}
	protected void do_rdbtnFemale_itemStateChanged(ItemEvent arg0) {
		rdbtnMale.getModel().setSelected(!((JRadioButton)arg0.getItem()).getModel().isSelected());
		if(character != null){
		    if (((JRadioButton)arg0.getItem()).getModel().isSelected()) 
		    {
		    	character.getAppearance().setGender(GenderType.fromValue("female"));

		    }
		    else{
		    	character.getAppearance().setGender(GenderType.fromValue("male"));

		    }
			ECEWorker worker = new ECEWorker();
		    worker.verarbeiteCharakter(character.getEDCHARACTER());		
		}
	}



	protected void do_textFieldName_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getEDCHARACTER().setName(textFieldName.getText());
		}
	}

	protected int textToInt(String text) {
		Integer zahl=null;
		try {
			zahl = new Integer(text);
		}
		catch(NumberFormatException e) {
			// Don't Care
			return 0;
		}
		if( zahl == null ) return 0;
		return zahl.intValue();
	}

	protected void do_textFieldAge_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getAppearance().setAge(textToInt(textFieldAge.getText()));
		}
	}
	
	protected void do_textFieldSize_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getAppearance().setHeight(textToInt(textFieldSize.getText()));
		}
	}
	
	protected void do_textFieldWeight_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getAppearance().setWeight(textToInt(textFieldWeight.getText()));
		}
	}
	
	protected void do_textFieldSkincolor_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getAppearance().setSkin(textFieldSkincolor.getText());
		}
	}
	
	protected void do_textFieldEyecolor_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getAppearance().setEyes(textFieldEyecolor.getText());
		}
	}
	
	protected void do_textFieldHaircolor_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getAppearance().setHair(textFieldHaircolor.getText());
		}
	}
}
