/* $Id: ScriptSourceViewerConfiguration.java,v 1.1 2004/08/17 19:39:55 dougsatch Exp $ */
package com.something.eclipse.script.sourceviewer;

import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: ScriptSourceViewerConfiguration.java,v 1.1 2004/08/17 19:39:55 dougsatch Exp $
 */
public abstract class ScriptSourceViewerConfiguration extends SourceViewerConfiguration
{
	/** Adapt this to a preference change */
	public abstract void adaptToPreferenceChange(PropertyChangeEvent event);
}
