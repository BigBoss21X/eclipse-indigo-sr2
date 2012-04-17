/* $Id: DocumentAndCommandScanner.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $ */
package com.something.eclipse.script.text;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.RuleBasedScanner;

/**
 * This scanner extends the normal rule based scanner by adding the ability to 
 * read DocumentCommands as well as IDocuments. This ability is toggled on or off depending on whether  
 * setRange(document,offset,length) (off) or setRange(document,documentCommand,offset,length) (on)
 * was called.
 * 
 * @author Doug Satchwell
 * @version $Id: DocumentAndCommandScanner.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $
 */
public class DocumentAndCommandScanner extends RuleBasedScanner
{
	private DocumentCommand fDocumentCommand;

	/**
	 * Set this to use as a normal RuleBasedScanner
	 */
	public void setRange(IDocument document,int offset, int length) 
	{
		super.setRange(document,offset,length);
		fDocumentCommand = null;
	}

	/**
	 * Set this to use as a normal in conjunction with DocumentCommand's
	 */
	public void setRange(IDocument document, DocumentCommand documentCommand,int offset, int length) 
	{
		setRange(document,offset,length);
		fDocumentCommand = documentCommand;
	}

	/*
	 * @see ICharacterScanner#read()
	 */
	public int read() 
	{
		try 
		{			
			if (fOffset < fRangeEnd) 
			{
				try 
				{
					return fDocument.getChar(fOffset);
				} 
				catch (BadLocationException e) 
				{}
			}			
			if (fDocumentCommand != null && fOffset < fRangeEnd+fDocumentCommand.text.length())
			{
				return fDocumentCommand.text.charAt((fRangeEnd+fDocumentCommand.text.length()-fOffset)-1);
			}			
			return EOF;	
		} 
		finally 
		{
			++ fOffset;
		}
	}
}
