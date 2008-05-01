/*
 * Created on Sep 2, 2005
 *
 */
package plugin.lsttokens;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.core.PCClass;
import pcgen.core.PObject;
import pcgen.core.Vision;
import pcgen.core.prereq.Prerequisite;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.GlobalLstToken;
import pcgen.persistence.lst.prereq.PreParserFactory;
import pcgen.util.Logging;
import pcgen.util.enumeration.VisionType;

/**
 * <code>VisionLst</code> handles the processing of the VISION tag in LST
 * code.
 * 
 * Last Editor: $Author$ 
 * Last Edited: $Date$
 * 
 * @author Devon Jones
 * @version $Revision$
 */
public class VisionLst implements GlobalLstToken
{

	/**
	 * @see pcgen.persistence.lst.LstToken#getTokenName()
	 */
	public String getTokenName()
	{
		return "VISION";
	}

	/**
	 * @see pcgen.persistence.lst.GlobalLstToken#parse(pcgen.core.PObject,
	 *      java.lang.String, int)
	 */
	public boolean parse(PObject obj, String value, int anInt) throws PersistenceLayerException
	{
 		final StringTokenizer aTok = new StringTokenizer(value, "|");
		final List<Prerequisite> prereqs = new ArrayList<Prerequisite>();
		final List<Vision> visions = new ArrayList<Vision>();
		
		while (aTok.hasMoreTokens())
		{
			String visionString = aTok.nextToken();

			if (".CLEAR".equals(visionString))
			{
				obj.clearVisionList();
				continue;
			}

			if (visionString.startsWith("PRE"))
			{
				final PreParserFactory factory = PreParserFactory.getInstance();
				final Prerequisite prereq = factory.parse(visionString);
				prereqs.add(prereq);
			}
			else 
			{	
				if (visionString.indexOf(',') >= 0)
				{
					Logging
						.deprecationPrint("Use of comma in VISION Tag is deprecated.  Use .CLEAR.[Vision] instead.");
					final StringTokenizer visionTok =
							new StringTokenizer(visionString, ",");
					String numberTok = visionTok.nextToken();
					if (numberTok == "2")
					{
						visionString = ".CLEAR." + visionTok.nextToken();
					}
					else if (numberTok == "0")
					{
						visionString = ".SET." + visionTok.nextToken();
					}
					else
					{
						visionString = visionTok.nextToken();
					}
				}
	
				Vision vis = null;
				if (visionString.startsWith(".CLEAR."))
				{
					obj.removeVisionType(VisionType.getVisionType(visionString
						.substring(7)));
				}
				else if (visionString.startsWith(".SET."))
				{
					obj.clearVisionList();
					vis = getVision(anInt, visionString.substring(5));
				}
				else
				{
					vis = getVision(anInt, visionString);
				}
	
				if (vis != null)
				{
					visions.add(vis);
				}
			}

		}
		for (Vision vis: visions)
		{
			for (Prerequisite prereq : prereqs)
			{
				vis.addPreReq(prereq);
			}

			if (anInt > -9)
			{
				((PCClass) obj).addVision(anInt, vis);
			}
			else
			{
				obj.addVision(vis);
			}
		}

		return true;
	}

	private Vision getVision(int anInt, String visionType)
	{
		// expecting value in form of Darkvision (60')
		final StringTokenizer cTok = new StringTokenizer(visionType, "(')");
		final String aKey = cTok.nextToken().trim(); // e.g. Darkvision
		String aVal = "0";
		if (cTok.hasMoreTokens())
		{
			aVal = cTok.nextToken(); // e.g. 60
		}
		return new Vision(VisionType.getVisionType(aKey), aVal);
	}
}
