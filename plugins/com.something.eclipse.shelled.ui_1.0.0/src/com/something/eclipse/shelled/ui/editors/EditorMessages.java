/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.editors;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class EditorMessages
{
	private static final String RESOURCE_BUNDLE = "com.something.eclipse.shelled.ui.editors.EditorMessages";

	private static ResourceBundle fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

	private EditorMessages()
	{}

	public static String getString(String key)
	{
		try
		{
			return fgResourceBundle.getString(key);
		}
		catch(MissingResourceException e)
		{
			return '!'+key+'!';
		}
	}

	public static ResourceBundle getResourceBundle()
	{
		return fgResourceBundle;
	}

	/**
	 * Gets a string from the resource bundle and formats it with arguments
	 */
	public static String getFormattedString(String key, Object[] args)
	{
		return MessageFormat.format(getString(key),args);
	}

	/**
	 * Gets a string from the resource bundle and formats it with arguments
	 */
	public static String getFormattedString(String key, Object arg)
	{
		return MessageFormat.format(getString(key),new Object[]{arg});
	}
}