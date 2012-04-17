/* $Id: IndentType.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $ */
package com.something.eclipse.script.text;

/**
 * A type of indent.
 * 
 * @author Doug Satchwell
 * @version $Id: IndentType.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $
 */
public class IndentType
{
	/** No indent */
	public static final IndentType NONE = new IndentType();
	/** A single indent increment */
	public static final IndentType INCREMENT = new IndentType();
	/** A single indent decrement */
	public static final IndentType DECREMENT = new IndentType();
	/** An inflexion - both an increment and a decrement */
	public static final IndentType INFLEXION = new IndentType();
	
	private IndentType()
	{}
}
