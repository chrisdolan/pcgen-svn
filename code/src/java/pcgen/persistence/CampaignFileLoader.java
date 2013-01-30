/*
 * CampaignFileLoader.java
 * Copyright 2010 Connor Petty <cpmeister@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Created on Apr 15, 2010, 4:00:56 PM
 */
package pcgen.persistence;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import pcgen.core.Campaign;
import pcgen.core.Globals;
import pcgen.persistence.lst.CampaignLoader;
import pcgen.system.ConfigurationSettings;
import pcgen.system.LanguageBundle;
import pcgen.system.PCGenTask;
import pcgen.util.Logging;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class CampaignFileLoader extends PCGenTask
{
	public interface CampaignFilter
	{
		boolean acceptPccFile(File campaignFile);
		boolean acceptCampaign(Campaign campaign);
	}

	private File alternateSourceFolder = null;
	
	private final FileFilter pccFileFilter = new FileFilter()
	{

        @Override
		public boolean accept(File aFile)
		{
        	String fileName = aFile.getName();
			/*
			 * This is a specific "hack" in order to speed loading when
			 * in a development (Subversion-based) environment - Tom
			 * Parker 1/17/07
			 */
			if (".svn".equals(fileName))
			{
				return false;
			}
			if (StringUtils.endsWithIgnoreCase(fileName, ".pcc"))
			{
				return campaignFilter == null || campaignFilter.acceptPccFile(aFile);
			}
			return aFile.isDirectory();
		}

	};
	private LinkedList<URI> campaignFiles = new LinkedList<URI>();
	private boolean isLoading = false;
	private CampaignLoader campaignLoader = new CampaignLoader()
	{
		protected void finishCampaign(Campaign campaign)
		{
			if (!isLoading || null == campaignFilter || campaignFilter.acceptCampaign(campaign))
				super.finishCampaign(campaign);
		}
	};
	private final CampaignFilter campaignFilter;

	public CampaignFileLoader() {
		this.campaignFilter = null;
	}
	public CampaignFileLoader(CampaignFilter campaignFilter) {
		this.campaignFilter = campaignFilter;
	}

	@Override
	public String getMessage()
	{
		return LanguageBundle.getString("in_taskLoadCampaigns"); //$NON-NLS-1$
	}

	@Override
	public void execute()
	{
		// Load the initial campaigns
		if (alternateSourceFolder != null)
		{
			findPCCFiles(alternateSourceFolder);
		}
		else
		{
			findPCCFiles(new File(ConfigurationSettings.getPccFilesDir()));
			final String vendorDataDir = ConfigurationSettings.getVendorDataDir();
			if (vendorDataDir != null)
			{
				findPCCFiles(new File(vendorDataDir));
			}
		}
		setMaximum(campaignFiles.size());
		loadCampaigns();
		initCampaigns();
	}

	private void findPCCFiles(File aDirectory)
	{
		if (!aDirectory.exists() || !aDirectory.isDirectory())
		{
			return;
		}
		for (File file : aDirectory.listFiles(pccFileFilter))
		{
			if (file.isDirectory())
			{
				findPCCFiles(file);
				continue;
			}
			campaignFiles.add(file.toURI());
		}
	}

	private void loadCampaigns()
	{
		isLoading = true;
		int progress = 0;
		while (!campaignFiles.isEmpty())
		{
			URI uri = campaignFiles.poll();
			if (Globals.getCampaignByURI(uri, false) == null)
			{
				try
				{
					campaignLoader.loadLstFile(null, uri);
				}
				catch (PersistenceLayerException ex)
				{
					// LATER: This is not an appropriate way to deal with this exception.
					// Deal with it this way because of the way the loading takes place.  XXX
					Logging.errorPrint("PersistanceLayer", ex);
				}
			}
			progress++;
			setProgress(progress);
		}
		isLoading = false;
	}

	private void initCampaigns()
	{
		// This may modify the globals list; need a local copy so
		// the iteration doesn't fail.
		List<Campaign> initialCampaigns =
				new ArrayList<Campaign>(Globals.getCampaignList());
		
		for (Campaign c : initialCampaigns)
		{
			campaignLoader.initRecursivePccFiles(c);
		}
	}

	/**
	 * @return the alternateSourceFolder
	 */
	public File getAlternateSourceFolder()
	{
		return alternateSourceFolder;
	}

	/**
	 * @param alternateSourceFolder the alternateSourceFolder to set
	 */
	public void setAlternateSourceFolder(File alternateSourceFolder)
	{
		this.alternateSourceFolder = alternateSourceFolder;
	}

}
