/*
 * WeightPane.java
 *
 * Created on February 22, 2004, 4:09 PM
 */

package plugin.charactersheet.gui;

import gmgen.gui.GridBoxLayout;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.display.CharacterDisplay;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * <code>WeightPane</code>.
 *
 * @author  soulcatcher
 * @version	$Revision$
 */
public class WeightPane extends JPanel
{
	private PlayerCharacter pc;

	private JPanel weightAllowancePanel;

	private JPanel weightPanel;
	private JPanel lightPanel;
	private JPanel lightValuePanel;
	private JLabel light;
	private JPanel mediumPanel;
	private JPanel mediumValuePanel;
	private JLabel medium;
	private JPanel heavyPanel;
	private JPanel heavyValuePanel;
	private JLabel heavy;
	private JPanel liftHeadPanel;
	private JPanel liftHeadValuePanel;
	private JLabel liftHead;
	private JPanel liftGroundPanel;
	private JPanel liftGroundValuePanel;
	private JLabel liftGround;
	private JPanel dragPanel;
	private JPanel dragValuePanel;
	private JLabel drag;

	private static final String DIALOG = "Dialog";
	private static final String SPACE = " ";
	private static final String WEIGHT_ALLOWANCE = "WEIGHT ALLOWANCE";
	private static final String LIGHT = "Light";
	private static final String MEDIUM = "Medium";
	private static final String HEAVY = "Heavy";
	private static final String LIFT_HEAD = "Lift Over Head";
	private static final String LIFT_GROUND = "Lift Off Ground";
	private static final String PUSH_DRAG = "Push / Drag";
	private static final Font FONT_TEN = new Font(DIALOG, 0, 10);

	/** Creates new form WeightPane */
	public WeightPane()
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
		weightAllowancePanel = new JPanel();
		weightPanel = new JPanel();
		lightPanel = new JPanel();
		lightValuePanel = new JPanel();
		light = new JLabel();
		mediumPanel = new JPanel();
		mediumValuePanel = new JPanel();
		medium = new JLabel();
		heavyPanel = new JPanel();
		heavyValuePanel = new JPanel();
		heavy = new JLabel();
		liftHeadPanel = new JPanel();
		liftHeadValuePanel = new JPanel();
		liftHead = new JLabel();
		liftGroundPanel = new JPanel();
		liftGroundValuePanel = new JPanel();
		liftGround = new JLabel();
		dragPanel = new JPanel();
		dragValuePanel = new JPanel();
		drag = new JLabel();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel weightAllowanceLabel = new JLabel();
		weightAllowancePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		weightAllowanceLabel.setFont(new Font(DIALOG, 1, 14));
		weightAllowanceLabel.setText(WEIGHT_ALLOWANCE);
		weightAllowancePanel.add(weightAllowanceLabel);
		add(weightAllowancePanel);

		weightPanel.setLayout(new GridBoxLayout(2, 6));

		JLabel lightLabel = new JLabel();
		lightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		lightLabel.setFont(FONT_TEN);
		lightLabel.setText(LIGHT);
		lightPanel.add(lightLabel);
		weightPanel.add(lightPanel);

		lightValuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		light.setFont(FONT_TEN);
		light.setText(SPACE);
		lightValuePanel.add(light);
		weightPanel.add(lightValuePanel);

		JLabel mediumLabel = new JLabel();
		mediumPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		mediumLabel.setFont(FONT_TEN);
		mediumLabel.setText(MEDIUM);
		mediumPanel.add(mediumLabel);
		weightPanel.add(mediumPanel);

		mediumValuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		medium.setFont(FONT_TEN);
		medium.setText(SPACE);
		mediumValuePanel.add(medium);
		weightPanel.add(mediumValuePanel);

		JLabel heavyLabel = new JLabel();
		heavyPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		heavyLabel.setFont(FONT_TEN);
		heavyLabel.setText(HEAVY);
		heavyPanel.add(heavyLabel);
		weightPanel.add(heavyPanel);

		heavyValuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		heavy.setFont(FONT_TEN);
		heavy.setText(SPACE);
		heavyValuePanel.add(heavy);
		weightPanel.add(heavyValuePanel);

		JLabel liftHeadLabel = new JLabel();
		liftHeadPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		liftHeadLabel.setFont(FONT_TEN);
		liftHeadLabel.setText(LIFT_HEAD);
		liftHeadPanel.add(liftHeadLabel);
		weightPanel.add(liftHeadPanel);

		liftHeadValuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		liftHead.setFont(new Font(DIALOG, 0, 10));
		liftHead.setText(SPACE);
		liftHeadValuePanel.add(liftHead);
		weightPanel.add(liftHeadValuePanel);

		JLabel liftGroundLabel = new JLabel();
		liftGroundPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		liftGroundLabel.setFont(FONT_TEN);
		liftGroundLabel.setText(LIFT_GROUND);
		liftGroundPanel.add(liftGroundLabel);
		weightPanel.add(liftGroundPanel);

		liftGroundValuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		liftGround.setFont(FONT_TEN);
		liftGround.setText(SPACE);
		liftGroundValuePanel.add(liftGround);
		weightPanel.add(liftGroundValuePanel);

		JLabel dragLabel = new JLabel();
		dragPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		dragLabel.setFont(FONT_TEN);
		dragLabel.setText(PUSH_DRAG);
		dragPanel.add(dragLabel);
		weightPanel.add(dragPanel);

		dragValuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		drag.setFont(FONT_TEN);
		drag.setText(SPACE);
		dragValuePanel.add(drag);
		weightPanel.add(dragValuePanel);

		add(weightPanel);
	}

	/**
	 * Sets the background and border colour of the WeightPane and sub-components.
	 */
	public void setColor()
	{
		setBackground(CharacterPanel.header);
		weightPanel.setBackground(CharacterPanel.border);
		weightAllowancePanel.setBackground(CharacterPanel.header);
		lightPanel.setBackground(CharacterPanel.bodyMedLight);
		liftHeadPanel.setBackground(CharacterPanel.bodyMedLight);
		lightValuePanel.setBackground(CharacterPanel.bodyLight);
		liftHeadValuePanel.setBackground(CharacterPanel.bodyLight);
		mediumPanel.setBackground(CharacterPanel.bodyMedLight);
		liftGroundPanel.setBackground(CharacterPanel.bodyMedLight);
		mediumValuePanel.setBackground(CharacterPanel.bodyLight);
		liftGroundValuePanel.setBackground(CharacterPanel.bodyLight);
		heavyPanel.setBackground(CharacterPanel.bodyMedLight);
		dragPanel.setBackground(CharacterPanel.bodyMedLight);
		heavyValuePanel.setBackground(CharacterPanel.bodyLight);
		dragValuePanel.setBackground(CharacterPanel.bodyLight);

		weightAllowancePanel.setBorder(new LineBorder(CharacterPanel.border));
		lightPanel.setBorder(new LineBorder(CharacterPanel.border));
		liftHeadPanel.setBorder(new LineBorder(CharacterPanel.border));
		lightValuePanel.setBorder(new LineBorder(CharacterPanel.border));
		liftHeadValuePanel.setBorder(new LineBorder(CharacterPanel.border));
		mediumPanel.setBorder(new LineBorder(CharacterPanel.border));
		liftGroundPanel.setBorder(new LineBorder(CharacterPanel.border));
		mediumValuePanel.setBorder(new LineBorder(CharacterPanel.border));
		liftGroundValuePanel.setBorder(new LineBorder(CharacterPanel.border));
		heavyPanel.setBorder(new LineBorder(CharacterPanel.border));
		dragPanel.setBorder(new LineBorder(CharacterPanel.border));
		heavyValuePanel.setBorder(new LineBorder(CharacterPanel.border));
		dragValuePanel.setBorder(new LineBorder(CharacterPanel.border));
	}

	/**
	 * Set the player character.
	 * @param pc player character
	 */
	public void setPc(PlayerCharacter pc)
	{
		this.pc = pc;
	}

	/**
	 * Refresh the weight pane.
	 */
	public void refresh()
	{
		CharacterDisplay display = pc.getDisplay();
		light.setText(Globals.getGameModeUnitSet().displayWeightInUnitSet(
			new Double(display.getLoadToken("Light")).doubleValue()));
		medium.setText(Globals.getGameModeUnitSet().displayWeightInUnitSet(
			new Double(display.getLoadToken("Medium")).doubleValue()));
		heavy.setText(Globals.getGameModeUnitSet().displayWeightInUnitSet(
			new Double(display.getLoadToken("Heavy")).doubleValue()));
		liftHead
			.setText(Globals.getGameModeUnitSet().displayWeightInUnitSet(
				new Double(display.getLoadToken("OverHead"))
					.doubleValue()));
		liftGround.setText(Globals.getGameModeUnitSet()
			.displayWeightInUnitSet(
				new Double(display.getLoadToken("OffGround"))
					.doubleValue()));
		drag
			.setText(Globals.getGameModeUnitSet().displayWeightInUnitSet(
				new Double(display.getLoadToken("PushDrag"))
					.doubleValue()));
	}

	/**
	 * Destroy the weight pane, no code at present
	 */
	public void destruct()
	{
		//Put any code here that is needed to prevent memory leaks when this panel is destroyed
	}
}
