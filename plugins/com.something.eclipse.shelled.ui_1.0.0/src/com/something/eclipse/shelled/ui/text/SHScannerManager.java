/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.text;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.RGB;

import com.something.eclipse.shelled.ui.editors.ColorConstants;
import com.something.eclipse.shelled.ui.editors.ColorManager;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHScannerManager.java,v 1.1 2004/08/17 19:56:53 dougsatch Exp $
 */
public class SHScannerManager
{
	private int shellType;
	private ColorManager colorManager;

	/**
	 * @param colorManager
	 */
	public SHScannerManager(ColorManager colorManager, int shellType)
	{
		this.colorManager = colorManager;
		this.shellType = shellType;
	}
	
	public ITokenScanner getScanner(String contentType)
	{
		if (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType))
			return getScriptScanner();
		if (SHPartitionScanner.COMMENT_CONTENT_TYPE.equals(contentType))
			return getCommentScanner();
		if (SHPartitionScanner.HASHBANG_CONTENT_TYPE.equals(contentType))
			return getHashBangScanner();
		if (SHPartitionScanner.SINGLE_QUOTE_CONTENT_TYPE.equals(contentType))
			return getSingleQuoteScanner();
		if (SHPartitionScanner.DOUBLE_QUOTE_CONTENT_TYPE.equals(contentType))
			return getDoubleQuoteScanner();
		if (SHPartitionScanner.EVAL_CONTENT_TYPE.equals(contentType))
			return getEvalScanner();
		return null;
	}

	private SHScanner getScriptScanner() 
	{
		SHScanner scanner = new SHScanner(colorManager, shellType);
		scanner.refresh();
		return scanner;
	}

	private RuleBasedPartitionScanner getCommentScanner() 
	{
		return createRuleBasedScanner(ColorConstants.COMMENT);
	}

	private RuleBasedPartitionScanner getHashBangScanner() 
	{
		return createRuleBasedScanner(ColorConstants.HASHBANG);
	}

	private SHDoubleQuoteScanner getDoubleQuoteScanner() 
	{
		SHDoubleQuoteScanner scanner = new SHDoubleQuoteScanner(colorManager, shellType);
		scanner.setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(ColorConstants.DOUBLE_QUOTE))));
		scanner.refresh();
		return scanner; 
	}

	private RuleBasedPartitionScanner getSingleQuoteScanner() 
	{
		return createRuleBasedScanner(ColorConstants.SINGLE_QUOTE);
	}
	
	private RuleBasedPartitionScanner getEvalScanner() 
	{
		return createRuleBasedScanner(ColorConstants.EVAL_TYPE);
	}

	private RuleBasedPartitionScanner createRuleBasedScanner(RGB colour)
	{
		RuleBasedPartitionScanner scanner = new RuleBasedPartitionScanner();
		scanner.setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(colour))));
		return scanner;
	}

}
