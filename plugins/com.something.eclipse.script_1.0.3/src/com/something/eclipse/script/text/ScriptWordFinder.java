/* $Id: ScriptWordFinder.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $ */
package com.something.eclipse.script.text;

 
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

/**
 * @author Doug Satchwell
 * @version $Id: ScriptWordFinder.java,v 1.1 2004/08/17 19:39:49 dougsatch Exp $
 */
public class ScriptWordFinder {
	
	public static IRegion findWord(IDocument document, int offset) {
		
		
		int start= -1;
		int end= -1;
		
		try {
			
			int pos= offset;
			char c;
			
			while (pos >= 0) {
				c= document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c))
				{
					break;
				}
				--pos;
			}
			
			start= pos;
			
			pos= offset;
			int length= document.getLength();
			
			while (pos < length) {
				c= document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c))
				{
					break;
				}
				++pos;
			}
			
			end= pos;
			
		} catch (BadLocationException x) {
		}
		
		if (start > -1 && end > -1) {
			Region region = null;
			if (start == offset && end == offset)
			{
				region = new Region(offset, 0);
			}
			else if (start == offset)
			{
				region = new Region(start, end - start);
			}
			else
			{
				region = new Region(start + 1, end - start - 1);
			}
			
			String word = null;
			try
			{
				word = document.get(region.getOffset(), region.getLength());
			}
			catch (BadLocationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return region;
		}
		
		return null;
	}
}
