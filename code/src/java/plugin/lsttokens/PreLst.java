/*
 * Created on Sep 2, 2005
 *
 */
package plugin.lsttokens;

import pcgen.cdom.base.ConcretePrereqObject;
import pcgen.cdom.base.Constants;
import pcgen.core.prereq.Prerequisite;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.rules.persistence.token.ParseResult;

/**
 * @author djones4
 *
 */
public class PreLst extends AbstractToken implements
		CDOMPrimaryToken<ConcretePrereqObject>
{
	@Override
	public String getTokenName()
	{
		return "PRE";
	}

	public ParseResult parseToken(LoadContext context, ConcretePrereqObject pcc,
		String value)
	{
		if (Constants.LST_DOT_CLEAR.equals(value))
		{
			context.obj.clearPrerequisiteList(pcc);
			return ParseResult.SUCCESS;
		}
		return ParseResult.INTERNAL_ERROR;
	}

	public String[] unparse(LoadContext context, ConcretePrereqObject pcc)
	{
		Changes<Prerequisite> changes = context.obj.getPrerequisiteChanges(pcc);
		if (changes == null || !changes.includesGlobalClear())
		{
			// indicates no Token
			return null;
		}
		return new String[] { Constants.LST_DOT_CLEAR };
	}

	public Class<ConcretePrereqObject> getTokenClass()
	{
		return ConcretePrereqObject.class;
	}
}
