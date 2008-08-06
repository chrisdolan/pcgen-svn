package plugin.lsttokens.template;

import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import pcgen.base.lang.StringUtil;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Category;
import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.SubClassCategory;
import pcgen.cdom.reference.CategorizedCDOMReference;
import pcgen.core.PCClass;
import pcgen.core.PCTemplate;
import pcgen.core.SubClass;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;

/**
 * Class deals with FAVOREDCLASS Token
 */
public class FavoredclassToken extends AbstractToken implements
		CDOMPrimaryToken<PCTemplate>
{

	public static final Class<PCClass> PCCLASS_CLASS = PCClass.class;
	public static final Class<SubClass> SUBCLASS_CLASS = SubClass.class;

	@Override
	public String getTokenName()
	{
		return "FAVOREDCLASS";
	}

	public boolean parse(LoadContext context, PCTemplate pct, String value)
	{
		if (isEmpty(value) || hasIllegalSeparator('|', value))
		{
			return false;
		}

		return parseFavoredClass(context, pct, value);
	}

	public boolean parseFavoredClass(LoadContext context, CDOMObject cdo,
			String value)
	{
		boolean foundAny = false;
		boolean foundOther = false;

		StringTokenizer tok = new StringTokenizer(value, Constants.PIPE);

		while (tok.hasMoreTokens())
		{
			String token = tok.nextToken();
			if (Constants.LST_ALL.equalsIgnoreCase(token)
					|| Constants.LST_ANY.equalsIgnoreCase(token))
			{
				foundAny = true;
				context.getObjectContext().put(cdo,
						ObjectKey.ANY_FAVORED_CLASS, true);
			}
			else
			{
				CDOMReference<? extends PCClass> ref;
				foundOther = true;
				int dotLoc = token.indexOf('.');
				if (dotLoc == -1)
				{
					// Primitive
					ref = context.ref.getCDOMReference(PCCLASS_CLASS, token);
				}
				else
				{
					// SubClass
					String parent = token.substring(0, dotLoc);
					String subclass = token.substring(dotLoc + 1);
					SubClassCategory scc = SubClassCategory.getConstant(parent);
					ref = context.ref.getCDOMReference(SUBCLASS_CLASS, scc,
							subclass);
				}
				context.getObjectContext().addToList(cdo,
						ListKey.FAVORED_CLASS, ref);
			}
		}
		if (foundAny && foundOther)
		{
			Logging.errorPrint("Non-sensical " + getTokenName()
					+ ": Contains ANY and a specific reference: " + value);
			return false;
		}
		return true;
	}

	public String[] unparse(LoadContext context, PCTemplate pct)
	{
		Changes<CDOMReference<? extends PCClass>> changes = context
				.getObjectContext().getListChanges(pct, ListKey.FAVORED_CLASS);
		if (changes == null || changes.isEmpty())
		{
			return null;
		}
		SortedSet<String> set = new TreeSet<String>();
		for (CDOMReference<? extends PCClass> ref : changes.getAdded())
		{
			Class<? extends PCClass> refClass = ref.getReferenceClass();
			if (SUBCLASS_CLASS.equals(refClass))
			{
				Category<SubClass> parent = ((CategorizedCDOMReference<SubClass>) ref)
						.getCDOMCategory();
				set.add(parent.toString() + "." + ref.getLSTformat());
			}
			else
			{
				set.add(ref.getLSTformat());
			}
		}
		return new String[] { StringUtil.join(set, Constants.PIPE) };
	}

	public Class<PCTemplate> getTokenClass()
	{
		return PCTemplate.class;
	}
}
