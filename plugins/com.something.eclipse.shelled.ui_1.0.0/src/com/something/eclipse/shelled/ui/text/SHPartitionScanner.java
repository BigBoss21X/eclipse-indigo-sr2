/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHPartitionScanner.java,v 1.1 2004/08/17 19:56:53 dougsatch Exp $
 */
public class SHPartitionScanner extends RuleBasedPartitionScanner
{
	public static final String EVAL_CONTENT_TYPE = "__eval";
	public static final String COMMENT_CONTENT_TYPE = "__comment";
	public static final String HASHBANG_CONTENT_TYPE = "__hashbang";
	public static final String SINGLE_QUOTE_CONTENT_TYPE = "__single_quote";
	public static final String DOUBLE_QUOTE_CONTENT_TYPE = "__double_quote";

	public static final String[] CONTENT_TYPES = new String[]{
		IDocument.DEFAULT_CONTENT_TYPE, 
		HASHBANG_CONTENT_TYPE,
		COMMENT_CONTENT_TYPE,
		SINGLE_QUOTE_CONTENT_TYPE,
		DOUBLE_QUOTE_CONTENT_TYPE,
		EVAL_CONTENT_TYPE
	};
	
	public SHPartitionScanner() 
	{
		super();

		List rules= new ArrayList();
		// Add rule for single line comments.
		rules.add(new EndOfLineRule("#!",new Token(HASHBANG_CONTENT_TYPE))); 
		rules.add(new EndOfLineRule("#",new Token(COMMENT_CONTENT_TYPE))); 
		rules.add(new SingleLineRule("$(",")",new Token(EVAL_CONTENT_TYPE),'\\',false,true));
		rules.add(new SingleLineRule("`","`",new Token(EVAL_CONTENT_TYPE),'\\',false,true));
		rules.add(new SingleLineRule("\"","\"",new Token(DOUBLE_QUOTE_CONTENT_TYPE),'\\',false,true));
		rules.add(new SingleLineRule("'","'",new Token(SINGLE_QUOTE_CONTENT_TYPE),'\\',false,true));
		
		IPredicateRule[] result= new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}
