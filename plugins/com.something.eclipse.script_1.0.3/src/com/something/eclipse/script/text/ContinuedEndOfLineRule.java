/* $Id: ContinuedEndOfLineRule.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $ */
package com.something.eclipse.script.text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;

/**
 * 
 * This is an end of line rule which has a continuation character. In P4 this would be 
 * &amp;, and in shell script it would be \.
 * 
 * @author Doug Satchwell
 * @version $Id: ContinuedEndOfLineRule.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $
 */
public class ContinuedEndOfLineRule extends PatternRule
{
	/** The token to be returned on success */
	private IToken successToken;
	private char startChar;
	private char newLineEscapeCharacter;

	public ContinuedEndOfLineRule(String startSequence, String endSequence, IToken successToken, char newLineEscapeCharacter) 
	{
		this(startSequence, endSequence, successToken, newLineEscapeCharacter, (char)0);
	}

	public ContinuedEndOfLineRule(String startSequence, String endSequence, IToken successToken, char newLineEscapeCharacter, char escapeCharacter) 
	{
		super(startSequence, endSequence, successToken, escapeCharacter, true, false);
		this.newLineEscapeCharacter = newLineEscapeCharacter;
	}
	
	protected boolean endSequenceDetected(ICharacterScanner scanner)
	{
		int c;
		char[][] delimiters = scanner.getLegalLineDelimiters();
		while ((c = scanner.read()) != ICharacterScanner.EOF)
		{
			boolean newLine = false;
			if (c == newLineEscapeCharacter)  // need to separate since newLineEscapeCharacter might equal fEscapeCharacter
			{
				int eolChar = scanner.read();
				for (int i = 0; i < delimiters.length; i++)
				{
					if (eolChar == delimiters[i][0] && sequenceDetected(scanner, delimiters[i], true))
					{
						// skip EOL characters (we have already read one of them, so start from 1)
						for (int j=0;j<delimiters[i].length;j++)
							scanner.read();
						newLine=true;
						break;
					}
				}
				// Don't escape unless we found EOL!
				if (!newLine)
					scanner.unread();
			}
			
			if (!newLine)
			{
				if (c == fEscapeCharacter)
				{
					// Skip the escaped character (e.g. n would be escaped for \n)
					scanner.read();
				}
				else if (fEndSequence.length > 0 && c == fEndSequence[0])
				{
					// Check if the specified end sequence has been found.
					if (sequenceDetected(scanner, fEndSequence, true))
						return true;
				}
				else if (fBreaksOnEOL)
				{
					// Check for end of line since it can be used to terminate the pattern.
					for (int i = 0; i < delimiters.length; i++)
					{
						if (c == delimiters[i][0] && sequenceDetected(scanner, delimiters[i], true))
						{
							return false;
						}
					}								
				}
			}
		}
		if (fBreaksOnEOF)
			return true;
		scanner.unread();
		return false;
	}
	
	private static char[] wordStarts(String[] words)
	{
		HashSet set = new HashSet();
		for (int i=0;i<words.length;i++)
			set.add(new Character(words[i].toCharArray()[0]));
		char[] letterChars = new char[set.size()];
		int i = 0;
		for (Iterator iter = set.iterator();iter.hasNext();i++)
		{
			Character character = (Character)iter.next();
			letterChars[i] = character.charValue();
		}
		Arrays.sort(letterChars);
		return letterChars;
	}
}
