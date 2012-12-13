/*
 * CombatPane1.java
 *
 * Created on February 3, 2004, 9:54 AM
 */

package plugin.charactersheet.gui;

import gmgen.plugin.PlayerCharacterOutput;

import java.awt.Font;
import java.util.Properties;

import pcgen.core.PlayerCharacter;
import pcgen.core.display.CharacterDisplay;
import pcgen.io.exporttoken.HPToken;
import pcgen.io.exporttoken.MovementToken;
import pcgen.io.exporttoken.SRToken;
import pcgen.util.Delta;

/**
 * Confirmed no memory Leaks Dec 10, 2004
 * @author  ddjone3
 */
public class CombatPane1 extends javax.swing.JPanel
{
	private PlayerCharacter pc;
	private Properties pcProperties;
	private boolean updateProperties = false;

	private static final String PROP_WOUNDS = "cs.CombatPane1.woundsTb";
	private static final String PROP_SUBDUAL = "cs.CombatPane1.subdualTb";

	private static final String AC_BASE_TOKEN = "AC.Base";
	private static final String AC_ARMOR_TOKEN = "AC.Armor";
	private static final String AC_SHIELD_TOKEN = "AC.Shield";
	private static final String AC_ABILITY_TOKEN = "AC.Ability";
	private static final String AC_SIZE_TOKEN = "AC.Size";
	private static final String AC_NATURAL_TOKEN = "AC.NaturalArmor";
	private static final String AC_MISC_TOKEN = "AC.Misc";
	private static final String MISSCHANCE_VAR = "MISSCHANCE";
	private static final String SPELLFAILURE_TOKEN = "SPELLFAILURE";
	private static final String ACCHECK_TOKEN = "ACCHECK";
	private static final String SPACE = " ";
	private static final String PERCENT = "%";
	private static final String COLON = ":";
	private static final String EQUALS = "=";
	private static final String PLUS = "+";
	private static final String HP = "  HP  ";
	private static final String AC = "  AC  ";
	private static final String TOTAL = "Total";
	private static final String FLAT = "Flat";
	private static final String TOUCH = "Touch";
	private static final String BASE = "Base";
	private static final String ARMOR = "Armor";
	private static final String SHIELD = "Shield";
	private static final String STAT = "Stat";
	private static final String SIZE = "Size";
	private static final String NATURAL = "Natural";
	private static final String MISC = "Misc";
	private static final String MISS = "Miss";
	private static final String CHANCE = "Chance";
	private static final String ARCANE = "Arcane";
	private static final String SPELL = "Spell";
	private static final String FAILURE = "Failure";
	private static final String CHECK = "Check";
	private static final String PENALTY = "Penalty";
	private static final String RESIST = "Resist";
	private static final String CURR_HP = "Wounds/Current HP";
	private static final String CURR_SUBDUAL = "Subdual Damage";
	private static final String DR = "Damage Reduction";
	private static final String SPEED = "Speed";
	private static final Font FONT_TEN = new Font("Dialog", 0, 10);
	private static final Font FONT_NINE = new Font("Dialog", 0, 9);

	/** Creates new form CombatPane1 */
	public CombatPane1()
	{
		initComponents();
		setColor();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()
	{//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;

		jPanel1 = new javax.swing.JPanel();
		attrAbbrev = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		attrAbbrev1 = new javax.swing.JLabel();
		woundsTb = new javax.swing.JTextField();
		subdualTb = new javax.swing.JTextField();
		padding3 = new javax.swing.JLabel();
		padding4 = new javax.swing.JLabel();
		padding5 = new javax.swing.JLabel();
		padding6 = new javax.swing.JLabel();
		padding7 = new javax.swing.JLabel();
		padding8 = new javax.swing.JLabel();
		padding9 = new javax.swing.JLabel();
		padding10 = new javax.swing.JLabel();
		padding11 = new javax.swing.JLabel();
		padding12 = new javax.swing.JLabel();
		padding13 = new javax.swing.JLabel();
		padding14 = new javax.swing.JLabel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		padding15 = new javax.swing.JLabel();
		padding16 = new javax.swing.JLabel();
		padding17 = new javax.swing.JLabel();
		padding18 = new javax.swing.JLabel();
		padding19 = new javax.swing.JLabel();
		padding20 = new javax.swing.JLabel();
		padding21 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		jLabel14 = new javax.swing.JLabel();
		jLabel15 = new javax.swing.JLabel();
		jLabel16 = new javax.swing.JLabel();
		jLabel17 = new javax.swing.JLabel();
		jLabel18 = new javax.swing.JLabel();
		jLabel19 = new javax.swing.JLabel();
		jLabel20 = new javax.swing.JLabel();
		jLabel21 = new javax.swing.JLabel();
		jLabel22 = new javax.swing.JLabel();
		jLabel23 = new javax.swing.JLabel();
		jLabel24 = new javax.swing.JLabel();
		jLabel25 = new javax.swing.JLabel();
		jLabel26 = new javax.swing.JLabel();
		jLabel27 = new javax.swing.JLabel();
		jLabel28 = new javax.swing.JLabel();
		jLabel29 = new javax.swing.JLabel();
		jLabel30 = new javax.swing.JLabel();
		jLabel31 = new javax.swing.JLabel();
		jLabel32 = new javax.swing.JLabel();
		jLabel33 = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		totalAc = new javax.swing.JLabel();
		jPanel4 = new javax.swing.JPanel();
		speed = new javax.swing.JLabel();
		jPanel5 = new javax.swing.JPanel();
		damageReduction = new javax.swing.JLabel();
		jPanel8 = new javax.swing.JPanel();
		flatAc = new javax.swing.JLabel();
		jPanel7 = new javax.swing.JPanel();
		touchAc = new javax.swing.JLabel();
		jPanel6 = new javax.swing.JPanel();
		acBase = new javax.swing.JLabel();
		jPanel9 = new javax.swing.JPanel();
		totalHp = new javax.swing.JLabel();
		jPanel15 = new javax.swing.JPanel();
		acArmor = new javax.swing.JLabel();
		jPanel14 = new javax.swing.JPanel();
		acShield = new javax.swing.JLabel();
		jPanel13 = new javax.swing.JPanel();
		acStat = new javax.swing.JLabel();
		jPanel12 = new javax.swing.JPanel();
		acSize = new javax.swing.JLabel();
		jPanel11 = new javax.swing.JPanel();
		acNatural = new javax.swing.JLabel();
		jPanel10 = new javax.swing.JPanel();
		acMisc = new javax.swing.JLabel();
		jPanel16 = new javax.swing.JPanel();
		missChance = new javax.swing.JLabel();
		jPanel17 = new javax.swing.JPanel();
		spellFailure = new javax.swing.JLabel();
		jPanel18 = new javax.swing.JPanel();
		armorCheck = new javax.swing.JLabel();
		jPanel19 = new javax.swing.JPanel();
		spellResist = new javax.swing.JLabel();

		setLayout(new java.awt.GridBagLayout());

		jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			0, 0));

		attrAbbrev.setText(HP);
		jPanel1.add(attrAbbrev);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel1, gridBagConstraints);

		jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			0, 0));

		attrAbbrev1.setText(AC);
		jPanel2.add(attrAbbrev1);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel2, gridBagConstraints);

		woundsTb.setFont(FONT_TEN);
		// SwingConstants.CENTER is equivalent to JTextField.CENTER but more
		// 'correct' in a Java coding context (it is a static reference)
		woundsTb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		woundsTb.addActionListener(new java.awt.event.ActionListener()
		{
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				woundsTbActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.ipadx = 5;
		gridBagConstraints.ipady = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
		add(woundsTb, gridBagConstraints);

		subdualTb.setFont(FONT_TEN);
		// SwingConstants.CENTER is equivalent to JTextField.CENTER but more
		// 'correct' in a Java coding context (it is a static reference)
		subdualTb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		subdualTb.addActionListener(new java.awt.event.ActionListener()
		{
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				subdualTbActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 11;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.ipadx = 5;
		gridBagConstraints.ipady = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
		add(subdualTb, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 20;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		add(padding3, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 18;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
		add(padding4, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 0);
		add(padding5, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
		add(padding6, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 29, 0, 0);
		add(padding7, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 14;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
		add(padding8, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 21;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 33, 0, 0);
		add(padding9, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 29, 0, 0);
		add(padding10, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 16;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
		add(padding11, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 33, 0, 0);
		add(padding12, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 12;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
		add(padding13, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 26;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		add(padding14, gridBagConstraints);

		jLabel1.setText(COLON);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 1);
		add(jLabel1, gridBagConstraints);

		jLabel2.setText(COLON);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 1);
		add(jLabel2, gridBagConstraints);

		jLabel3.setText(EQUALS);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 1);
		add(jLabel3, gridBagConstraints);

		jLabel4.setText(PLUS);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
		add(jLabel4, gridBagConstraints);

		jLabel5.setText(PLUS);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
		add(jLabel5, gridBagConstraints);

		jLabel6.setText(PLUS);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 14;
		gridBagConstraints.gridy = 2;
		add(jLabel6, gridBagConstraints);

		jLabel7.setText(PLUS);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 12;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
		add(jLabel7, gridBagConstraints);

		jLabel8.setText(PLUS);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 18;
		gridBagConstraints.gridy = 2;
		add(jLabel8, gridBagConstraints);

		jLabel9.setText(PLUS);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 16;
		gridBagConstraints.gridy = 2;
		add(jLabel9, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 24;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		add(padding15, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 22;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		add(padding16, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 23;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 23, 0, 0);
		add(padding17, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 25;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 23, 0, 0);
		add(padding18, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 27;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 23, 0, 0);
		add(padding19, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
		add(padding20, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 31, 0, 0);
		add(padding21, gridBagConstraints);

		jLabel10.setFont(FONT_NINE);
		jLabel10.setText(TOTAL);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel10, gridBagConstraints);

		jLabel11.setFont(FONT_NINE);
		jLabel11.setText(TOUCH);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(jLabel11, gridBagConstraints);

		jLabel12.setFont(FONT_NINE);
		jLabel12.setText(FLAT);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel12, gridBagConstraints);

		jLabel13.setFont(FONT_NINE);
		jLabel13.setText(BASE);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel13, gridBagConstraints);

		jLabel14.setFont(FONT_NINE);
		jLabel14.setText(ARMOR);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel14, gridBagConstraints);

		jLabel15.setFont(FONT_NINE);
		jLabel15.setText(SHIELD);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel15, gridBagConstraints);

		jLabel16.setFont(FONT_NINE);
		jLabel16.setText(STAT);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 12;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel16, gridBagConstraints);

		jLabel17.setFont(FONT_NINE);
		jLabel17.setText(SIZE);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 14;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel17, gridBagConstraints);

		jLabel18.setFont(FONT_NINE);
		jLabel18.setText(NATURAL);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 16;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel18, gridBagConstraints);

		jLabel19.setFont(FONT_NINE);
		jLabel19.setText(MISC);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 18;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel19, gridBagConstraints);

		jLabel20.setFont(FONT_NINE);
		jLabel20.setText(MISS);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 21;
		gridBagConstraints.gridy = 3;
		add(jLabel20, gridBagConstraints);

		jLabel21.setFont(FONT_NINE);
		jLabel21.setText(CHANCE);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 21;
		gridBagConstraints.gridy = 4;
		add(jLabel21, gridBagConstraints);

		jLabel22.setFont(FONT_NINE);
		jLabel22.setText(ARCANE);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 23;
		gridBagConstraints.gridy = 3;
		add(jLabel22, gridBagConstraints);

		jLabel23.setFont(FONT_NINE);
		jLabel23.setText(SPELL);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 23;
		gridBagConstraints.gridy = 4;
		add(jLabel23, gridBagConstraints);

		jLabel24.setFont(FONT_NINE);
		jLabel24.setText(FAILURE);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 23;
		gridBagConstraints.gridy = 5;
		add(jLabel24, gridBagConstraints);

		jLabel25.setFont(FONT_NINE);
		jLabel25.setText(ARMOR);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 25;
		gridBagConstraints.gridy = 3;
		add(jLabel25, gridBagConstraints);

		jLabel26.setFont(FONT_NINE);
		jLabel26.setText(CHECK);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 25;
		gridBagConstraints.gridy = 4;
		add(jLabel26, gridBagConstraints);

		jLabel27.setFont(FONT_NINE);
		jLabel27.setText(PENALTY);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 25;
		gridBagConstraints.gridy = 5;
		add(jLabel27, gridBagConstraints);

		jLabel28.setFont(FONT_NINE);
		jLabel28.setText(SPELL);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 27;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel28, gridBagConstraints);

		jLabel29.setFont(FONT_NINE);
		jLabel29.setText(RESIST);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 26;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel29, gridBagConstraints);

		jLabel30.setFont(FONT_NINE);
		jLabel30.setText(CURR_HP);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 7;
		add(jLabel30, gridBagConstraints);

		jLabel31.setFont(FONT_NINE);
		jLabel31.setText(CURR_SUBDUAL);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 11;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 7;
		add(jLabel31, gridBagConstraints);

		jLabel32.setFont(FONT_NINE);
		jLabel32.setText(DR);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 18;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 5;
		add(jLabel32, gridBagConstraints);

		jLabel33.setFont(FONT_NINE);
		jLabel33.setText(SPEED);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 23;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 5;
		add(jLabel33, gridBagConstraints);

		jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		totalAc.setFont(FONT_TEN);
		totalAc.setText(SPACE);
		jPanel3.add(totalAc);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		add(jPanel3, gridBagConstraints);

		jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		speed.setFont(FONT_TEN);
		speed.setText(SPACE);
		jPanel4.add(speed);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 23;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel4, gridBagConstraints);

		jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		damageReduction.setFont(FONT_TEN);
		damageReduction.setText(SPACE);
		jPanel5.add(damageReduction);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 19;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel5, gridBagConstraints);

		jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		flatAc.setFont(FONT_TEN);
		flatAc.setText(SPACE);
		jPanel8.add(touchAc);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel8, gridBagConstraints);

		jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		touchAc.setFont(FONT_TEN);
		touchAc.setText(SPACE);
		jPanel7.add(flatAc);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel7, gridBagConstraints);

		jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		acBase.setFont(FONT_TEN);
		acBase.setText(SPACE);
		jPanel6.add(acBase);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel6, gridBagConstraints);

		jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		totalHp.setFont(FONT_TEN);
		totalHp.setText(SPACE);
		jPanel9.add(totalHp);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		add(jPanel9, gridBagConstraints);

		jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		acArmor.setFont(FONT_TEN);
		acArmor.setText(SPACE);
		jPanel15.add(acArmor);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 9;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel15, gridBagConstraints);

		jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		acShield.setFont(FONT_TEN);
		acShield.setText(SPACE);
		jPanel14.add(acShield);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 11;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel14, gridBagConstraints);

		jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		acStat.setFont(FONT_TEN);
		acStat.setText(SPACE);
		jPanel13.add(acStat);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 13;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel13, gridBagConstraints);

		jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		acSize.setFont(FONT_TEN);
		acSize.setText(SPACE);
		jPanel12.add(acSize);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 15;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel12, gridBagConstraints);

		jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		acNatural.setFont(FONT_TEN);
		acNatural.setText(SPACE);
		jPanel11.add(acNatural);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 17;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel11, gridBagConstraints);

		jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		acMisc.setFont(FONT_TEN);
		acMisc.setText(SPACE);
		jPanel10.add(acMisc);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 19;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel10, gridBagConstraints);

		jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		missChance.setFont(FONT_TEN);
		missChance.setText(SPACE);
		jPanel16.add(missChance);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 21;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel16, gridBagConstraints);

		jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		spellFailure.setFont(FONT_TEN);
		spellFailure.setText(SPACE);
		jPanel17.add(spellFailure);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 23;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel17, gridBagConstraints);

		jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		armorCheck.setFont(FONT_TEN);
		armorCheck.setText(SPACE);
		jPanel18.add(armorCheck);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 25;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel18, gridBagConstraints);

		jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		spellResist.setFont(FONT_TEN);
		spellResist.setText(SPACE);
		jPanel19.add(spellResist);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 27;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jPanel19, gridBagConstraints);

	}//GEN-END:initComponents

	private void woundsTbActionPerformed(java.awt.event.ActionEvent evt)
	{//GEN-FIRST:event_woundsTbActionPerformed
		// Add your handling code here:
		pc.setDirty(true);
		updateProperties();
	}//GEN-LAST:event_woundsTbActionPerformed

	private void subdualTbActionPerformed(java.awt.event.ActionEvent evt)
	{//GEN-FIRST:event_subdualTbActionPerformed
		// Add your handling code here:
		pc.setDirty(true);
		updateProperties();
	}//GEN-LAST:event_subdualTbActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel acArmor;
	private javax.swing.JLabel acBase;
	private javax.swing.JLabel acMisc;
	private javax.swing.JLabel acNatural;
	private javax.swing.JLabel acShield;
	private javax.swing.JLabel acSize;
	private javax.swing.JLabel acStat;
	private javax.swing.JLabel armorCheck;
	private javax.swing.JLabel attrAbbrev;
	private javax.swing.JLabel attrAbbrev1;
	private javax.swing.JLabel damageReduction;
	private javax.swing.JLabel flatAc;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel23;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel25;
	private javax.swing.JLabel jLabel26;
	private javax.swing.JLabel jLabel27;
	private javax.swing.JLabel jLabel28;
	private javax.swing.JLabel jLabel29;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel30;
	private javax.swing.JLabel jLabel31;
	private javax.swing.JLabel jLabel32;
	private javax.swing.JLabel jLabel33;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JPanel jPanel11;
	private javax.swing.JPanel jPanel12;
	private javax.swing.JPanel jPanel13;
	private javax.swing.JPanel jPanel14;
	private javax.swing.JPanel jPanel15;
	private javax.swing.JPanel jPanel16;
	private javax.swing.JPanel jPanel17;
	private javax.swing.JPanel jPanel18;
	private javax.swing.JPanel jPanel19;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JLabel missChance;
	private javax.swing.JLabel padding10;
	private javax.swing.JLabel padding11;
	private javax.swing.JLabel padding12;
	private javax.swing.JLabel padding13;
	private javax.swing.JLabel padding14;
	private javax.swing.JLabel padding15;
	private javax.swing.JLabel padding16;
	private javax.swing.JLabel padding17;
	private javax.swing.JLabel padding18;
	private javax.swing.JLabel padding19;
	private javax.swing.JLabel padding20;
	private javax.swing.JLabel padding21;
	private javax.swing.JLabel padding3;
	private javax.swing.JLabel padding4;
	private javax.swing.JLabel padding5;
	private javax.swing.JLabel padding6;
	private javax.swing.JLabel padding7;
	private javax.swing.JLabel padding8;
	private javax.swing.JLabel padding9;
	private javax.swing.JLabel speed;
	private javax.swing.JLabel spellFailure;
	private javax.swing.JLabel spellResist;
	private javax.swing.JTextField subdualTb;
	private javax.swing.JLabel totalAc;
	private javax.swing.JLabel totalHp;
	private javax.swing.JLabel touchAc;
	private javax.swing.JTextField woundsTb;

	// End of variables declaration//GEN-END:variables

	/**
	 * SetColor
	 */
	public void setColor()
	{
		setBackground(CharacterPanel.white);
		jPanel1.setBackground(CharacterPanel.bodyDark);
		jPanel2.setBackground(CharacterPanel.bodyDark);
		woundsTb.setBackground(CharacterPanel.white);
		subdualTb.setBackground(CharacterPanel.white);
		jPanel3.setBackground(CharacterPanel.bodyLight);
		jPanel4.setBackground(CharacterPanel.white);
		jPanel5.setBackground(CharacterPanel.white);
		jPanel8.setBackground(CharacterPanel.bodyLight);
		jPanel7.setBackground(CharacterPanel.bodyLight);
		jPanel6.setBackground(CharacterPanel.white);
		jPanel9.setBackground(CharacterPanel.bodyLight);
		jPanel15.setBackground(CharacterPanel.white);
		jPanel14.setBackground(CharacterPanel.white);
		jPanel13.setBackground(CharacterPanel.white);
		jPanel12.setBackground(CharacterPanel.white);
		jPanel11.setBackground(CharacterPanel.white);
		jPanel10.setBackground(CharacterPanel.white);
		jPanel16.setBackground(CharacterPanel.white);
		jPanel17.setBackground(CharacterPanel.bodyLight);
		jPanel18.setBackground(CharacterPanel.bodyLight);
		jPanel19.setBackground(CharacterPanel.bodyLight);
		jPanel1.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel2.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		woundsTb.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		subdualTb.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel3.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel4.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel5.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel8.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel7.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel6.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel9.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel15.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel14.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel13.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel12.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel11.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel10.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel16.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel17.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel18.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
		jPanel19.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border, 2));
	}

	/**
	 * Set the pc
	 * @param pc
	 * @param pcProperties
	 */
	public void setPc(PlayerCharacter pc, Properties pcProperties)
	{
		this.pc = pc;
		this.pcProperties = pcProperties;
	}

	/**
	 * Refresh
	 */
	public void refresh()
	{
		PlayerCharacterOutput pcOut = new PlayerCharacterOutput(pc);
		CharacterDisplay display = pc.getDisplay();

		totalHp.setText(Integer.toString(HPToken.getHPToken(pc)));
		damageReduction.setText(display.calcDR() + ' ');
		speed.setText(MovementToken.getMovementToken(display));

		totalAc.setText(Integer.toString(display.getACTotal()));
		flatAc.setText(Integer.toString(display.flatfootedAC()));
		touchAc.setText(Integer.toString(display.touchAC()));
		acBase.setText(pcOut.getExportToken(AC_BASE_TOKEN));
		acArmor.setText(pcOut.getExportToken(AC_ARMOR_TOKEN));
		acShield.setText(pcOut.getExportToken(AC_SHIELD_TOKEN));
		acStat.setText(pcOut.getExportToken(AC_ABILITY_TOKEN));
		acSize.setText(pcOut.getExportToken(AC_SIZE_TOKEN));
		acNatural.setText(pcOut.getExportToken(AC_NATURAL_TOKEN));
		acMisc.setText(pcOut.getExportToken(AC_MISC_TOKEN));
		missChance.setText(pc.getVariable(MISSCHANCE_VAR, true).intValue()
			+ PERCENT);

		spellFailure.setText(pc.modToFromEquipment(SPELLFAILURE_TOKEN)
			+ PERCENT);
		armorCheck.setText(Delta.toString(pc.modToFromEquipment(ACCHECK_TOKEN)));
		//Max Dex
		spellResist.setText(Integer.toString(SRToken.getSRToken(pc)));
		updatePane();
	}

	/**
	 * Update pc properties
	 */
	public void updateProperties()
	{
		if (updateProperties)
		{
			pcProperties.put(PROP_WOUNDS, woundsTb.getText());
			pcProperties.put(PROP_SUBDUAL, subdualTb.getText());
		}
	}

	/**
	 * Update the pane
	 */
	public void updatePane()
	{
		woundsTb.setText((String) pcProperties.get(PROP_WOUNDS));
		subdualTb.setText((String) pcProperties.get(PROP_SUBDUAL));
		updateProperties = true;
	}

	/**
	 * Destroy
	 */
	public void destruct()
	{
		//Put any code here that is needed to prevent memory leaks when this panel is destroyed
	}
}
