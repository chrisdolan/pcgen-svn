package plugin.lsttokens.pcclass;

import java.util.Collection;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import pcgen.base.lang.StringUtil;
import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.PCClass;
import pcgen.core.SpellProhibitor;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.enumeration.ProhibitedSpellType;

/**
 * Class deals with PROHIBITED Token
 */
public class ProhibitedToken extends AbstractToken implements
		CDOMPrimaryToken<PCClass>
{

	@Override
	public String getTokenName()
	{
		return "PROHIBITED";
	}

	public boolean parse(LoadContext context, PCClass pcc, String value)
	{
		if (isEmpty(value) || hasIllegalSeparator(',', value))
		{
			return false;
		}
		StringTokenizer elements = new StringTokenizer(value, Constants.COMMA);
		while (elements.hasMoreTokens())
		{
			String aValue = elements.nextToken();
			if (!aValue.equalsIgnoreCase("None"))
			{
				SpellProhibitor prohibSchool = new SpellProhibitor();
				prohibSchool.setType(ProhibitedSpellType.SCHOOL);
				prohibSchool.addValue(aValue);
				context.getObjectContext().addToList(pcc,
					ListKey.PROHIBITED_SPELLS, prohibSchool);
				SpellProhibitor prohibSubSchool = new SpellProhibitor();
				prohibSubSchool.setType(ProhibitedSpellType.SUBSCHOOL);
				prohibSubSchool.addValue(aValue);
				context.getObjectContext().addToList(pcc,
					ListKey.PROHIBITED_SPELLS, prohibSubSchool);
			}
		}
		return true;
	}

	public String[] unparse(LoadContext context, PCClass pcc)
	{
		Changes<SpellProhibitor> changes =
				context.getObjectContext().getListChanges(pcc,
					ListKey.PROHIBITED_SPELLS);
		Collection<SpellProhibitor> added = changes.getAdded();
		if (added == null || added.isEmpty())
		{
			// Zero indicates no Token present
			return null;
		}
		Set<String> set = new TreeSet<String>();
		for (SpellProhibitor sp : added)
		{
			set.addAll(sp.getValueList());
		}
		return new String[]{StringUtil.join(set, Constants.COMMA)};
	}

	public Class<PCClass> getTokenClass()
	{
		return PCClass.class;
	}

}
