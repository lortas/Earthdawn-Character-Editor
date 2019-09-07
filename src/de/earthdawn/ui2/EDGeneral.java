package de.earthdawn.ui2;

import de.earthdawn.CharacterContainer;
import de.earthdawn.ECEWorker;
import de.earthdawn.NamegiverComparator;
import de.earthdawn.config.ApplicationProperties;
import de.earthdawn.data.APPEARANCEType;
import de.earthdawn.data.Base64BinaryType;
import de.earthdawn.data.GenderType;
import de.earthdawn.data.NAMEGIVERABILITYType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class EDGeneral extends JPanel {
	private static final long serialVersionUID = 3353372429516944708L;
	private static final String backgroundImage="images/background/genralpanel.jpg";
	private CharacterContainer character;
	private JTextField textFieldName;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	private JTextField textFieldSkincolor;
	private JTextField textFieldEyecolor;
	private JTextField textFieldHaircolor;
	private JTextArea charComment;
	private JTextArea charDescription;
	private JTextArea txtRaceabilities;
	private JRadioButton rdbtnNoGender;
	private JSpinner spinnerDamage;
	private JSpinner spinnerNormalWounds;
	private JSpinner spinnerBloodWounds;
	private JSpinner spinnerSize;
	private JSpinner spinnerWeight;
	private JSpinner spinnerAge;
	private JTextField textFieldPlayer;
	private JTextField textFieldBirth;
	private JLabel labelSize;
	private JLabel labelWeight;
	private JPanel pnlPortrait;
	private JLabel lblPortrait;
	private JButton btnRace;
	private JPopupMenu popupMenuRace;

	/**
	 * Create the panel.
	 */
	public EDGeneral() {
		setOpaque(false);
		setLayout(new MigLayout("", "[150px:150px:150px][50px:70px:80px][50px:100px:100px][200px:200px:500px,grow][230px:230px:230px]", "[20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px]"));

		add(new JLabel("Playername"), "cell 0 0,alignx right,aligny center");
		textFieldPlayer = new JTextField();
		textFieldPlayer.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldPlayer_caretUpdate(arg0);
			}
		});
		textFieldPlayer.setColumns(10);
		textFieldPlayer.setOpaque(false);
		add(textFieldPlayer, "cell 1 0 2 1,growx,aligny center");

		add(new JLabel("Charactername"), "cell 0 1,alignx right,aligny center");
		textFieldName = new JTextField();
		textFieldName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldName_caretUpdate(arg0);
			}
		});
		textFieldName.setColumns(10);
		textFieldName.setOpaque(false);
		add(textFieldName, "cell 1 1 2 1,growx,aligny center");

		add(new JLabel("Race"), "cell 0 2,alignx right,aligny center");
		btnRace = new JButton("Change Race");
		add(btnRace, "cell 1 2 2 1,grow");
		btnRace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnChangeRace_actionPerformed(arg0);
			}
		});
		btnRace.setOpaque(false);
		btnRace.setContentAreaFilled(false);

		popupMenuRace = new JPopupMenu();
		Map<String, Map<String, List<NAMEGIVERABILITYType>>> namegivers = ApplicationProperties.create().getNamgiversByType();
		for( String namegiversorigin : new TreeSet<String>(namegivers.keySet()) ) {
			JMenu menuRace = new JMenu(namegiversorigin);
			popupMenuRace.add(menuRace);
			Map<String, List<NAMEGIVERABILITYType>> namegiverByOrigin = namegivers.get(namegiversorigin);
			for( String namegiverstype : new TreeSet<String>(namegiverByOrigin.keySet()) ) {
				List<NAMEGIVERABILITYType> namegiverList = namegiverByOrigin.get(namegiverstype);
				Collections.sort(namegiverList, new NamegiverComparator());
				JMenu menu;
				if( namegiverList.size() == 1 ) menu = menuRace;
				else {
					menu = new JMenu(namegiverstype);
					menuRace.add(menu);
				}
				for( NAMEGIVERABILITYType n : namegiverList ) {
					JMenuItem menuItem = new JMenuItem(n.getName());
					menuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							JMenuItem race = (JMenuItem)arg0.getSource();
							Component racetype = ((JPopupMenu)race.getParent()).getInvoker();
							if( (racetype==null) || !(racetype instanceof JMenu) ) {
								do_Race_Changed(race.getText(),"");
								return;
							}
							Component raceorigin = ((JPopupMenu)racetype.getParent()).getInvoker();
							if( (raceorigin==null) || !(raceorigin instanceof JMenu) ) {
								do_Race_Changed(race.getText(),((JMenu)racetype).getText());
								return;
							}
							do_Race_Changed(race.getText(),((JMenu)raceorigin).getText());
						}
					});
					menu.add(menuItem);
				}
			}
		}

		JScrollPane charDescriptionPanel = new JScrollPane();
		charDescriptionPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Description", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		charDescriptionPanel.setOpaque(false);
		add(charDescriptionPanel, "cell 3 0 2 7,grow");

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
		add(rdbtnFemale, "cell 1 4,alignx left,aligny center");

		rdbtnNoGender = new JRadioButton("No Gender");
		rdbtnNoGender.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_rdbtnSex_itemStateChanged(arg0);
			}
		});
		rdbtnNoGender.setOpaque(false);
		add(rdbtnNoGender, "cell 1 5,alignx left,aligny center");

		add(new JLabel("Birth"), "cell 0 6,alignx trailing,aligny center");
		textFieldBirth = new JTextField();
		textFieldBirth.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldBirth_caretUpdate(arg0);
			}
		});
		textFieldBirth.setOpaque(false);
		textFieldBirth.setColumns(10);
		add(textFieldBirth, "cell 1 6 2 1,growx,aligny center");

		JScrollPane charCommentPanel = new JScrollPane();
		charCommentPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Comment", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		charCommentPanel.setOpaque(false);
		add(charCommentPanel, "cell 3 7 1 6,grow");

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

		add(new JLabel("Sex"), "cell 0 3,alignx right,aligny center");

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				do_rdbtnSex_itemStateChanged(arg0);
			}
		});
		rdbtnMale.setOpaque(false);
		add(rdbtnMale, "cell 1 3,alignx left,aligny center");

		pnlPortrait = new JPanel();
		pnlPortrait.setBorder(new TitledBorder(null, "Portrait", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlPortrait.setOpaque(false);
		add(pnlPortrait, "cell 4 7 1 9,alignx center,aligny center");

		lblPortrait = new JLabel();
		pnlPortrait.add(lblPortrait);
		lblPortrait.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if( e.getButton() == 1 ) do_updatePortrait();
				else setEmptyPortrait();
			}
		});
		lblPortrait.setVerticalAlignment(JLabel.BOTTOM);
		lblPortrait.setHorizontalAlignment(JLabel.CENTER);
		lblPortrait.setOpaque(false);

		add(new JLabel("Skincolor"), "cell 0 10,alignx right,aligny center");
		textFieldSkincolor = new JTextField();
		textFieldSkincolor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldSkincolor_caretUpdate(arg0);
			}
		});
		textFieldSkincolor.setColumns(10);
		textFieldSkincolor.setOpaque(false);
		add(textFieldSkincolor, "cell 1 10 2 1,growx,aligny center");

		add(new JLabel("Eyecolor"), "cell 0 11,alignx right,aligny center");
		textFieldEyecolor = new JTextField();
		textFieldEyecolor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldEyecolor_caretUpdate(arg0);
			}
		});
		textFieldEyecolor.setColumns(10);
		textFieldEyecolor.setOpaque(false);
		add(textFieldEyecolor, "cell 1 11 2 1,growx,aligny center");

		add(new JLabel("Haircolor"), "cell 0 12,alignx right,aligny center");
		textFieldHaircolor = new JTextField();
		textFieldHaircolor.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_textFieldHaircolor_caretUpdate(arg0);
			}
		});
		textFieldHaircolor.setColumns(10);
		textFieldHaircolor.setOpaque(false);
		add(textFieldHaircolor, "cell 1 12 2 1,growx,aligny center");

		add(new JLabel("Size"), "cell 0 8,alignx right,aligny center");
		labelSize = new JLabel("feet");
		add(labelSize, "cell 2 8,alignx center,aligny center");
		spinnerSize = new JSpinner(new SpinnerNumberModel(0f, 0, 1000, 0.1));
		spinnerSize.setOpaque(false);
		spinnerSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				do_spinnerSize_stateChanged(arg0);
			}
		});
		add(spinnerSize, "cell 1 8,alignx left,aligny center");

		labelWeight=new JLabel("pound");
		add(new JLabel("Weight"), "cell 0 9,alignx right,aligny center");
		add(labelWeight, "cell 2 9,alignx center,aligny center");
		spinnerWeight = new JSpinner(new SpinnerNumberModel(0f, 0, 1000, 0.1));
		spinnerWeight.setOpaque(false);
		spinnerWeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				do_spinnerWeight_stateChanged(arg0);
			}
		});
		add(spinnerWeight, "cell 1 9,alignx left,aligny center");

		add(new JLabel("Damage"), "cell 0 13,alignx right,aligny center");
		spinnerDamage = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		spinnerDamage.setOpaque(false);
		spinnerDamage.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				do_spinnerDamage_stateChanged(arg0);
			}
		});
		add(spinnerDamage, "cell 1 13,alignx left,aligny center");

		add(new JLabel("NormalWounds"), "cell 0 14,alignx right,aligny center");
		spinnerNormalWounds = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		spinnerNormalWounds.setOpaque(false);
		spinnerNormalWounds.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				do_spinnerNormalWounds_stateChanged(arg0);
			}
		});
		add(spinnerNormalWounds, "cell 1 14,alignx left,aligny center");

		add(new JLabel("BloodWounds"), "cell 0 15,alignx right,aligny center");
		spinnerBloodWounds = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		spinnerBloodWounds.setOpaque(false);
		spinnerBloodWounds.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				do_spinnerBloodWounds_stateChanged(arg0);
			}
		});
		add(spinnerBloodWounds, "cell 1 15,alignx left,aligny center");

		add(new JLabel("Age"), "cell 0 7,alignx right,aligny center");
		add(new JLabel("year"), "cell 2 7,alignx left,aligny center");
		spinnerAge = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
		spinnerAge.setOpaque(false);
		spinnerAge.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				do_spinnerAge_stateChanged(arg0);
			}
		});
		add(spinnerAge, "cell 1 7,alignx left,aligny center");

		txtRaceabilities = new JTextArea();
		txtRaceabilities.setLineWrap(true);
		txtRaceabilities.setOpaque(false);
		txtRaceabilities.setEditable(false);
		JScrollPane panelRaceabilities = new JScrollPane();
		panelRaceabilities.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Race Abilities", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panelRaceabilities.setOpaque(false);
		panelRaceabilities.setViewportView(txtRaceabilities);
		panelRaceabilities.getViewport().setOpaque(false);
		add(panelRaceabilities, "cell 2 13 2 3,grow");

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnMale);
		group.add(rdbtnFemale);
		group.add(rdbtnNoGender);
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		textFieldPlayer.setText(character.getPlayer());
		textFieldName.setText(character.getName());
		textFieldBirth.setText(character.getAppearance().getBirth());
		APPEARANCEType appearance = character.getAppearance();
		spinnerAge.setValue(appearance.getAge());
		spinnerSize.setValue(appearance.getHeight());
		spinnerWeight.setValue(appearance.getWeight());
		textFieldSkincolor.setText(appearance.getSkin());
		textFieldEyecolor.setText(appearance.getEyes());
		textFieldHaircolor.setText(appearance.getHair());
		spinnerDamage.setValue(character.getHealth().getDamage());
		spinnerNormalWounds.setValue(character.getWound().getNormal());
		spinnerBloodWounds.setValue(character.getWound().getBlood());
		charDescription.setText(character.getDESCRIPTION());
		charComment.setText(character.getCOMMENT());
		txtRaceabilities.setText(character.getAbilities());
		String origin = appearance.getOrigin();
		if( origin.isEmpty() ) btnRace.setText(appearance.getRace());
		else btnRace.setText(appearance.getRace()+" ("+origin+")");

		if(appearance.getGender().equals(GenderType.MALE)){
			rdbtnMale.getModel().setSelected(true);
		} else if(appearance.getGender().equals(GenderType.FEMALE)){
			rdbtnFemale.getModel().setSelected(true);
		} else {
			rdbtnNoGender.getModel().setSelected(true);
		}

		List<Base64BinaryType> potraits = character.getPortrait();
		if( ! potraits.isEmpty() ) {
			ImageIcon icon = new ImageIcon(potraits.get(0).getValue());
			Image image = icon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH);
			lblPortrait.setIcon(new ImageIcon(image));
		}
	}

	public CharacterContainer getCharacter() {
		return character;
	}
	protected void do_Race_Changed(String race,String origin) {
		if( character == null ) return;
		APPEARANCEType appearance = character.getAppearance();
		if( race.equals(appearance.getRace()) && origin.equals(appearance.getOrigin())) return;
		if( origin.isEmpty() ) btnRace.setText(race);
		else btnRace.setText(race+" ("+origin+")");
		appearance.setRace(race);
		appearance.setOrigin(origin);
		int a = JOptionPane.showOptionDialog(this,
				EDMainWindow.NLS.getString("Confirmation.ResetPortraitAndLanguages.text"),
				EDMainWindow.NLS.getString("Confirmation.ResetPortraitAndLanguages.title"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				EDMainWindow.OptionDialog_YesNoOptions,
				EDMainWindow.OptionDialog_YesNoOptions[0]);
		if( a == 0 ) {
			character.getPortrait().clear();
			character.clearLanguages();
		}
		new ECEWorker(character).verarbeiteCharakter();
		character.refesh();
	}

	protected void do_rdbtnSex_itemStateChanged(ItemEvent arg0) {
		if( character == null ) return;
		if( arg0.getStateChange() != ItemEvent.SELECTED ) return;
		JRadioButton radioButton = (JRadioButton)arg0.getItem();
		if( radioButton == null ) return;
		GenderType gender = GenderType.MINUS;
		if( radioButton.equals(rdbtnFemale)) gender = GenderType.FEMALE;
		if( radioButton.equals(rdbtnMale)) gender = GenderType.MALE;
		APPEARANCEType appearance = character.getAppearance();
		if( appearance.getGender().equals(gender) ) return;
		appearance.setGender(gender);
		int a = JOptionPane.showOptionDialog(this,
				EDMainWindow.NLS.getString("Confirmation.ChangePortrait.text"),
				EDMainWindow.NLS.getString("Confirmation.ChangePortrait.title"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				EDMainWindow.OptionDialog_YesNoOptions,
				EDMainWindow.OptionDialog_YesNoOptions[0]);
		if( a == 0 ) character.getPortrait().clear();
		new ECEWorker(character).verarbeiteCharakter();
		character.refesh();
	}

	protected void do_textFieldBirth_caretUpdate(CaretEvent arg0) {
		if( character == null ) return;
		character.getAppearance().setBirth(textFieldBirth.getText());
	}

	protected void do_textFieldPlayer_caretUpdate(CaretEvent arg0) {
		if( character == null ) return;
		character.getEDCHARACTER().setPlayer(textFieldPlayer.getText());
	}

	protected void do_textFieldName_caretUpdate(CaretEvent arg0) {
		if(character != null){
			character.getEDCHARACTER().setName(textFieldName.getText());
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

	protected void do_spinnerDamage_stateChanged(ChangeEvent arg0) {
		if( character == null ) return;
		character.getHealth().setDamage((Integer)spinnerDamage.getValue());
		character.refesh();
	}

	protected void do_spinnerNormalWounds_stateChanged(ChangeEvent arg0) {
		if( character == null ) return;
		character.getWound().setNormal((Integer)spinnerNormalWounds.getValue());
		character.refesh();
	}

	protected void do_spinnerBloodWounds_stateChanged(ChangeEvent arg0) {
		if( character == null ) return;
		character.getWound().setBlood((Integer)spinnerBloodWounds.getValue());
		character.refesh();
	}

	protected void do_spinnerWeight_stateChanged(ChangeEvent arg0) {
		if( character == null ) return;
		Object value = spinnerWeight.getValue();
		if( value instanceof Double ) {
			character.getAppearance().setWeight(((Double)value).floatValue());
		} else if( value instanceof Float ) {
			character.getAppearance().setWeight(((Float)value).floatValue());
		} else if( value instanceof Integer ) {
			character.getAppearance().setWeight(((Integer)value).floatValue());
		} else {
			System.err.println("Unexpected object type for spinnerWeight value: "+value.getClass().getName());
		}
		character.getAppearance().setWeightString( ApplicationProperties.create().getUnitCalculator().formatWeight( character.getAppearance().getWeight() ) );
		labelWeight.setText( character.getAppearance().getWeightString() );
	}

	protected void do_spinnerSize_stateChanged(ChangeEvent arg0) {
		if( character == null ) return;
		Object value = spinnerSize.getValue();
		if( value instanceof Double ) {
			character.getAppearance().setHeight(((Double)value).floatValue());
		} else if( value instanceof Float ) {
			character.getAppearance().setHeight(((Float)value).floatValue());
		} else if( value instanceof Integer ) {
			character.getAppearance().setHeight(((Integer)value).floatValue());
		} else {
			System.err.println("Unexpected object type for sinnerSize value: "+value.getClass().getName());
		}
		character.getAppearance().setHeightString( ApplicationProperties.create().getUnitCalculator().formatLength( character.getAppearance().getHeight(), -1 ) );
		labelSize.setText( character.getAppearance().getHeightString() );
	}

	protected void do_spinnerAge_stateChanged(ChangeEvent arg0) {
		if( character == null ) return;
		int age = (Integer)spinnerAge.getValue();
		character.getAppearance().setAge(age);
		character.refesh();
	}

	protected void do_updatePortrait() {
		JFileChooser fc = new JFileChooser(new File("images/character"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG-GIF-PNG Images", "jpg", "gif", "png", "jpeg");
		fc.setFileFilter(filter);
		ImagePreview imagepreview = new ImagePreview(200,200);
		fc.setAccessory(imagepreview);
		fc.addPropertyChangeListener(imagepreview);
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				fileInputStream.read(data);
				fileInputStream.close();
				Base64BinaryType base64bin = new Base64BinaryType();
				base64bin.setValue(data);
				final String[] filename = file.getName().split("\\.");
				base64bin.setContenttype("image/"+filename[filename.length-1]);
				character.getPortrait().set(0, base64bin);
				ImageIcon icon = new ImageIcon(data);
				Image image = icon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH);
				lblPortrait.setIcon(new ImageIcon(image));
			} catch (FileNotFoundException e1) {
				System.err.println(e1.getLocalizedMessage());
			} catch (IOException e2) {
				System.err.println(e2.getLocalizedMessage());
			}
		}
	}

	protected void setEmptyPortrait() {
		int a = JOptionPane.showOptionDialog(this,
				EDMainWindow.NLS.getString("Confirmation.EmptyPortrait.text"),
				EDMainWindow.NLS.getString("Confirmation.EmptyPortrait.title"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				EDMainWindow.OptionDialog_YesNoOptions,
				EDMainWindow.OptionDialog_YesNoOptions[0]);
		if( a != 0 ) return;
		Base64BinaryType base64bin = new Base64BinaryType();
		base64bin.setValue(new byte[]{-119,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82,0,0,0,1,
				0,0,0,1,1,3,0,0,0,37,-37,86,-54,0,0,0,1,115,82,71,66,0,-82,-50,28,-23,0,0,0,3,
				80,76,84,69,-1,-1,-1,-89,-60,27,-56,0,0,0,10,73,68,65,84,8,-41,99,96,0,0,0,2,0,
				1,-30,33,-68,51,0,0,0,0,73,69,78,68,-82,66,96,-126});
		base64bin.setContenttype("image/png");
		character.getPortrait().set(0, base64bin);
		character.refesh();
	}

	protected void do_btnChangeRace_actionPerformed(ActionEvent arg0) {
		popupMenuRace.show(this, btnRace.getX(), btnRace.getY()+btnRace.getHeight());
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
