/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: ColorManager.java,v 1.1 2004/08/17 19:57:03 dougsatch Exp $
 */
public class ColorManager
{
	protected Map fColorTable = new HashMap(10);

	public void dispose() 
	{
		Iterator e = fColorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
	}
	
	public Color getColor(RGB rgb) 
	{
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) 
		{
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
