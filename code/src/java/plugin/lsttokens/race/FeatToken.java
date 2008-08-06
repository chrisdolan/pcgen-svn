package plugin.lsttokens.race;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.base.util.MapToList;
import pcgen.cdom.base.AssociatedPrereqObject;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.AbilityUtilities;
import pcgen.core.Race;
import pcgen.rules.context.AssociatedChanges;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.TokenUtilities;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;

/**
 * Class deals with FEAT Token
 */
public class FeatToken extends AbstractToken implements CDOMPrimaryToken<Race>
{
	public static final Class<Ability> ABILITY_CLASS = Ability.class;

	@Override
	public String getTokenName()
	{
		return "FEAT";
	}

	public boolean parse(LoadContext context, Race pct, String value)
	{
		return parseFeat(context, pct, value);
	}

	public boolean parseFeat(LoadContext context, CDOMObject obj, String value)
	{
		if (isEmpty(value) || hasIllegalSeparator('|', value))
		{
			return false;
		}

		StringTokenizer tok = new StringTokenizer(value, Constants.PIPE);

		boolean first = true;

		while (tok.hasMoreTokens())
		{
			String token = tok.nextToken();
			if (Constants.LST_DOT_CLEAR.equals(token))
			{
				if (!first)
				{
					Logging.errorPrint("  Non-sensical " + getTokenName()
							+ ": .CLEAR was not the first list item: " + value);
					return false;
				}
				context.getListContext().removeAllFromList(getTokenName(), obj,
						Ability.FEATLIST);
			}
			else
			{
				CDOMReference<Ability> ability = TokenUtilities
						.getTypeOrPrimitive(context, ABILITY_CLASS,
								AbilityCategory.FEAT, token);
				if (ability == null)
				{
					return false;
				}
				AssociatedPrereqObject assoc = context.getListContext()
						.addToList(getTokenName(), obj, Ability.FEATLIST,
								ability);
				assoc.setAssociation(AssociationKey.NATURE,
						Ability.Nature.AUTOMATIC);
				if (token.indexOf('(') != -1)
				{
					List<String> choices = new ArrayList<String>();
					AbilityUtilities.getUndecoratedName(token, choices);
					assoc.setAssociation(AssociationKey.ASSOC_CHOICES, choices);
				}
			}
			first = false;
		}
		return true;
	}

	public String[] unparse(LoadContext context, Race pct)
	{
		AssociatedChanges<CDOMReference<Ability>> changes = context
				.getListContext().getChangesInList(getTokenName(), pct,
						Ability.FEATLIST);
		MapToList<CDOMReference<Ability>, AssociatedPrereqObject> mtl = changes
				.getAddedAssociations();
		if (mtl == null || mtl.isEmpty())
		{
			// Zero indicates no Token
			return null;
		}
		MapToList<CDOMReference<Ability>, AssociatedPrereqObject> added = changes
				.getAddedAssociations();
		Collection<CDOMReference<Ability>> removedItems = changes.getRemoved();
		StringBuilder sb = new StringBuilder();
		if (changes.includesGlobalClear())
		{
			if (removedItems != null && !removedItems.isEmpty())
			{
				context.addWriteMessage("Non-sensical relationship in "
						+ getTokenName()
						+ ": global .CLEAR and local .CLEAR. performed");
				return null;
			}
			sb.append(Constants.LST_DOT_CLEAR);
		}
		else if (removedItems != null && !removedItems.isEmpty())
		{
			context.addWriteMessage(getTokenName() + " does not support "
					+ Constants.LST_DOT_CLEAR_DOT);
			return null;
		}
		if (added != null && !added.isEmpty())
		{
			boolean needsPipe = sb.length() != 0;
			for (CDOMReference<Ability> ref : added.getKeySet())
			{
				String lstFormat = ref.getLSTformat();
				for (int i = 0; i < added.sizeOfListFor(ref); i++)
				{
					if (needsPipe)
					{
						sb.append(Constants.PIPE);
					}
					needsPipe = true;
					sb.append(lstFormat);
				}
			}
		}
		if (sb.length() == 0)
		{
			return null;
		}
		return new String[] { sb.toString() };
	}

	public Class<Race> getTokenClass()
	{
		return Race.class;
	}

}
