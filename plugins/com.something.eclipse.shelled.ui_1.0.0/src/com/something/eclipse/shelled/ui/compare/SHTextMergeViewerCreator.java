/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.compare;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

import com.something.eclipse.shelled.ui.text.SHWords;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHTextMergeViewerCreator.java,v 1.1 2004/08/17 19:57:34 dougsatch Exp $
 */
public class SHTextMergeViewerCreator implements IViewerCreator
{
	/* (non-Javadoc)
	 * @see org.eclipse.compare.IViewerCreator#createViewer(org.eclipse.swt.widgets.Composite, org.eclipse.compare.CompareConfiguration)
	 */
	public Viewer createViewer(Composite parent, CompareConfiguration config)
	{
		int shellType = SHWords.SH;
		return new SHTextMergeViewer(parent, config, shellType);
	}
}
