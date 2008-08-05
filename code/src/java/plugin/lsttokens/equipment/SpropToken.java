package plugin.lsttokens.equipment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Equipment;
import pcgen.core.SpecialProperty;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;

/**
 * Deals with SPROP token
 */
public class SpropToken extends AbstractToken implements
		CDOMPrimaryToken<Equipment>
{

	@Override
	public String getTokenName()
	{
		return "SPROP";
	}

	public boolean parse(LoadContext context, Equipment eq, String value)
	{
		if (isEmpty(value) || hasIllegalSeparator('|', value))
		{
			return false;
		}
		if (Constants.LST_DOT_CLEAR.equals(value))
		{
			context.getObjectContext().removeList(eq,
					ListKey.SPECIAL_PROPERTIES);
			return true;
		}

		SpecialProperty sa = SpecialProperty.createFromLst(value);
		if (sa == null)
		{
			return false;
		}
		context.getObjectContext()
				.addToList(eq, ListKey.SPECIAL_PROPERTIES, sa);
		return true;
	}

	public String[] unparse(LoadContext context, Equipment eq)
	{
		Changes<SpecialProperty> changes = context.getObjectContext()
				.getListChanges(eq, ListKey.SPECIAL_PROPERTIES);
		Collection<SpecialProperty> added = changes.getAdded();
		if (added == null || added.isEmpty())
		{
			// Zero indicates no Token
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (SpecialProperty sp : added)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(sp.getDisplayName());
			if (sp.hasPrerequisites())
			{
				sb.append(Constants.PIPE);
				sb.append(getPrerequisiteString(context, sp
						.getPrerequisiteList()));
			}
			list.add(sb.toString());
		}
		return list.toArray(new String[list.size()]);
	}

	public Class<Equipment> getTokenClass()
	{
		return Equipment.class;
	}
}
