/* $Id: ScriptScanner.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $ */
package com.something.eclipse.script.text;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WordRule;

/**
 * A scanner that creates rules its own rules 
 * 
 * @author Doug Satchwell
 * @version $Id: ScriptScanner.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $
 */
public abstract class ScriptScanner extends RuleBasedScanner
{
	public ScriptScanner()
	{
		super();
	}
	
	/** Create the rules from scratch */
	protected abstract IRule[] createRules();
	
	/** Reread the set of rules */
	public void refresh()
	{
		setRules(createRules());
	}
	
	protected static void addWords(WordRule rule,String[] words,IToken keywordToken)
	{
		for (int i = 0;i < words.length; i++)
			rule.addWord(words[i], keywordToken);
	}
}
