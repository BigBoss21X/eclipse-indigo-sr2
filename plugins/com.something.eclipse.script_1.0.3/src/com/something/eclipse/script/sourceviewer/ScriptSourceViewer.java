/* $Id: ScriptSourceViewer.java,v 1.1 2004/08/17 19:39:55 dougsatch Exp $ */
package com.something.eclipse.script.sourceviewer;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.text.DocumentCommand;

/**
 * @author Doug Satchwell
 * @version $Id: ScriptSourceViewer.java,v 1.1 2004/08/17 19:39:55 dougsatch Exp $
 */
public class ScriptSourceViewer extends SourceViewer
{
	public ScriptSourceViewer(Composite parent,IVerticalRuler ruler,IOverviewRuler overviewRuler,boolean showAnnotationsOverview,int styles)
	{
		super(parent, ruler, overviewRuler, showAnnotationsOverview, styles);
	}
/*	
	protected void customizeDocumentCommand(DocumentCommand command)
	{
		if (command.text.equals("\t"))
		{
			int line = getTextWidget().getLineAtOffset(command.offset);
			int lineStartOffset = getTextWidget().getOffsetAtLine(line);
			int column = command.offset - lineStartOffset;
			//command.text = PreferenceUtil.getTab(column);
			command.text = "   ";
		}
		super.customizeDocumentCommand(command);
	}*/
}