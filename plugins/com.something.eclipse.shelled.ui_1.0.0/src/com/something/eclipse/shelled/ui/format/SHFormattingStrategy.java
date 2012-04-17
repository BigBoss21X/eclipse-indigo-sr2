/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.format;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHFormattingStrategy.java,v 1.1 2004/08/17 19:57:10 dougsatch Exp $
 */
public class SHFormattingStrategy implements IFormattingStrategy
{
	private ISourceViewer sourceViewer;
	private String initialIndentation;
	
	public SHFormattingStrategy(ISourceViewer viewer)
	{
		this.sourceViewer = viewer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.formatter.IFormattingStrategy#formatterStarts(java.lang.String)
	 */
	public void formatterStarts(String initialIndentation)
	{
		this.initialIndentation = initialIndentation;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.formatter.IFormattingStrategy#format(java.lang.String, boolean, java.lang.String, int[])
	 */
	public String format(String content,boolean isLineStart,String indentation,int[] positions)
	{
		SHCodeFormatter formatter = new SHCodeFormatter();
		IDocument doc = sourceViewer.getDocument();
		return formatter.format(content, indentation, doc.getLegalLineDelimiters());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.formatter.IFormattingStrategy#formatterStops()
	 */
	public void formatterStops()
	{}
}
