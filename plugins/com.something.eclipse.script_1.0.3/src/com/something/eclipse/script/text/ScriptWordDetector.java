/* $Id: ScriptWordDetector.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $ */
package com.something.eclipse.script.text;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: ScriptWordDetector.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $
 */
public class ScriptWordDetector implements IWordDetector 
{

	/**
	 * @see IWordDetector#isWordStart
	 */
	public boolean isWordStart(char c) 
	{
		return Character.isJavaIdentifierStart(c);
	}
	
	/**
	 * @see IWordDetector#isWordPart
	 */
	public boolean isWordPart(char c) 
	{
		return Character.isJavaIdentifierPart(c);
	}
}
