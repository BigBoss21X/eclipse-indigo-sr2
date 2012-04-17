/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;

import com.something.eclipse.shelled.ui.editors.EditorMessages;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: EditorActionBarContributor.java,v 1.1 2004/08/17 19:56:58 dougsatch Exp $
 */
public class EditorActionBarContributor extends BasicTextEditorActionContributor 
{
	private List fPartListeners= new ArrayList();
	private RetargetAction retargetShowManPage;
	private RetargetTextEditorAction showManPage;
	
	public EditorActionBarContributor()
	{
		retargetShowManPage= new RetargetAction(ActionConstants.SHOW_MANPAGE_ACTION, EditorMessages.getString("ShowManPage.label"));
		retargetShowManPage.setActionDefinitionId(ActionDefinitionIds.SHOW_MANPAGE);
		markAsPartListener(retargetShowManPage);
		
		ResourceBundle b= EditorMessages.getResourceBundle();
		showManPage= new RetargetTextEditorAction(b, "ShowManPage."); //$NON-NLS-1$
		showManPage.setActionDefinitionId(ActionDefinitionIds.SHOW_MANPAGE);
	}
	
	protected final void markAsPartListener(RetargetAction action) 
	{
		fPartListeners.add(action);
	}
	
	/*
	 * @see IEditorActionBarContributor#init(IActionBars, IWorkbenchPage)
	 */
	public void init(IActionBars bars, IWorkbenchPage page) {
		Iterator e= fPartListeners.iterator();
		while (e.hasNext()) 
			page.addPartListener((RetargetAction) e.next());
			
		super.init(bars, page);
		
		bars.setGlobalActionHandler(ActionConstants.SHOW_MANPAGE_ACTION, showManPage);
	}
	
	
	public void contributeToMenu(IMenuManager menu) 
	{		
		super.contributeToMenu(menu);		
		IMenuManager editMenu= menu.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (editMenu != null) 
		{
			editMenu.add(new Separator("group.generate"));
			editMenu.appendToGroup("group.generate", retargetShowManPage);
		}
	}
	
	public void setActiveEditor(IEditorPart part) 
	{
		super.setActiveEditor(part);		
		showManPage.setAction(getAction((ITextEditor)part, "ShowManPage")); //$NON-NLS-1$
	}
	
	
	
	/*
	 * @see IEditorActionBarContributor#dispose()
	 */
	public void dispose() 
	{
		setActiveEditor(null);
		super.dispose();
	}
}
