/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.compare;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;

import com.something.eclipse.shelled.ui.editors.ColorManager;
import com.something.eclipse.shelled.ui.editors.SHEditorConfiguration;
import com.something.eclipse.shelled.ui.text.SHPartitionScanner;


public class SHTextMergeViewer extends TextMergeViewer 
{
	private int shellType;
	private ColorManager colorManager;

	/**
	 * @param parent
	 * @param configuration
	 */
	public SHTextMergeViewer(Composite parent, CompareConfiguration configuration, int shellType)
	{
		super(parent, configuration);
		this.shellType = shellType;
	}

	public String getTitle() 
	{
		return "Shell Compare";
	}

	protected IDocumentPartitioner getDocumentPartitioner() 
	{
		return new DefaultPartitioner(new SHPartitionScanner(),SHPartitionScanner.CONTENT_TYPES);
	}
	
	protected void configureTextViewer(TextViewer textViewer) 
	{
		if (textViewer instanceof SourceViewer) 
		{
			this.colorManager = new ColorManager();
			((SourceViewer)textViewer).configure(new SHEditorConfiguration(null,colorManager,shellType));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	protected void handleDispose(DisposeEvent event)
	{
		if (colorManager!=null)
			colorManager.dispose();
		super.handleDispose(event);
	}
}
