package plugin.lsttokens.campaign;

import java.net.URI;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Campaign;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.CampaignSourceEntry;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;

/**
 * Class deals with WEAPONPROF Token
 */
public class WeaponprofToken extends AbstractToken implements CDOMPrimaryToken<Campaign>
{

	public String getTokenName()
	{
		return "WEAPONPROF";
	}

	public boolean parse(Campaign campaign, String value, URI sourceUri)
	{
		campaign.addLine("WEAPONPROF:" + value);
		campaign.addToListFor(ListKey.FILE_WEAPON_PROF, CampaignSourceEntry
			.getNewCSE(campaign, sourceUri, value));
		return true;
	}

	public boolean parse(LoadContext context, Campaign obj, String value)
		throws PersistenceLayerException
	{
		if (isEmpty(value) || hasIllegalSeparator('|', value))
		{
			return false;
		}
		CampaignSourceEntry cse = context.getCampaignSourceEntry(obj, value);
		if (cse == null)
		{
			//Error
			return false;
		}
		context.obj.addToList(obj, ListKey.FILE_WEAPON_PROF, cse);
		return true;
	}

	public String[] unparse(LoadContext context, Campaign obj)
	{
		Changes<CampaignSourceEntry> cseChanges =
				context.obj.getListChanges(obj, ListKey.FILE_WEAPON_PROF);
		Collection<CampaignSourceEntry> added = cseChanges.getAdded();
		if (added == null)
		{
			//empty indicates no WEAPONPROF token
			return null;
		}
		Set<String> set = new TreeSet<String>();
		for (CampaignSourceEntry cse : added)
		{
			set.add(cse.getLSTformat());
		}
		return set.toArray(new String[set.size()]);
	}

	public Class<Campaign> getTokenClass()
	{
		return Campaign.class;
	}

}
