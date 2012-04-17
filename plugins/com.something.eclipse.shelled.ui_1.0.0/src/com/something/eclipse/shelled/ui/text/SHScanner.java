/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.text;

import java.util.ArrayList;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.something.eclipse.script.text.ScriptScanner;
import com.something.eclipse.script.text.WhitespaceDetector;
import com.something.eclipse.shelled.ui.UIPlugin;
import com.something.eclipse.shelled.ui.editors.ColorConstants;
import com.something.eclipse.shelled.ui.editors.ColorManager;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHScanner.java,v 1.1 2004/08/17 19:56:53 dougsatch Exp $
 */
public class SHScanner extends ScriptScanner
{
	private int shellType;
	private ColorManager manager;

	public SHScanner(ColorManager manager, int shellType) 
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
		IToken keywordToken = new Token(new TextAttribute(manager.getColor(ColorConstants.KEYWORD),null,SWT.BOLD));
		IToken commandToken = new Token(new TextAttribute(manager.getColor(ColorConstants.FUNCTION)));
		IToken defaultToken = new Token(new TextAttribute(manager.getColor(ColorConstants.DEFAULT)));
		IToken varToken = new Token(new TextAttribute(manager.getColor(ColorConstants.VARIABLE_TYPE)));
		
		IWordDetector wordDetector = new IWordDetector(){
			public boolean isWordStart(char c)
			{
				return Character.isJavaIdentifierStart(c);			
			}

			public boolean isWordPart(char c)
			{
				return Character.isJavaIdentifierPart(c);			
			}};

		IWordDetector dollarDetector = new IWordDetector(){
			public boolean isWordStart(char c)
			{
				return c=='$';			
			}

			public boolean isWordPart(char c)
			{
				return Character.isLetterOrDigit(c);			
			}};
			
		rules.add(new WhitespaceRule(new WhitespaceDetector()));		
		rules.add(new SHAssignmentRule(wordDetector,Token.UNDEFINED,varToken));
		rules.add(new SHDollarRule(dollarDetector,Token.UNDEFINED,varToken,false,'{','}'));

		WordRule wordRule = new WordRule(wordDetector,defaultToken);
		SHWords words = UIPlugin.getDefault().getWords(shellType);
		addWords(wordRule,words.getCommands(),commandToken);
		addWords(wordRule,words.getKeywords(),keywordToken);
		rules.add(wordRule);
		
		
		IRule[] rule = new IRule[rules.size()];
		return (IRule[])rules.toArray(rule);
	}
	
	
}
