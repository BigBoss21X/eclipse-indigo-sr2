/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.format;

import com.something.eclipse.script.format.ICodeFormatter;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHCodeFormatter.java,v 1.1 2004/08/17 19:57:10 dougsatch Exp $
 */
public class SHCodeFormatter implements ICodeFormatter
{
	/**
	 * @param content
	 * @param positions
	 * @param strings
	 * @return
	 */
	public String format(String string, String indentation, String[] lineDelimiters)
	{
		StringBuffer buffer = new StringBuffer();
		int start = 0;
		int newLineIndex = 0;
		int end = -1;
		boolean previousAmpersand = false;
		
		// iterate over each line
		while(true)
		{
			start = end + 1;
			newLineIndex = string.indexOf('\n', start);

			// If we come across the picture statement, stop formatting and just copy the remainder across
			if(newLineIndex>=start+8 && string.substring(start,start+8).equalsIgnoreCase("picture,"))
			{
				buffer.append(string.substring(start));
				break;
			}
			
			
			// if there is no new line, then the end point is the end of the string
			if(newLineIndex == -1)
				end = string.length()-1;
			else
				end = newLineIndex;
			
			int nonSpaceStart = start; // the start of the line ignoring whitespace
			int nonSpaceEnd = end; // the end of the line ignoring whitespace
			
			// find where non-whitespace starts
			while(nonSpaceStart<nonSpaceEnd && nonSpaceStart<string.length() && Character.isWhitespace(string.charAt(nonSpaceStart)))
			{
				++nonSpaceStart;
			}
			
			// find where non-whitespace ends 
			while(nonSpaceStart<nonSpaceEnd && nonSpaceEnd<string.length() && Character.isWhitespace(string.charAt(nonSpaceEnd)))
			{
				--nonSpaceEnd;
			}
/*			
			if (newLineIndex != -1)
			{
				switch(string.charAt(nonSpaceStart))
				{
					case '{':
						formatLine(buffer,string,start,nonSpaceStart,end,nonSpaceEnd,currentIndentationLevel);
						++currentIndentationLevel;
						break;
					case '}':
						--currentIndentationLevel;
					default:
						formatLine(buffer,string,start,nonSpaceStart,end,nonSpaceEnd,currentIndentationLevel);
				}
				// if this line is not just a new line, add a new line
				if (string.charAt(nonSpaceEnd)=='\n' && buffer.length()>1)
					buffer.deleteCharAt(buffer.length()-1);
				buffer.append(EOL_WINDOWS);
					
				switch(string.charAt(nonSpaceEnd))
				{
					case '&':
						if(!previousAmpersand)
						{
							previousAmpersand=true;
							++currentIndentationLevel;
						}
						break;
					default:
						if(previousAmpersand)
						{
							previousAmpersand=false;
							--currentIndentationLevel;
						}
				}
			}
			else
			{
				formatLine(buffer,string,start,nonSpaceStart,end,nonSpaceEnd,currentIndentationLevel);
				break;
			}*/
		}
		return buffer.toString();
	}
}
