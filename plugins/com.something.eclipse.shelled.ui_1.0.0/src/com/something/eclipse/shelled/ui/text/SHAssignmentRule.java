/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.text;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHAssignmentRule.java,v 1.1 2004/08/17 19:56:53 dougsatch Exp $
 */
public class SHAssignmentRule implements IRule
{
	private IToken defaultToken;
	private IWordDetector detector;
	private IToken successToken;
	private StringBuffer buffer = new StringBuffer();	
	
	public SHAssignmentRule(IWordDetector detector, IToken defaultToken, IToken token)
	{
		this.detector = detector;
		this.successToken = token;
		this.defaultToken = defaultToken;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner)
	{
		char c= (char)scanner.read();
//		System.out.println(c);
		if (detector.isWordStart((c)))
		{
			buffer.setLength(0);
			do 
			{
				buffer.append((c));
				c= (char)scanner.read();
			}
			while (c != ICharacterScanner.EOF && detector.isWordPart(c));
			scanner.unread();
			if (c=='=')
				return successToken;
			if (defaultToken.isUndefined())
				unreadBuffer(scanner);
			return defaultToken;
		}
		scanner.unread();
		return Token.UNDEFINED;
	}
	
	/**
	 * Returns the characters in the buffer to the scanner.
	 *
	 * @param scanner the scanner to be used
	 */
	protected void unreadBuffer(ICharacterScanner scanner) {
		for (int i= buffer.length() - 1; i >= 0; i--)
			scanner.unread();
	}
}
