/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.editors;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHDocument.java,v 1.1 2004/08/17 19:57:03 dougsatch Exp $
 */
public class SHDocument extends Document implements IDocument 
{
	/** Ensure we only hae UNIX-style line delimiter \n */
	public static final String[] lineDelimiters = new String[]{"\n"};
	
	public SHDocument()
	{
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLegalLineDelimiters()
	 */
	public String[] getLegalLineDelimiters() 
	{
		return lineDelimiters;
	}
}
