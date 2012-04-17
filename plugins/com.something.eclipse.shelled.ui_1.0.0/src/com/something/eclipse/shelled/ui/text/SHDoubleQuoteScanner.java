/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.text;

import java.util.ArrayList;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

import com.something.eclipse.script.text.ScriptScanner;
import com.something.eclipse.script.text.WhitespaceDetector;
import com.something.eclipse.shelled.ui.editors.ColorConstants;
import com.something.eclipse.shelled.ui.editors.ColorManager;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHDoubleQuoteScanner.java,v 1.1 2004/08/17 19:56:53 dougsatch Exp $
 */
public class SHDoubleQuoteScanner extends ScriptScanner
{
	private int shellType;
	private ColorManager manager;

	public SHDoubleQuoteScanner(ColorManager manager, int shellType) 
	{
		super();
		this.shellType = shellType;
		this.manager = manager;
	}

	/* (non-Javadoc)
	 * @see com.something.eclipse.script.text.ScriptScanner#createRules()
	 */
	protected IRule[] createRules()
	{
		ArrayList rules = new ArrayList(50);
		
		// Add all keywords as rules
		IToken commandToken = new Token(new TextAttribute(manager.getColor(ColorConstants.FUNCTION)));
		IToken defaultToken = new Token(new TextAttribute(manager.getColor(ColorConstants.DEFAULT)));
		IToken evalToken = new Token(new TextAttribute(manager.getColor(ColorConstants.EVAL_TYPE)));
		IToken singleQuoteToken = new Token(new TextAttribute(manager.getColor(ColorConstants.SINGLE_QUOTE)));
		IToken varToken = new Token(new TextAttribute(manager.getColor(ColorConstants.VARIABLE_TYPE)));
		
		IWordDetector dollarDetector = new IWordDetector(){
			public boolean isWordStart(char c)
			{
				return c=='$';			
			}

			public boolean isWordPart(char c)
			{
				return Character.isLetterOrDigit(c);			
			}};

		// Add generic whitespace rule. This is here for efficiency reasons, there is a LOT of whitespace
		// and when a token is detected the other rules are not evaluated.
		rules.add(new WhitespaceRule(new WhitespaceDetector()));
		rules.add(new SHDollarRule(dollarDetector,Token.UNDEFINED,varToken,false,'{','}'));
		rules.add(new SingleLineRule("$(",")",evalToken,'\\',false));
		rules.add(new SingleLineRule("`","`",evalToken,'\\',false));
		rules.add(new SingleLineRule("'","'",singleQuoteToken,'\\',false));
				
		IRule[] rule = new IRule[rules.size()];
		return (IRule[])rules.toArray(rule);
	}
}
