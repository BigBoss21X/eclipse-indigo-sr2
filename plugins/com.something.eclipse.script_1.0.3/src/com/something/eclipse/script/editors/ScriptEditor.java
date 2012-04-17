/* $Id: ScriptEditor.java,v 1.1 2004/08/17 19:39:43 dougsatch Exp $ */
package com.something.eclipse.script.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.editors.text.TextEditor;

import com.something.eclipse.script.sourceviewer.ScriptSourceViewerConfiguration;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: ScriptEditor.java,v 1.1 2004/08/17 19:39:43 dougsatch Exp $
 */
public abstract class ScriptEditor extends TextEditor
{
	public ScriptEditor()
	{
		super();
//		setPreferenceStore(preferenceStore());
		setKeyBindingScopes(keyBindingScopes());
		setSourceViewerConfiguration(sourceViewerConfiguration());
	}
	
	/** Configure the key binding scopes for this */
	protected abstract String[] keyBindingScopes();
	
	/** The source viewer configuration for this editor */
	protected abstract ScriptSourceViewerConfiguration sourceViewerConfiguration();
	
	/** The preference store to use - probably that of the plugin */
//	protected abstract IPreferenceStore preferenceStore();
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextEditor#handlePreferenceStoreChanged(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	protected void handlePreferenceStoreChanged(PropertyChangeEvent event)
	{
		super.handlePreferenceStoreChanged(event);
		ScriptSourceViewerConfiguration config = (ScriptSourceViewerConfiguration)getSourceViewerConfiguration();
		config.adaptToPreferenceChange(event);
	}
}
