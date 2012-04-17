/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.text;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

/**
 * An implementation of <code>IRule</code> capable of detecting words
 * Word rules also allow for the association of tokens with specific words. 
 * That is, not only can the rule be used to provide tokens for exact matches, 
 * but also for the generalized notion of a word in the context in which it is used.
 * A word rules uses a word detector to determine what a word is.
 * <p>
 * This word rule is CASE INSENSITIVE.
 * 
 * @author Doug Satchwell
 * @version $Id: SHWordRule.java,v 1.1 2004/08/17 19:56:53 dougsatch Exp $
 * @see IWordDetector
 */
public class SHWordRule implements IRule
{
	protected boolean caseInsensitive;
	/** Internal setting for the uninitialized column constraint */
	protected static final int UNDEFINED = -1;
	/** The word detector used by this rule */
	protected IWordDetector fDetector;
	/** The default token to be returned on success and if nothing else has been specified. */
	protected IToken fDefaultToken;
	/** The column constraint */
	protected int fColumn = UNDEFINED;
	/** The table of predefined words and token for this rule */
	protected Map fWords = new HashMap();
	/** Buffer used for pattern detection */
	private StringBuffer fBuffer = new StringBuffer();
	/**
	 * Creates a rule which, with the help of an word detector, will return the token
	 * associated with the detected word. If no token has been associated, the scanner 
	 * will be rolled back and an undefined token will be returned in order to allow 
	 * any subsequent rules to analyze the characters.
	 *
	 * @param detector the word detector to be used by this rule, may not be <code>null</code>
	 *
	 * @see #addWord
	 */
	public SHWordRule(IWordDetector detector)
	{
		this(detector, Token.UNDEFINED);
	}
	/**
	 * Creates a rule which, with the help of an word detector, will return the token
	 * associated with the detected word. If no token has been associated, the
	 * specified default token will be returned.
	 *
	 * @param detector the word detector to be used by this rule, may not be <code>null</code>
	 * @param defaultToken the default token to be returned on success 
	 *		if nothing else is specified, may not be <code>null</code>
	 *
	 * @see #addWord
	 */
	public SHWordRule(IWordDetector detector, IToken defaultToken)
	{
		Assert.isNotNull(detector);
		Assert.isNotNull(defaultToken);
		fDetector = detector;
		fDefaultToken = defaultToken;
	}
	/**
	 * Adds a word and the token to be returned if it is detected.
	 *
	 * @param word the word this rule will search for, may not be <code>null</code>
	 * @param token the token to be returned if the word has been found, may not be <code>null</code>
	 */
	public void addWord(String word, IToken token)
	{
		Assert.isNotNull(word);
		Assert.isNotNull(token);
		fWords.put(word.toUpperCase(), token);
	}
	/**
	 * Sets a column constraint for this rule. If set, the rule's token
	 * will only be returned if the pattern is detected starting at the 
	 * specified column. If the column is smaller then 0, the column
	 * constraint is considered removed.
	 *
	 * @param column the column in which the pattern starts
	 */
	public void setColumnConstraint(int column)
	{
		if (column < 0)
			column = UNDEFINED;
		fColumn = column;
	}
	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner)
	{
		int c = scanner.read();
		if (fDetector.isWordStart((char)c))
		{
			if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1))
			{
				fBuffer.setLength(0);
				do
				{
					fBuffer.append((char)c);
					c = scanner.read();
				}
				while (c != ICharacterScanner.EOF && fDetector.isWordPart((char)c));
				scanner.unread();
				IToken token = (IToken)fWords.get(fBuffer.toString().toUpperCase());
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
	/**
	 * Returns the characters in the buffer to the scanner.
	 *
	 * @param scanner the scanner to be used
	 */
	protected void unreadBuffer(ICharacterScanner scanner)
	{
		for (int i = fBuffer.length() - 1; i >= 0; i--)
			scanner.unread();
	}
}
