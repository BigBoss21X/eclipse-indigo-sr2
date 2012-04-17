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
 * @version $Id: SHDollarRule.java,v 1.1 2004/08/17 19:56:52 dougsatch Exp $
 */
public class SHDollarRule implements IRule
{
	private IToken defaultToken;
	private IWordDetector detector;
	private IToken successToken;
	private StringBuffer buffer = new StringBuffer();
	private char openBrace;
	private char closeBrace;
	private boolean bracesRequired;
	
	
	public SHDollarRule(IWordDetector detector, IToken defaultToken, IToken token, boolean bracesRequired, char openBrace, char closeBrace)
	{
		this.detector = detector;
		this.successToken = token;
		this.defaultToken = defaultToken;
		this.openBrace = openBrace;
		this.closeBrace = closeBrace;
		this.bracesRequired = bracesRequired;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner)
	{
		int c= scanner.read();
		if (detector.isWordStart((char) c)) 
		{
			buffer.setLength(0);
			int counter = 0;
			do 
			{
				buffer.append((char) c);
				c= scanner.read();
				++counter;
			}
			while (c != ICharacterScanner.EOF && (detector.isWordPart((char) c) || (c==openBrace && counter==1)));
			if (c==closeBrace)
				buffer.append((char) c);
			else
				scanner.unread();
			if (buffer.length()>1)
			{
				char startChar = buffer.charAt(1);
				char endChar = buffer.charAt(buffer.length()-1);
				if ((startChar==openBrace && endChar==closeBrace) || (!bracesRequired && (Character.isJavaLetterOrDigit(startChar) && Character.isJavaLetterOrDigit(endChar))))
					return successToken;
			}
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
