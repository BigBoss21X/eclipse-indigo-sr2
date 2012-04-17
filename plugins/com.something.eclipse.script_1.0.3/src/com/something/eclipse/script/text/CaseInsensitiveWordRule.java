/* $Id: CaseInsensitiveWordRule.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $ */
package com.something.eclipse.script.text;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Unlike it's superclass, this word rule is CASE INSENSITIVE.
 * All words added to the rule are uppercased. All characters passed 
 * to the word detector are also upper-cased, so there is no need for 
 * the word detector to handle both uppand lower cases.
 * 
 * @author Doug Satchwell
 * @version $Id: CaseInsensitiveWordRule.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $
 * @see IWordDetector
 */
public class CaseInsensitiveWordRule extends WordRule
{
	/** Buffer used for pattern detection */
	private StringBuffer fBuffer= new StringBuffer();

	public CaseInsensitiveWordRule(IWordDetector detector)
	{this(detector, Token.UNDEFINED);}
	
	public CaseInsensitiveWordRule(IWordDetector detector, IToken defaultToken)
	{super(detector,defaultToken);}
	
	public void addWord(String word, IToken token)
	{super.addWord(word.toUpperCase(),token);}
	
	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner)
	{
		char c = Character.toUpperCase((char)scanner.read());
		if (fDetector.isWordStart(c))
		{
			if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1))
			{
				fBuffer.setLength(0);
				do
				{
					fBuffer.append(c);
					c = Character.toUpperCase((char)scanner.read());
				}
				while (c != ICharacterScanner.EOF && fDetector.isWordPart(c));
				scanner.unread();
				IToken token = (IToken)fWords.get(fBuffer.toString());
				if (token != null)
					return token;
				if (fDefaultToken.isUndefined())
					unreadBuffer(scanner);
				return fDefaultToken;
			}
		}
		scanner.unread();
		return Token.UNDEFINED;
	}
	
	protected void unreadBuffer(ICharacterScanner scanner)
	{
		for (int i = fBuffer.length() - 1; i >= 0; i--)
			scanner.unread();
	}
}
