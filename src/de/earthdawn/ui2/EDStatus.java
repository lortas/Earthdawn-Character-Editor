package de.earthdawn.ui2;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import de.earthdawn.CharacterContainer;
import javax.swing.LayoutStyle.ComponentPlacement;

public class EDStatus extends JPanel {
	private CharacterContainer character;
	private JPanel panelLegendPoints;
	private JLabel lblTotal;
	private JTextField textFieldTotalLegendPoints;
	private JLabel lblCurrent;
	private JTextField textFieldCurrentLegendPoints;
	private JLabel lblUsed;
	private JTextField textFieldSpendLegendPoints;
	private JPanel panelDefense;
	private JLabel lblPysical;
	private JTextField textFieldDefensePhysical;
	private JLabel lblSpell;
	private JTextField textFieldDefenseSpell;
	private JLabel lblSozial;
	private JTextField textFieldDefenseSocial;
	private JPanel panel;
	private JLabel lblUnconsciousness;
	private JTextField textFieldUnconsciousness;
	private JLabel lblDeath;
	private JTextField textFieldDeath;
	private JLabel lblThreshold;
	private JTextField textFieldThreshold;

	/**
	 * Create the panel.
	 */
	public EDStatus() {
		setMinimumSize(new Dimension(10, 100));
		setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		panelLegendPoints = new JPanel();
		panelLegendPoints.setBorder(new TitledBorder(null, "Legend Points", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		panelDefense = new JPanel();
		FlowLayout fl_panelDefense = (FlowLayout) panelDefense.getLayout();
		fl_panelDefense.setAlignment(FlowLayout.LEADING);
		panelDefense.setBorder(new TitledBorder(null, "Defense", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		panel.setBorder(new TitledBorder(null, "Health", TitledBorder.LEADING, TitledBorder.TOP, null, null));


		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panelLegendPoints, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
				.addComponent(panelDefense, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panelLegendPoints, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
					.addGap(2)
					.addComponent(panelDefense, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(56, Short.MAX_VALUE))
		);
		
		lblUnconsciousness = new JLabel("Unconsciousness");
		panel.add(lblUnconsciousness);
		
		textFieldUnconsciousness = new JTextField();
		panel.add(textFieldUnconsciousness);
		textFieldUnconsciousness.setColumns(10);
		
		lblDeath = new JLabel("Death");
		panel.add(lblDeath);
		
		textFieldDeath = new JTextField();
		panel.add(textFieldDeath);
		textFieldDeath.setColumns(10);
		
		lblThreshold = new JLabel("Threshold");
		panel.add(lblThreshold);
		
		textFieldThreshold = new JTextField();
		panel.add(textFieldThreshold);
		textFieldThreshold.setColumns(10);
		
		lblPysical = new JLabel("Physical");
		panelDefense.add(lblPysical);
		
		textFieldDefensePhysical = new JTextField();
		panelDefense.add(textFieldDefensePhysical);
		textFieldDefensePhysical.setColumns(10);
		
		lblSpell = new JLabel("Spell");
		panelDefense.add(lblSpell);
		
		textFieldDefenseSpell = new JTextField();
		panelDefense.add(textFieldDefenseSpell);
		textFieldDefenseSpell.setColumns(10);
		
		lblSozial = new JLabel("Social");
		panelDefense.add(lblSozial);
		
		textFieldDefenseSocial = new JTextField();
		panelDefense.add(textFieldDefenseSocial);
		textFieldDefenseSocial.setColumns(10);
		FlowLayout fl_panelLegendPoints = new FlowLayout(FlowLayout.LEADING, 5, 5);
		panelLegendPoints.setLayout(fl_panelLegendPoints);
		
		lblTotal = new JLabel("Total");
		panelLegendPoints.add(lblTotal);
		
		textFieldTotalLegendPoints = new JTextField();
		panelLegendPoints.add(textFieldTotalLegendPoints);
		textFieldTotalLegendPoints.setColumns(10);
		
		lblUsed = new JLabel("Spend");
		panelLegendPoints.add(lblUsed);
		
		textFieldSpendLegendPoints = new JTextField();
		panelLegendPoints.add(textFieldSpendLegendPoints);
		textFieldSpendLegendPoints.setColumns(10);
		
		lblCurrent = new JLabel("Current");
		panelLegendPoints.add(lblCurrent);
		
		textFieldCurrentLegendPoints = new JTextField();
		panelLegendPoints.add(textFieldCurrentLegendPoints);
		textFieldCurrentLegendPoints.setColumns(10);
		setLayout(groupLayout);
	}

	public CharacterContainer getCharacter() {
		return character;
	}

	public void setCharacter(CharacterContainer character) {
		this.character = character;
		
		
		// Display Legendpoints
		int totalEarnedLP = this.character.getLegendPoints().getTotallegendpoints();
		int totalUsedLP = this.character.getCalculatedLegendpoints().getTotal();
		int totalLPleft = totalEarnedLP - totalUsedLP;
		textFieldTotalLegendPoints.setText(String.valueOf(totalEarnedLP));
		textFieldSpendLegendPoints.setText(String.valueOf(totalUsedLP));
		textFieldCurrentLegendPoints.setText(String.valueOf(totalLPleft));
		
		// DisplayDefense
		textFieldDefensePhysical.setText(String.valueOf(character.getDefence().getPhysical()));
		textFieldDefenseSpell.setText(String.valueOf(character.getDefence().getSpell()));
		textFieldDefenseSocial.setText(String.valueOf(character.getDefence().getSocial()));
		
		// Health
		textFieldUnconsciousness.setText(String.valueOf(character.getUnconsciousness().getValue()));
		textFieldDeath.setText(String.valueOf(character.getDeath().getValue()));
		textFieldThreshold.setText(String.valueOf(character.getWound().getThreshold()));
		
	}
}
