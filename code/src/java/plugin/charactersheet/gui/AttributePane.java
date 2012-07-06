/*
 * AttributePane.java
 *
 * Created on February 2, 2004, 7:38 PM
 */

package plugin.charactersheet.gui;

import gmgen.gui.GridBoxLayout;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pcgen.core.PCStat;
import pcgen.core.PlayerCharacter;
import pcgen.io.exporttoken.StatToken;

/**
 * Confirmed no memory Leaks Dec 10, 2004
 * @author  soulcatcher
 */
public class AttributePane extends JPanel
{
	private PlayerCharacter pc;
	private List<Attribute> attrList = new ArrayList<Attribute>();

	private static final Font FONT_EIGHT = new Font("Dialog", 0, 8);
	private static final Font FONT_BFOURTEEN = new Font("Dialog", 1, 14);
	private static final Font FONT_FOURTEEN = new Font("Dialog", 0, 14);
	private static final FlowLayout FLOWCENTER =
			new FlowLayout(java.awt.FlowLayout.CENTER, 1, 1);
	private static final String ABILITY = "Ability";
	private static final String BASE = "Base";
	private static final String CURR = "Curr";
	private static final String TEMP = "Temp";
	private static final String NAME = "Name";
	private static final String SCORE = "Score";
	private static final String MOD = "Mod";
	private static final String SPACE = " ";

	/** Creates new form AttributePane */
	public AttributePane()
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
	{
		setLayout(new GridBoxLayout(0, 7, 2, 2));

		JLabel abilityLabel = new JLabel(ABILITY);
		abilityLabel.setFont(FONT_EIGHT);
		add(abilityLabel);

		JLabel baseLabel = new JLabel(BASE);
		baseLabel.setFont(FONT_EIGHT);
		add(baseLabel);
		add(new JLabel());

		JLabel currLabel = new JLabel(CURR);
		currLabel.setFont(FONT_EIGHT);
		add(currLabel);
		add(new JLabel());

		JLabel tempLabel = new JLabel(TEMP);
		tempLabel.setFont(FONT_EIGHT);
		add(tempLabel);
		add(new JLabel());

		JLabel nameLabel = new JLabel(NAME);
		nameLabel.setFont(FONT_EIGHT);
		add(nameLabel);

		JLabel bscoreLabel = new JLabel(SCORE);
		bscoreLabel.setFont(FONT_EIGHT);
		add(bscoreLabel);

		JLabel bmodLabel = new JLabel(MOD);
		bmodLabel.setFont(FONT_EIGHT);
		add(bmodLabel);

		JLabel cscoreLabel = new JLabel(SCORE);
		cscoreLabel.setFont(FONT_EIGHT);
		add(cscoreLabel);

		JLabel cmodLabel = new JLabel(MOD);
		cmodLabel.setFont(FONT_EIGHT);
		add(cmodLabel);

		JLabel tscoreLabel = new JLabel(SCORE);
		tscoreLabel.setFont(FONT_EIGHT);
		add(tscoreLabel);

		JLabel tmodLabel = new JLabel(MOD);
		tmodLabel.setFont(FONT_EIGHT);
		add(tmodLabel);
	}

	/**
	 * set color
	 */
	public void setColor()
	{
		setLocalColor();
		for (Attribute att : attrList)
		{
			att.setColor();
		}
	}

	/**
	 * Set local color
	 */
	public void setLocalColor()
	{
		setBackground(CharacterPanel.white);
	}

	/**
	 * Set PC
	 * @param pc
	 */
	public void setPc(PlayerCharacter pc)
	{
		this.pc = pc;
		setVisible(true);
		destruct();

		for (PCStat stat : pc.getStatSet())
 		{
			Attribute attr = new Attribute(stat);
			attrList.add(attr);
		}
	}

	/**
	 * Refresh this pane
	 */
	public void refresh()
	{
		for (Attribute att : attrList)
		{
			att.refresh();
		}
	}

	/**
	 * Destroy this pane
	 */
	public void destruct()
	{
		for (Attribute att : attrList)
		{
			att.destruct();
		}
		attrList.clear();
	}

	protected class Attribute
	{
		PCStat stat;

		private JLabel attrAbbrev;
		private JPanel attrPanel;

		private JLabel baseAttr;
		private JPanel baseAttrPanel;
		private JLabel baseBonus;
		private JPanel baseBonusPanel;

		private JLabel modifiedAttr;
		private JPanel modifiedAttrPanel;
		private JLabel modifiedBonus;
		private JPanel modifiedBonusPanel;

		private JLabel tempAttr;
		private JPanel tempAttrPanel;
		private JLabel tempBonus;
		private JPanel tempBonusPanel;

		/**
		 * Constructor
		 * @param pcstat
		 */
		public Attribute(PCStat pcstat)
		{
			stat = pcstat;
			initComponents();
			setColor();
		}

		protected void initComponents()
		{
			attrPanel = new JPanel();
			attrPanel.setLayout(FLOWCENTER);
			attrAbbrev = new JLabel();
			attrAbbrev.setFont(FONT_BFOURTEEN);
			attrAbbrev.setText(SPACE);
			attrPanel.add(attrAbbrev);
			add(attrPanel);

			baseAttrPanel = new JPanel();
			baseAttrPanel.setLayout(FLOWCENTER);
			baseAttr = new JLabel();
			baseAttr.setFont(FONT_FOURTEEN);
			baseAttr.setText(SPACE);
			baseAttrPanel.add(baseAttr);
			add(baseAttrPanel);

			baseBonusPanel = new JPanel();
			baseBonusPanel.setLayout(FLOWCENTER);
			baseBonus = new JLabel();
			baseBonus.setFont(FONT_FOURTEEN);
			baseBonus.setText(SPACE);
			baseBonusPanel.add(baseBonus);
			add(baseBonusPanel);

			modifiedAttrPanel = new JPanel();
			modifiedAttrPanel.setLayout(FLOWCENTER);
			modifiedAttr = new JLabel();
			modifiedAttr.setFont(FONT_FOURTEEN);
			modifiedAttr.setText(SPACE);
			modifiedAttrPanel.add(modifiedAttr);
			add(modifiedAttrPanel);

			modifiedBonusPanel = new JPanel();
			modifiedBonusPanel.setLayout(FLOWCENTER);
			modifiedBonus = new JLabel();
			modifiedBonus.setFont(FONT_FOURTEEN);
			modifiedBonus.setText(SPACE);
			modifiedBonusPanel.add(modifiedBonus);
			add(modifiedBonusPanel);

			tempAttrPanel = new JPanel();
			tempAttrPanel.setLayout(FLOWCENTER);
			tempAttr = new JLabel();
			tempAttr.setFont(FONT_FOURTEEN);
			tempAttr.setText(SPACE);
			tempAttrPanel.add(tempAttr);
			add(tempAttrPanel);

			tempBonusPanel = new JPanel();
			tempBonusPanel.setLayout(FLOWCENTER);
			tempBonus = new JLabel();
			tempBonus.setFont(FONT_FOURTEEN);
			tempBonus.setText(SPACE);
			tempBonusPanel.add(tempBonus);
			add(tempBonusPanel);
		}

		/**
		 * Refresh
		 */
		public void refresh()
		{
			attrAbbrev.setText(StatToken.getNameToken(pc, stat));
			baseAttr.setText(StatToken.getStatToken(pc, stat, false, false,
					true, false, 0));
			baseBonus.setText(StatToken.getModToken(pc, stat, false, false,
					true, false, 0));
			modifiedAttr.setText(StatToken.getStatToken(pc, stat, false, true,
					true, false, 0));
			modifiedBonus.setText(StatToken.getModToken(pc, stat, false, true,
					true, false, 0));
			tempAttr.setText(StatToken.getStatToken(pc, stat, true, true, true,
					false, 0));
			tempBonus.setText(StatToken.getModToken(pc, stat, true, true, true,
					false, 0));
		}

		/**
		 * Set color
		 */
		public void setColor()
		{
			setBackground(CharacterPanel.white);
			attrPanel.setBackground(CharacterPanel.bodyDark);
			attrPanel.setBorder(new javax.swing.border.LineBorder(
				CharacterPanel.border, 2));

			baseAttrPanel.setBackground(CharacterPanel.white);
			baseAttrPanel.setBorder(new javax.swing.border.LineBorder(
				CharacterPanel.border, 2));
			baseBonusPanel.setBackground(CharacterPanel.white);
			baseBonusPanel.setBorder(new javax.swing.border.LineBorder(
				CharacterPanel.border, 2));

			modifiedAttrPanel.setBackground(CharacterPanel.bodyLight);
			modifiedAttrPanel.setBorder(new javax.swing.border.LineBorder(
				CharacterPanel.border, 2));
			modifiedBonusPanel.setBackground(CharacterPanel.bodyLight);
			modifiedBonusPanel.setBorder(new javax.swing.border.LineBorder(
				CharacterPanel.border, 2));

			tempAttrPanel.setBackground(CharacterPanel.white);
			tempAttrPanel.setBorder(new javax.swing.border.LineBorder(
				CharacterPanel.lightGrey, 2));
			tempBonusPanel.setBackground(CharacterPanel.white);
			tempBonusPanel.setBorder(new javax.swing.border.LineBorder(
				CharacterPanel.lightGrey, 2));
		}

		/**
		 * destroy
		 */
		public void destruct()
		{
			remove(attrPanel);
			remove(baseAttrPanel);
			remove(baseBonusPanel);
			remove(modifiedAttrPanel);
			remove(modifiedBonusPanel);
			remove(tempAttrPanel);
			remove(tempBonusPanel);
		}
	}
}
