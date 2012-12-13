/*
 * UnarmedPane.java
 *
 * Created on February 3, 2004, 3:23 PM
 */

package plugin.charactersheet.gui;

import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;
import pcgen.io.exporttoken.WeaponToken;
import pcgen.io.exporttoken.WeaponhToken;

import java.awt.Font;

/**
 * Confirmed no memory Leaks Dec 10, 2004
 * @author  ddjone3
 */
public class UnarmedPane extends javax.swing.JPanel
{
	private PlayerCharacter pc;

	private static final Font FONT_SIXTEEN = new Font("Dialog", 1, 16);
	private static final Font FONT_TEN = new Font("Dialog", 0, 10);
	private static final String UNARMED = "    Unarmed    ";
	private static final String TOTAL_ATTACK_BONUS = "Total Attack Bonus";
	private static final String SPACE = " ";
	private static final String DAMAGE = "Damage";
	private static final String CRITICAL = "Critical";

	/** Creates new form UnarmedPane */
	public UnarmedPane()
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
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		jPanel50 = new javax.swing.JPanel();
		totalAttackBonus = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		jPanel6 = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		jPanel49 = new javax.swing.JPanel();
		damage = new javax.swing.JLabel();
		jPanel8 = new javax.swing.JPanel();
		jPanel9 = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		jPanel48 = new javax.swing.JPanel();
		critical = new javax.swing.JLabel();

		setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

		jLabel1.setFont(FONT_SIXTEEN);
		jLabel1.setText(UNARMED);
		jPanel1.add(jLabel1);

		add(jPanel1);

		jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2,
			javax.swing.BoxLayout.Y_AXIS));

		jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel2.setFont(FONT_TEN);
		jLabel2.setText(TOTAL_ATTACK_BONUS);
		jPanel5.add(jLabel2);

		jPanel2.add(jPanel5);

		jPanel50.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		totalAttackBonus.setFont(FONT_TEN);
		totalAttackBonus.setText(SPACE);
		jPanel50.add(totalAttackBonus);

		jPanel2.add(jPanel50);

		add(jPanel2);

		jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3,
			javax.swing.BoxLayout.Y_AXIS));

		jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel5.setFont(FONT_TEN);
		jLabel5.setText(DAMAGE);
		jPanel6.add(jLabel5);

		jPanel3.add(jPanel6);

		jPanel49.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		damage.setFont(FONT_TEN);
		damage.setText(SPACE);
		jPanel49.add(damage);

		jPanel3.add(jPanel49);

		add(jPanel3);

		jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8,
			javax.swing.BoxLayout.Y_AXIS));

		jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		jLabel6.setFont(FONT_TEN);
		jLabel6.setText(CRITICAL);
		jPanel9.add(jLabel6);

		jPanel8.add(jPanel9);

		jPanel48.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
			1, 0));

		critical.setFont(FONT_TEN);
		critical.setText(SPACE);
		jPanel48.add(critical);

		jPanel8.add(jPanel48);

		add(jPanel8);

	}//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel critical;
	private javax.swing.JLabel damage;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel48;
	private javax.swing.JPanel jPanel49;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel50;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JLabel totalAttackBonus;

	// End of variables declaration//GEN-END:variables

	/**
	 * Set the color of this pane
	 */
	public void setColor()
	{
		setBackground(CharacterPanel.border);
		jPanel1.setBackground(CharacterPanel.header);
		jPanel2.setBackground(CharacterPanel.border);
		jPanel5.setBackground(CharacterPanel.header);
		jPanel50.setBackground(CharacterPanel.bodyLight);
		jPanel3.setBackground(CharacterPanel.border);
		jPanel6.setBackground(CharacterPanel.header);
		jPanel49.setBackground(CharacterPanel.bodyLight);
		jPanel8.setBackground(CharacterPanel.border);
		jPanel9.setBackground(CharacterPanel.header);
		jPanel48.setBackground(CharacterPanel.bodyLight);
		setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));
		jPanel1.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		jPanel5.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		jPanel50.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		jPanel6.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		jPanel49.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		jPanel9.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
		jPanel48.setBorder(new javax.swing.border.LineBorder(
			CharacterPanel.border));
	}

	/**
	 * Set the PC for this pane
	 * @param pc
	 */
	public void setPc(PlayerCharacter pc)
	{
		this.pc = pc;
	}

	/**
	 * Refresh this pane
	 */
	public void refresh()
	{
		try
		{
			Equipment eq = WeaponhToken.getWeaponEquipment(pc.getDisplay());
			totalAttackBonus.setText(WeaponToken.getTotalHitToken(pc, eq));
			damage.setText(WeaponToken.getDamageToken(pc, eq, false, false));
			StringBuilder sb = new StringBuilder();
			critical.setText(sb.append(WeaponToken.getCritToken(pc, eq))
				.append('/').append('X').append(
					WeaponToken.getMultToken(pc, eq)).toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Destroy
	 */
	public void destruct()
	{
		//Put any code here that is needed to prevent memory leaks when this panel is destroyed
	}
}
