package de.earthdawn.ui2.tree;

import de.earthdawn.data.Base64BinaryType;
import de.earthdawn.data.THREADITEMType;
import de.earthdawn.data.YesnoType;
import de.earthdawn.data.ItemkindType;
import de.earthdawn.ui2.ImagePreview;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class ThreadItemNodePanel extends AbstractNodePanel<THREADITEMType> {
	private static final long serialVersionUID = -1042688290869805755L;
	private JTextField textFieldName;
	private JSpinner spinnerWeight;
	private JTextField textFieldLocation;
	private JCheckBox chckbxUsed;
	private JComboBox<ItemkindType> comboBoxType;
	private JTextField textFieldDescription;
	private JSpinner spinnerSpellDefense;
	private JSpinner spinnerMaxThreads;
	private JSpinner spinnerLP;
	private JTextField textFieldBookRef;
	private JSpinner spinnerDepatterningrate;
	private JSpinner spinnerBloodDamage;
	private JSpinner spinnerEdn;
	private JTextField textFieldEffect;
	private JSpinner spinnerWeaventhreadrank;
	private JSpinner spinnerSize;
	private JLabel lblImage;

	public ThreadItemNodePanel(THREADITEMType node) {
		super(node);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new MigLayout("", "[24px][128px,grow][24px][60px:60px:60px][30px][60px:60px:60px][150px:150px]", "[20px:20px:20px][20px:20px:20px][20px:20px:20px][20px:20px:20px][][20px:20px:20px][20px:20px:20px]"));

		add(new JLabel("Type"), "cell 0 0,alignx left,aligny center");
		comboBoxType = new JComboBox<ItemkindType>(ItemkindType.values());
		comboBoxType.setFont(new Font("Tahoma", Font.PLAIN, 10));
		add(comboBoxType, "cell 1 0,growx,aligny center");
		comboBoxType.setSelectedItem(nodeObject.getKind());

		add(new JLabel("EDN"), "cell 2 0,alignx left,aligny center");
		spinnerEdn = new JSpinner(new SpinnerNumberModel(node.getEnchantingdifficultynumber(), 0, 99, 1));
		add(spinnerEdn, "cell 3 0,alignx left,aligny center");

		lblImage = new JLabel();
		List<Base64BinaryType> images = node.getIMAGE();
		if( ! images.isEmpty() ) {
			ImageIcon icon = new ImageIcon(images.get(0).getValue());
			Image image = icon.getImage().getScaledInstance(120, -1, Image.SCALE_SMOOTH);
			lblImage.setIcon(new ImageIcon(image));
		} else {
			byte[] data = new byte[]{0};
			File file = new File("images/nopicture.png");
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				data = new byte[(int) file.length()];
				fileInputStream.read(data);
				fileInputStream.close();
			} catch (FileNotFoundException e) {
				System.err.println(e.getLocalizedMessage());
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			}
			lblImage.setIcon(new ImageIcon((new ImageIcon(data)).getImage().getScaledInstance(-1, 150, Image.SCALE_SMOOTH)));
		}
		lblImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if( e.getButton() == 1 ) do_updateImage();
			}
		});
		lblImage.setVerticalAlignment(JLabel.BOTTOM);
		lblImage.setHorizontalAlignment(JLabel.CENTER);
		lblImage.setOpaque(false);
		add(lblImage, "cell 6 0 1 7,alignx center,aligny center,grow");

		add(new JLabel("Weight"), "cell 2 1,alignx left,aligny center");
		spinnerWeight = new JSpinner(new SpinnerNumberModel(node.getWeight(), 0, 99, 1));
		add(spinnerWeight, "cell 3 1,alignx left,aligny center");

		add(new JLabel("LP cost growth"), "cell 4 2");
		spinnerLP = new JSpinner(new SpinnerNumberModel(node.getLpcostgrowth(), 0, 15, 1));
		add(spinnerLP, "cell 5 2,alignx left,aligny center");

		add(new JLabel("BookRef"), "cell 0 3,alignx left,aligny center");
		textFieldBookRef = new JTextField();
		textFieldBookRef.setText(node.getBookref());
		add(textFieldBookRef, "cell 1 3,growx,aligny center");
		textFieldBookRef.setColumns(10);

		add(new JLabel("Max Threads"), "cell 4 3");
		spinnerMaxThreads = new JSpinner(new SpinnerNumberModel(node.getMaxthreads(), 0, 15, 1));
		add(spinnerMaxThreads, "cell 5 3,alignx left,aligny center");

		add(new JLabel("Spell Defense"), "cell 2 2");
		spinnerSpellDefense = new JSpinner(new SpinnerNumberModel(node.getSpelldefense(), 0, 99, 1));
		add(spinnerSpellDefense, "cell 3 2,alignx left,aligny center");

		add(new JLabel("Thread Rank"), "cell 2 3");
		spinnerWeaventhreadrank = new JSpinner(new SpinnerNumberModel(node.getWeaventhreadrank(), 0, 15, 1));
		add(spinnerWeaventhreadrank, "cell 3 3,alignx left,aligny center");

		add(new JLabel("Size"), "cell 2 4");
		spinnerSize = new JSpinner(new SpinnerNumberModel(node.getSize(), 0, 99, 1));
		add(spinnerSize, "cell 3 4,alignx left,aligny center");

		add(new JLabel("Name"), "cell 0 2,alignx left,aligny center");
		textFieldName = new JTextField();
		add(textFieldName, "cell 1 2,growx,aligny center");
		textFieldName.setColumns(12);
		textFieldName.setText(nodeObject.getName());

		add(new JLabel("Location"), "cell 0 1,alignx left,aligny center");
		textFieldLocation = new JTextField();
		add(textFieldLocation, "cell 1 1,growx,aligny center");
		textFieldLocation.setColumns(10);
		textFieldLocation.setText(node.getLocation());

		add(new JLabel("Blood Damage"), "cell 4 0,alignx left,aligny center");
		spinnerBloodDamage = new JSpinner(new SpinnerNumberModel(node.getBlooddamage(), 0, 99, 1));
		add(spinnerBloodDamage, "cell 5 0,alignx left,aligny center");

		add(new JLabel("Depatterningrate"), "cell 4 1");
		spinnerDepatterningrate = new JSpinner(new SpinnerNumberModel(node.getDepatterningrate(), 0, 99, 1));
		add(spinnerDepatterningrate, "cell 5 1,alignx left,aligny center");

		add(new JLabel("Effect"), "cell 0 5");
		textFieldEffect = new JTextField();
		add(textFieldEffect, "cell 1 5 5 1,growx,aligny center");
		textFieldEffect.setColumns(12);
		textFieldEffect.setText(nodeObject.getEffect());

		chckbxUsed = new JCheckBox("Used");
		chckbxUsed.setOpaque(false);
		add(chckbxUsed, "cell 5 4,alignx right,aligny center");
		chckbxUsed.setSelected(node.getUsed() == YesnoType.YES);

		add(new JLabel("Description"), "cell 0 6");
		textFieldDescription = new JTextField();
		add(textFieldDescription, "cell 1 6 5 1,growx,aligny center");
		textFieldDescription.setColumns(12);
		String description = nodeObject.getDESCRIPTION();
		if( description != null ) textFieldDescription.setText(description);
	}

	@Override
	public void updateObject() {
		nodeObject.setKind((ItemkindType) comboBoxType.getSelectedItem());
		nodeObject.setName(textFieldName.getText());
		nodeObject.setWeight(((Double) spinnerWeight.getValue()).floatValue());
		nodeObject.setLocation(textFieldLocation.getText());
		nodeObject.setBookref(textFieldBookRef.getText());
		nodeObject.setBlooddamage((Integer) spinnerBloodDamage.getValue());
		nodeObject.setDepatterningrate((Integer) spinnerDepatterningrate.getValue());
		nodeObject.setEnchantingdifficultynumber((Integer) spinnerEdn.getValue());
		nodeObject.setEffect(textFieldEffect.getText());
		if(chckbxUsed.isSelected()) nodeObject.setUsed(YesnoType.YES);
		else nodeObject.setUsed(YesnoType.NO);
		nodeObject.setDESCRIPTION(textFieldDescription.getText());
		nodeObject.setLpcostgrowth((Integer)spinnerLP.getValue());
		nodeObject.setMaxthreads((Integer)spinnerMaxThreads.getValue());
		nodeObject.setSpelldefense((Integer)spinnerSpellDefense.getValue());
		nodeObject.setWeaventhreadrank((Integer)spinnerWeaventhreadrank.getValue());
		nodeObject.setSize((Integer)spinnerSize.getValue());
	}

	protected void do_updateImage() {
		JFileChooser fc = new JFileChooser(new File("images"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG-GIF-PNG Images", "jpg", "gif", "png", "jpeg");
		fc.setFileFilter(filter);
		ImagePreview imagepreview = new ImagePreview(150,150);
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
				List<Base64BinaryType> objectimages = nodeObject.getIMAGE();
				if( objectimages.isEmpty() ) objectimages.add(base64bin);
				else objectimages.set(0, base64bin);
				ImageIcon icon = new ImageIcon(data);
				Image image = icon.getImage().getScaledInstance(-1, 150, Image.SCALE_SMOOTH);
				lblImage.setIcon(new ImageIcon(image));
			} catch (FileNotFoundException e1) {
				System.err.println(e1.getLocalizedMessage());
			} catch (IOException e2) {
				System.err.println(e2.getLocalizedMessage());
			}
		}
	}
}
