/* $Id: ICodeFormatter.java,v 1.1 2004/08/17 19:39:57 dougsatch Exp $ */
package com.something.eclipse.script.format;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: ICodeFormatter.java,v 1.1 2004/08/17 19:39:57 dougsatch Exp $
 */
public interface ICodeFormatter
{
	String format(String string, String indentation, String[] lineDelimiters);
}
