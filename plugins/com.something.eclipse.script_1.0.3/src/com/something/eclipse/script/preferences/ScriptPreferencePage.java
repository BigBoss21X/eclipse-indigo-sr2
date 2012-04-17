/* $Id: ScriptPreferencePage.java,v 1.1 2004/08/17 19:39:57 dougsatch Exp $ */
package com.something.eclipse.script.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: ScriptPreferencePage.java,v 1.1 2004/08/17 19:39:57 dougsatch Exp $
 */
public abstract class ScriptPreferencePage extends PreferencePage implements IWorkbenchPreferencePage 
{
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent)
	{
		initializePreferences(getPreferenceStore());	
		return new Composite(parent, SWT.NULL);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
	 */
	protected abstract IPreferenceStore doGetPreferenceStore();
	
	/**
	 * @param store the preference store found from <code>getPreferenceStore()</code>
	 */
	protected abstract void initializePreferences(IPreferenceStore store);
	
	/**
	 * @param store the preference store found from <code>getPreferenceStore()</code>
	 */
	protected abstract void storePreferences(IPreferenceStore store);

	/**
	 * @param store the preference store found from <code>getPreferenceStore()</code>
	 */
	protected abstract void restoreDefaultPreferences(IPreferenceStore store);
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults()
	{
		restoreDefaultPreferences(getPreferenceStore());
		super.performDefaults();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench)
	{}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk()
	{
		storePreferences(getPreferenceStore());
		return super.performOk();
	}
}