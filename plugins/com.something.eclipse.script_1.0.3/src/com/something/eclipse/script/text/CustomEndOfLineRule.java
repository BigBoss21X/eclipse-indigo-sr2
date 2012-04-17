/* $Id: CustomEndOfLineRule.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $ */
package com.something.eclipse.script.text;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.Token;

/**
 * 
 * This is an end of line rule which has a continuation character. In P4 this would be 
 * &amp;, and in shell script it would be \.
 * 
 * @author Doug Satchwell
 * @version $Id: CustomEndOfLineRule.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $
 */
public class CustomEndOfLineRule extends PatternRule
{
	/** The token to be returned on success */
	private IToken successToken;
	private char startChar;

	public CustomEndOfLineRule(IToken successToken) 
	{
		this(successToken, (char)0);
	}

	public CustomEndOfLineRule(IToken successToken, char escapeCharacter) 
	{
		super("1", "&", successToken, escapeCharacter, false, false);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.PatternRule#doEvaluate(org.eclipse.jface.text.rules.ICharacterScanner, boolean)
	 */
	protected IToken doEvaluate(ICharacterScanner scanner, boolean resume)
	{	
		if (endSequenceDetected(scanner))
			return fToken;
		return Token.UNDEFINED;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.PatternRule#endSequenceDetected(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	protected boolean endSequenceDetected(ICharacterScanner scanner)
	{
		int c;
		char[][] delimiters= scanner.getLegalLineDelimiters();
//		System.out.println();
		while ((c= scanner.read()) != ICharacterScanner.EOF) 
		{
//			System.out.print((char)c);
			if (c == fEscapeCharacter) {
				// Skip the escaped character.
				scanner.read();
			} else if (fEndSequence.length > 0 && c == fEndSequence[0]) {
				// Check if the specified end sequence has been found.
				if (sequenceDetected(scanner, fEndSequence, true))
					return false;
			} else if (fBreaksOnEOL) {
				// Check for end of line since it can be used to terminate the pattern.
				for (int i= 0; i < delimiters.length; i++) {
					if (c == delimiters[i][0] && sequenceDetected(scanner, delimiters[i], true))
						return true;
				}
			}
		}
		if (fBreaksOnEOF) 
			return true;
		scanner.unread();
		return false;	
	}
}
