/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;


/**
 * @author Doug - Something Software Ltd.
 * @version $Id: SHWords.java,v 1.1 2004/08/17 19:56:53 dougsatch Exp $
 */
public class SHWords
{
	public static final int CSH = 1;
	public static final int KSH = 2;
	public static final int SH = 3;
	public static final int BASH = 4;
	
	private int shellType;
	private String[] keywords; // e.g. if, do
	private PropertyResourceBundle rb;
	
	public SHWords(int shellType)
	{
		this.shellType = shellType;
		keywords = new String[]{"do","done","if","fi","then","else","elif","case","esac","while","for","in","select","time","until","function","[","[[","]","]]"};
		try
		{
			rb = new PropertyResourceBundle(getClass().getClassLoader().getResourceAsStream("/com/something/eclipse/shelled/resources/commands.properties"));
		}
		catch(RuntimeException x1)
		{
			// TODO Auto-generated catch block
			x1.printStackTrace(System.err);
		}
		catch(IOException x)
		{
			// TODO Auto-generated catch block
			x.printStackTrace();
		}
	}
	
	public String[] getCommands()
	{
		ArrayList list = new ArrayList();
		for (Enumeration enum = rb.getKeys();enum.hasMoreElements();)
		{
			list.add(enum.nextElement());
		}
		return (String[])list.toArray(new String[0]);
	}
	
	public String[] getKeywords()
	{
		return keywords;
	}
}
