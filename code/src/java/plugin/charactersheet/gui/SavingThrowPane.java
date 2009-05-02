/*
 * CombatPane1.java
 *
 * Created on February 3, 2004, 9:54 AM
 */

package plugin.charactersheet.gui;

import gmgen.gui.GridBoxLayout;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import pcgen.core.Globals;
import pcgen.core.PCStat;
import pcgen.core.PlayerCharacter;
import pcgen.io.exporttoken.CheckToken;
import pcgen.io.exporttoken.StatToken;
import pcgen.util.Delta;

/**
 * Confirmed no memory Leaks Dec 10, 2004
 * @author  ddjone3
 */
public class SavingThrowPane extends JPanel
{
	private PlayerCharacter pc;

	private JLabel savingThrowsLabel;
	private JLabel totalLabel;
	private JLabel baseLabel;
	private JLabel abilityLabel;
	private JLabel magicLabel;
	private JLabel miscLabel;
	private JLabel epicLabel;
	private JLabel tempLabel;
	private JLabel tempModsLabel;

	private JPanel fortPanel;
	private JLabel fortLabel;
	private JLabel conLabel;
	private JPanel fortTotalPanel;
	private JLabel fortTotal;
	private JPanel fortBasePanel;
	private JLabel fortBase;
	private JPanel fortAbilityPanel;
	private JLabel fortAbility;
	private JPanel fortMagicPanel;
	private JLabel fortMagic;
	private JPanel fortMiscPanel;
	private JLabel fortMisc;
	private JPanel fortTempPanel;
	private JLabel fortTemp;
	private JPanel fortEpicPanel;
	private JLabel fortEpic;
	private JTextField fortNotesTb;

	private JPanel refPanel;
	private JLabel refLabel;
	private JLabel dexLabel;
	private JPanel refTotalPanel;
	private JLabel refTotal;
	private JPanel refBasePanel;
	private JLabel refBase;
	private JPanel refAbilityPanel;
	private JLabel refAbility;
	private JPanel refMagicPanel;
	private JLabel refMagic;
	private JPanel refMiscPanel;
	private JLabel refMisc;
	private JPanel refEpicPanel;
	private JLabel refEpic;
	private JPanel refTempPanel;
	private JLabel refTemp;
	private JTextField refNotesTb;

	private JPanel willPanel;
	private JLabel willLabel;
	private JLabel wisLabel;
	private JPanel willTotalPanel;
	private JLabel willTotal;
	private JPanel willBasePanel;
	private JLabel willBase;
	private JPanel willAbilityPanel;
	private JLabel willAbility;
	private JPanel willMagicPanel;
	private JLabel willMagic;
	private JPanel willMiscPanel;
	private JLabel willMisc;
	private JPanel willEpicPanel;
	private JLabel willEpic;
	private JPanel willTempPanel;
	private JLabel willTemp;
	private JTextField willNotesTb;

	private static final String EQUALS = "=";
	private static final String PLUS = "+";
	private static final String SPACE = " ";
	private static final String DIALOG = "Dialog";
	private static final String TOTAL = "Total";
	private static final String SAVING_THROWS = "Saving Throws";
	private static final String BASE = "Base";
	private static final String ABILITY = "Ability";
	private static final String MAGIC = "Magic";
	private static final String MISC = "Misc";
	private static final String EPIC = "Epic";
	private static final String TEMP = "Temp";
	private static final String TEMP_MODS = "Temporary Modifiers";
	private static final String REFLEX = "1";
	private static final String DEX = "(dex)";
	private static final String WILL = "2";
	private static final String WIS = "(wis)";
	private static final String FORT = "0";
	private static final String CON = "(con)";

	private static final String TOTAL_TOKEN = "TOTAL";
	private static final String BASE_TOKEN = "BASE";
	private static final String MAGIC_TOKEN = "MAGIC";
	private static final String MISC_TOKEN = "MISC.NOMAGIC.NOSTAT";
	private static final String EPIC_TOKEN = "CHECK.2.EPIC";
	private static final String PLUS_ZERO = "+0";

	private static final Font FONT_TEN = new Font(DIALOG, 0, 10);
	private static final Font FONT_NINE = new Font(DIALOG, 0, 9);

	/** Creates new form CombatPane1 */
	public SavingThrowPane()
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
		totalLabel = new JLabel();
		savingThrowsLabel = new JLabel();
		baseLabel = new JLabel();
		abilityLabel = new JLabel();
		magicLabel = new JLabel();
		miscLabel = new JLabel();
		epicLabel = new JLabel();
		tempLabel = new JLabel();
		tempModsLabel = new JLabel();

		fortPanel = new JPanel();
		fortLabel = new JLabel();
		conLabel = new JLabel();
		fortTotalPanel = new JPanel();
		fortTotal = new JLabel();
		fortBasePanel = new JPanel();
		fortBase = new JLabel();
		fortAbilityPanel = new JPanel();
		fortAbility = new JLabel();
		fortMagicPanel = new JPanel();
		fortMagic = new JLabel();
		fortMiscPanel = new JPanel();
		fortMisc = new JLabel();
		fortEpicPanel = new JPanel();
		fortEpic = new JLabel();
		fortTempPanel = new JPanel();
		fortTemp = new JLabel();
		fortNotesTb = new JTextField();

		refPanel = new JPanel();
		refLabel = new JLabel();
		dexLabel = new JLabel();
		refTotalPanel = new JPanel();
		refTotal = new JLabel();
		refBasePanel = new JPanel();
		refBase = new JLabel();
		refAbilityPanel = new JPanel();
		refAbility = new JLabel();
		refMagicPanel = new JPanel();
		refMagic = new JLabel();
		refMiscPanel = new JPanel();
		refMisc = new JLabel();
		refEpicPanel = new JPanel();
		refEpic = new JLabel();
		refTempPanel = new JPanel();
		refTemp = new JLabel();
		refNotesTb = new JTextField();

		willPanel = new JPanel();
		willLabel = new JLabel();
		wisLabel = new JLabel();
		willTotalPanel = new JPanel();
		willTotal = new JLabel();
		willBasePanel = new JPanel();
		willBase = new JLabel();
		willAbilityPanel = new JPanel();
		willAbility = new JLabel();
		willMagicPanel = new JPanel();
		willMagic = new JLabel();
		willMiscPanel = new JPanel();
		willMisc = new JLabel();
		willEpicPanel = new JPanel();
		willEpic = new JLabel();
		willTempPanel = new JPanel();
		willTemp = new JLabel();
		willNotesTb = new JTextField();

		setLayout(new GridBoxLayout(4, 15, 2, 1));

		//Row 1
		//Col 1
		savingThrowsLabel.setFont(FONT_TEN);
		savingThrowsLabel.setText(SAVING_THROWS);
		add(savingThrowsLabel);
		//Col 2
		totalLabel.setFont(FONT_NINE);
		totalLabel.setText(TOTAL);
		add(totalLabel);
		//Col 3
		add(new JLabel());
		//Col 4
		baseLabel.setFont(FONT_NINE);
		baseLabel.setText(BASE);
		add(baseLabel);
		//Col 5
		add(new JLabel());
		//Col 6
		abilityLabel.setFont(FONT_NINE);
		abilityLabel.setText(ABILITY);
		add(abilityLabel);
		//Col 7
		add(new JLabel());
		//Col 8
		magicLabel.setFont(FONT_NINE);
		magicLabel.setText(MAGIC);
		add(magicLabel);
		//Col 9
		add(new JLabel());
		//Col 10
		miscLabel.setFont(FONT_NINE);
		miscLabel.setText(MISC);
		add(miscLabel);
		//Col 11
		add(new JLabel());
		//Col 12
		epicLabel.setFont(FONT_NINE);
		epicLabel.setText(EPIC);
		add(epicLabel);
		//Col 13
		add(new JLabel());
		//Col 14
		tempLabel.setFont(FONT_NINE);
		tempLabel.setText(TEMP);
		add(tempLabel);
		//Col 15
		tempModsLabel.setFont(FONT_NINE);
		tempModsLabel.setText(TEMP_MODS);
		add(tempModsLabel);

		//Row 2
		//Col 1
		fortPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		fortLabel.setText(CheckToken.getNameToken(FORT).toUpperCase());
		fortPanel.add(fortLabel);
		conLabel.setFont(FONT_TEN);
		conLabel.setText(CON);
		fortPanel.add(conLabel);
		add(fortPanel);
		//Col 2
		add(getBonusPanel(fortTotalPanel, fortTotal));
		//Col 3
		add(new JLabel(EQUALS));
		//Col 4
		add(getBonusPanel(fortBasePanel, fortBase));
		//Col 5
		add(new JLabel(PLUS));
		//Col 6
		add(getBonusPanel(fortAbilityPanel, fortAbility));
		//Col 7
		add(new JLabel(PLUS));
		//Col 8
		add(getBonusPanel(fortMagicPanel, fortMagic));
		//Col 9
		add(new JLabel(PLUS));
		//Col 10
		add(getBonusPanel(fortMiscPanel, fortMisc));
		//Col 11
		add(new JLabel(PLUS));
		//Col 12
		add(getBonusPanel(fortEpicPanel, fortEpic));
		//Col 13
		add(new JLabel(PLUS));
		//Col 14
		add(getBonusPanel(fortTempPanel, fortTemp));
		//Col 15
		// SwingConstants.CENTER is equivalent to JTextField.CENTER but more
		// 'correct' in a Java coding context (it is a static reference)
		fortNotesTb.setHorizontalAlignment(SwingConstants.CENTER);
		add(fortNotesTb);

		//Row 3
		//Col 1
		refPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		refLabel.setText(CheckToken.getNameToken(REFLEX).toUpperCase());
		refPanel.add(refLabel);
		dexLabel.setFont(FONT_TEN);
		dexLabel.setText(DEX);
		refPanel.add(dexLabel);
		add(refPanel);
		//Col 2
		add(getBonusPanel(refTotalPanel, refTotal));
		//Col 3
		add(new JLabel(EQUALS));
		//Col 4
		add(getBonusPanel(refBasePanel, refBase));
		//Col 5
		add(new JLabel(PLUS));
		//Col 6
		add(getBonusPanel(refAbilityPanel, refAbility));
		//Col 7
		add(new JLabel(PLUS));
		//Col 8
		add(getBonusPanel(refMagicPanel, refMagic));
		//Col 9
		add(new JLabel(PLUS));
		//Col 10
		add(getBonusPanel(refMiscPanel, refMisc));
		//Col 11
		add(new JLabel(PLUS));
		//Col 12
		add(getBonusPanel(refEpicPanel, refEpic));
		//Col 13
		add(new JLabel(PLUS));
		//Col 14
		add(getBonusPanel(refTempPanel, refTemp));
		//Col 15
		// SwingConstants.CENTER is equivalent to JTextField.CENTER but more
		// 'correct' in a Java coding context (it is a static reference)
		refNotesTb.setHorizontalAlignment(SwingConstants.CENTER);
		add(refNotesTb);

		//Row 4
		//Col 1
		willPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		willLabel.setText(CheckToken.getNameToken(WILL).toUpperCase());
		willPanel.add(willLabel);
		wisLabel.setFont(FONT_TEN);
		wisLabel.setText(WIS);
		willPanel.add(wisLabel);
		add(willPanel);
		//Col 2
		add(getBonusPanel(willTotalPanel, willTotal));
		//Col 3
		add(new JLabel(EQUALS));
		//Col 4
		add(getBonusPanel(willBasePanel, willBase));
		//Col 5
		add(new JLabel(PLUS));
		//Col 6
		add(getBonusPanel(willAbilityPanel, willAbility));
		//Col 7
		add(new JLabel(PLUS));
		//Col 8
		add(getBonusPanel(willMagicPanel, willMagic));
		//Col 9
		add(new JLabel(PLUS));
		//Col 10
		add(getBonusPanel(willMiscPanel, willMisc));
		//Col 11
		add(new JLabel(PLUS));
		//Col 12
		add(getBonusPanel(willEpicPanel, willEpic));
		//Col 13
		add(new JLabel(PLUS));
		//Col 14
		add(getBonusPanel(willTempPanel, willTemp));
		//Col 15
		// SwingConstants.CENTER is equivalent to JTextField.CENTER but more
		// 'correct' in a Java coding context (it is a static reference)
		willNotesTb.setHorizontalAlignment(SwingConstants.CENTER);
		add(willNotesTb);
	}

	public void setColor()
	{
		setBackground(CharacterPanel.white);
		willNotesTb.setBorder(new LineBorder(CharacterPanel.black, 2));
		refNotesTb.setBorder(new LineBorder(CharacterPanel.black, 2));
		fortNotesTb.setBorder(new LineBorder(CharacterPanel.black, 2));
		fortEpicPanel.setBackground(CharacterPanel.white);
		fortEpicPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		refAbilityPanel.setBackground(CharacterPanel.white);
		refAbilityPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		willAbilityPanel.setBackground(CharacterPanel.white);
		willAbilityPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		willMagicPanel.setBackground(CharacterPanel.white);
		willMagicPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		refTotalPanel.setBackground(CharacterPanel.bodyLight);
		refTotalPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		refMagicPanel.setBackground(CharacterPanel.white);
		refMagicPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		refMiscPanel.setBackground(CharacterPanel.white);
		refMiscPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		willMiscPanel.setBackground(CharacterPanel.white);
		willMiscPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		fortMagicPanel.setBackground(CharacterPanel.white);
		fortMagicPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		refEpicPanel.setBackground(CharacterPanel.white);
		refEpicPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		willEpicPanel.setBackground(CharacterPanel.white);
		willEpicPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		fortMiscPanel.setBackground(CharacterPanel.white);
		fortMiscPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		refBasePanel.setBackground(CharacterPanel.white);
		refBasePanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		willBasePanel.setBackground(CharacterPanel.white);
		willBasePanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		fortAbilityPanel.setBackground(CharacterPanel.white);
		fortAbilityPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		fortBasePanel.setBackground(CharacterPanel.white);
		fortBasePanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		fortPanel.setBackground(CharacterPanel.bodyDark);
		fortPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		fortTotalPanel.setBackground(CharacterPanel.bodyLight);
		fortTotalPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		refTempPanel.setBackground(CharacterPanel.white);
		refTempPanel.setBorder(new LineBorder(CharacterPanel.lightGrey, 2));
		willTempPanel.setBackground(CharacterPanel.white);
		willTempPanel.setBorder(new LineBorder(CharacterPanel.lightGrey, 2));
		fortTempPanel.setBackground(CharacterPanel.white);
		fortTempPanel.setBorder(new LineBorder(CharacterPanel.lightGrey, 2));
		willTotalPanel.setBackground(CharacterPanel.bodyLight);
		willTotalPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		refPanel.setBackground(CharacterPanel.bodyDark);
		refPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		willPanel.setBackground(CharacterPanel.bodyDark);
		willPanel.setBorder(new LineBorder(CharacterPanel.border, 2));
		refTemp.setForeground(CharacterPanel.darkGrey);
		willTemp.setForeground(CharacterPanel.darkGrey);
		fortTemp.setForeground(CharacterPanel.darkGrey);
	}

	private JPanel getBonusPanel(JPanel panel, JLabel label)
	{
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
		label.setFont(FONT_TEN);
		label.setText(SPACE);
		panel.add(label);
		return panel;
	}

	public void setPc(PlayerCharacter pc)
	{
		this.pc = pc;
	}

	public void refresh()
	{
		fortTotal.setText(Delta.toString(CheckToken.getCheckToken(pc, FORT,
			TOTAL_TOKEN)));
		fortBase.setText(Delta.toString(CheckToken.getCheckToken(pc, FORT,
			BASE_TOKEN)));
		PCStat con = Globals.getContext().ref.getAbbreviatedObject(
				PCStat.class, "CON");
		fortAbility.setText(StatToken.getModToken(pc, con));
		fortMagic.setText(Delta.toString(CheckToken.getCheckToken(pc, FORT,
			MAGIC_TOKEN)));
		fortMisc.setText(Delta.toString(CheckToken.getCheckToken(pc, FORT,
			MISC_TOKEN)));
		fortEpic.setText(Delta.toString(CheckToken.getCheckToken(pc, FORT,
			EPIC_TOKEN)));
		fortTemp.setText(PLUS_ZERO);

		refTotal.setText(Delta.toString(CheckToken.getCheckToken(pc, REFLEX,
			TOTAL_TOKEN)));
		refBase.setText(Delta.toString(CheckToken.getCheckToken(pc, REFLEX,
			BASE_TOKEN)));
		PCStat dex = Globals.getContext().ref.getAbbreviatedObject(
				PCStat.class, "DEX");
		refAbility.setText(StatToken.getModToken(pc, dex));
		refMagic.setText(Delta.toString(CheckToken.getCheckToken(pc, REFLEX,
			MAGIC_TOKEN)));
		refMisc.setText(Delta.toString(CheckToken.getCheckToken(pc, REFLEX,
			MISC_TOKEN)));
		refEpic.setText(Delta.toString(CheckToken.getCheckToken(pc, REFLEX,
			EPIC_TOKEN)));
		refTemp.setText(PLUS_ZERO);

		willTotal.setText(Delta.toString(CheckToken.getCheckToken(pc, WILL,
			TOTAL_TOKEN)));
		willBase.setText(Delta.toString(CheckToken.getCheckToken(pc, WILL,
			BASE_TOKEN)));
		PCStat wis = Globals.getContext().ref.getAbbreviatedObject(
				PCStat.class, "WIS");
		willAbility.setText(StatToken.getModToken(pc, wis));
		willMagic.setText(Delta.toString(CheckToken.getCheckToken(pc, WILL,
			MAGIC_TOKEN)));
		willMisc.setText(Delta.toString(CheckToken.getCheckToken(pc, WILL,
			MISC_TOKEN)));
		willEpic.setText(Delta.toString(CheckToken.getCheckToken(pc, WILL,
			EPIC_TOKEN)));
		willTemp.setText(PLUS_ZERO);
	}

	public void destruct()
	{
		//Put any code here that is needed to prevent memory leaks when this panel is destroyed
	}
}
