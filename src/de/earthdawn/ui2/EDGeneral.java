package de.earthdawn.ui2;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEWorker;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.APPEARANCEType;
import de.earthdawn.data.GenderType;
import de.earthdawn.data.NAMEGIVERABILITYType;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.awt.Graphics;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JScrollPane;

public class EDGeneral extends JPanel {
	private static final long serialVersionUID = 3353372429516944708L;
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
	private static final String backgroundImage="templates/genralpanel_background.jpg";
	private NumberFormat numberformat = new DecimalFormat("0.00");
	private JTextArea charComment;
	private JTextArea charDescription;
	private JLabel lblRaceAbilities;
	private JTextField txtRaceabilities;
	private JRadioButton rdbtnNoGender;

	/**
	 * Create the panel.
	 */
	public EDGeneral() {
		setOpaque(false);
		JLabel lblSizeMeasure = new JLabel("ft");
		JLabel lblWeigtmeasure = new JLabel("lb");
		setLayout(new MigLayout("", "[110px][150px,grow][15px][364.00px,grow,fill]", "[20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][][]"));
		lblCharactername = new JLabel("Charactername");
		add(lblCharactername, "cell 0 0,alignx left,aligny center");

		textFieldName = new JTextField();
		textFieldName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldName_caretUpdate(arg0);
			}
		});


		textFieldName.setColumns(10);
		textFieldName.setOpaque(false);
		add(textFieldName, "cell 1 0 2 1,growx,aligny top");

		lblRace = new JLabel("Race");
		add(lblRace, "cell 0 1,alignx left,aligny center");

		comboBoxRace = new JComboBox();
		comboBoxRace.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_comboBoxRace_itemStateChanged(arg0);
			}
		});
		comboBoxRace.setOpaque(false);
		add(comboBoxRace, "cell 1 1 2 1,growx,aligny top");
		for (NAMEGIVERABILITYType n : ApplicationProperties.create().getNamegivers()) {
			comboBoxRace.addItem(n.getName());
		}

		JScrollPane charDescriptionPanel = new JScrollPane();
		charDescriptionPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Description", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		charDescriptionPanel.setOpaque(false);
		add(charDescriptionPanel, "cell 3 0 1 6,grow");

		charDescription = new JTextArea();
		charDescription.setLineWrap(true);
		charDescription.setOpaque(false);
		charDescription.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_charDescription_caretUpdate(arg0);
			}
		});
		charDescriptionPanel.setViewportView(charDescription);
		charDescriptionPanel.getViewport().setOpaque(false);

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_rdbtnSex_itemStateChanged(arg0);
			}
		});
		rdbtnFemale.setOpaque(false);
		add(rdbtnFemale, "cell 1 3,alignx left,aligny center");
		
		rdbtnNoGender = new JRadioButton("No Gender");
		rdbtnNoGender.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_rdbtnSex_itemStateChanged(arg0);
			}
		});
		rdbtnNoGender.setOpaque(false);
		add(rdbtnNoGender, "cell 1 4,alignx left,aligny center");

		JScrollPane charCommentPanel = new JScrollPane();
		charCommentPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Comment", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		charCommentPanel.setOpaque(false);
		add(charCommentPanel, "cell 3 6 1 5,grow");

		charComment = new JTextArea();
		charComment.setLineWrap(true);
		charComment.setOpaque(false);
		charComment.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_charComment_caretUpdate(arg0);
			}
		});
		charCommentPanel.setViewportView(charComment);
		charCommentPanel.getViewport().setOpaque(false);
		charCommentPanel.setOpaque(false);

		lblSex = new JLabel("Sex");
		add(lblSex, "cell 0 2,alignx left,aligny center");

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_rdbtnSex_itemStateChanged(arg0);
			}
		});
		rdbtnMale.setOpaque(false);
		add(rdbtnMale, "cell 1 2,alignx left,aligny center");

		lblAge = new JLabel("Age");
		add(lblAge, "cell 0 5,alignx left,aligny center");

		textFieldAge = new JTextField();
		textFieldAge.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldAge_caretUpdate(arg0);
			}
		});
		textFieldAge.setColumns(10);
		textFieldAge.setOpaque(false);
		add(textFieldAge, "cell 1 5 2 1,growx,aligny top");

		lblSize = new JLabel("Size");
		add(lblSize, "cell 0 6,alignx left,aligny center");

		textFieldSize = new JTextField();
		textFieldSize.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldSize_caretUpdate(arg0);
			}
		});
		textFieldSize.setColumns(10);
		textFieldSize.setOpaque(false);
		add(textFieldSize, "cell 1 6,growx,aligny top");

		lblWeight = new JLabel("Weight");
		add(lblWeight, "cell 0 7,alignx left,aligny center");

		textFieldWeight = new JTextField();
		textFieldWeight.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldWeight_caretUpdate(arg0);
			}
		});
		textFieldWeight.setColumns(10);
		textFieldWeight.setOpaque(false);
		add(textFieldWeight, "cell 1 7,growx,aligny top");

		lblSkincolor = new JLabel("Skincolor");
		add(lblSkincolor, "cell 0 8,alignx left,aligny center");
		add(lblSizeMeasure, "cell 2 6,alignx left,aligny center");
		add(lblWeigtmeasure, "cell 2 7,alignx left,aligny center");

		textFieldSkincolor = new JTextField();
		textFieldSkincolor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldSkincolor_caretUpdate(arg0);
			}
		});
		textFieldSkincolor.setColumns(10);
		textFieldSkincolor.setOpaque(false);
		add(textFieldSkincolor, "cell 1 8 2 1,growx,aligny top");

		lblEyecolor = new JLabel("Eyecolor");
		add(lblEyecolor, "cell 0 9,alignx left,aligny center");

		textFieldEyecolor = new JTextField();
		textFieldEyecolor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldEyecolor_caretUpdate(arg0);
			}
		});
		textFieldEyecolor.setColumns(10);
		textFieldEyecolor.setOpaque(false);
		add(textFieldEyecolor, "cell 1 9 2 1,growx,aligny top");

		lblHaircolor = new JLabel("Haircolor");
		add(lblHaircolor, "cell 0 10,alignx left,aligny center");

		textFieldHaircolor = new JTextField();
		textFieldHaircolor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldHaircolor_caretUpdate(arg0);
			}
		});
		textFieldHaircolor.setColumns(10);
		textFieldHaircolor.setOpaque(false);
		add(textFieldHaircolor, "cell 1 10 2 1,growx,aligny top");

		lblRaceAbilities = new JLabel("Race Abilities");
		add(lblRaceAbilities, "cell 0 11,alignx left,aligny center");

		txtRaceabilities = new JTextField();
		txtRaceabilities.setEditable(false);
		add(txtRaceabilities, "cell 1 11 3 1,growx,aligny center");
		txtRaceabilities.setColumns(10);
		txtRaceabilities.setOpaque(false);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnMale);
		group.add(rdbtnFemale);
		group.add(rdbtnNoGender);
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		textFieldName.setText(character.getName());
		textFieldAge.setText(new Integer(character.getAppearance().getAge()).toString());
		textFieldSize.setText(numberformat.format(character.getAppearance().getHeight()));
		textFieldWeight.setText(numberformat.format(character.getAppearance().getWeight()));
		textFieldSkincolor.setText(character.getAppearance().getSkin());
		textFieldEyecolor.setText(character.getAppearance().getEyes());
		textFieldHaircolor.setText(character.getAppearance().getHair());
		charDescription.setText(character.getDESCRIPTION());
		charComment.setText(character.getCOMMENT());
		txtRaceabilities.setText(character.getAbilities());

		if(character.getAppearance().getGender().equals(GenderType.MALE)){
			rdbtnMale.getModel().setSelected(true);
		} else if(character.getAppearance().getGender().equals(GenderType.FEMALE)){
			rdbtnFemale.getModel().setSelected(true);
		} else {
			rdbtnNoGender.getModel().setSelected(true);
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
				txtRaceabilities.setText(character.getAbilities());
				character.refesh();
			}
		}
	}

	protected void do_rdbtnSex_itemStateChanged(ItemEvent arg0) {
		if( character == null ) return;
		JRadioButton radioButton = (JRadioButton)arg0.getItem();
		if( radioButton == null ) return;
		GenderType gender = GenderType.MINUS;
		if( radioButton.equals(rdbtnFemale)) gender = GenderType.FEMALE;
		if( radioButton.equals(rdbtnMale)) gender = GenderType.MALE;
		APPEARANCEType appearance = character.getAppearance();
		if( appearance.getGender().equals(gender) ) return;
		appearance.setGender(gender);
		ECEWorker worker = new ECEWorker();
		worker.verarbeiteCharakter(character.getEDCHARACTER());		
		character.refesh();
	}

	protected void do_textFieldName_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getEDCHARACTER().setName(textFieldName.getText());
		}
	}

	protected int textToInt(String text) {
		if( text == null ) return 0;
		text=text.trim().replaceAll("[^0-9]", "");
		if( text.length() == 0 ) return 0;
		Integer zahl=null;
		try {
			zahl = new Integer(text);
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		if( zahl == null ) return 0;
		return zahl.intValue();
	}

	protected float textToFloat(String text) {
		if( text == null ) return 0;
		text=text.trim();
		if( text.length() == 0 ) return 0;
		Number zahl=null;
		try {
			zahl = numberformat.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if( zahl == null ) return 0;
		return zahl.floatValue();
	}

	protected void do_textFieldAge_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getAppearance().setAge(textToInt(textFieldAge.getText()));
		}
	}

	protected void do_textFieldSize_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getAppearance().setHeight(textToFloat(textFieldSize.getText()));
		}
	}

	protected void do_textFieldWeight_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getAppearance().setWeight(textToFloat(textFieldWeight.getText()));
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
	protected void do_charDescription_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.setDESCRIPTION(charDescription.getText());
		}
	}

	protected void do_charComment_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.setCOMMENT(charComment.getText());
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
			BufferedImage image = ImageIO.read(new File(backgroundImage));
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.paintComponent(g);
	}
}
