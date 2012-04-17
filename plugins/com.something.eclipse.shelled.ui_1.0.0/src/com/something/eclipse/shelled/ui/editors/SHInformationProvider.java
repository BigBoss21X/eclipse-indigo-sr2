/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.editors;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProvider;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.something.eclipse.shelled.ui.text.SHWordFinder;
import com.something.eclipse.shelled.ui.text.hover.SHTextHover;

/**
 * @author Doug Satchwell
 * @version $Id: SHInformationProvider.java,v 1.1 2004/08/17 19:57:03 dougsatch Exp $
 */
public class SHInformationProvider implements IInformationProvider
{

	class EditorWatcher implements IPartListener
	{

		/**
		 * @see IPartListener#partOpened(IWorkbenchPart)
		 */
		public void partOpened(IWorkbenchPart part)
		{}

		/**
		 * @see IPartListener#partDeactivated(IWorkbenchPart)
		 */
		public void partDeactivated(IWorkbenchPart part)
		{}

		/**
		 * @see IPartListener#partClosed(IWorkbenchPart)
		 */
		public void partClosed(IWorkbenchPart part)
		{
			if(part==fEditor)
			{
				fEditor.getSite().getWorkbenchWindow().getPartService().removePartListener(fPartListener);
				fPartListener = null;
			}
		}

		/**
		 * @see IPartListener#partActivated(IWorkbenchPart)
		 */
		public void partActivated(IWorkbenchPart part)
		{
			update();
		}

		public void partBroughtToTop(IWorkbenchPart part)
		{
			update();
		}
	}

	protected IEditorPart fEditor;
	protected IPartListener fPartListener;

	protected String fCurrentPerspective;
	protected SHTextHover fImplementation;

	public SHInformationProvider(IEditorPart editor)
	{
		fEditor = editor;
		if(fEditor!=null)
		{
			fPartListener = new EditorWatcher();
			IWorkbenchWindow window = fEditor.getSite().getWorkbenchWindow();
			window.getPartService().addPartListener(fPartListener);
			update();
		}
	}

	protected void update()
	{
		IWorkbenchWindow window = fEditor.getSite().getWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		if(page!=null)
		{
			IPerspectiveDescriptor perspective = page.getPerspective();
			if(perspective!=null)
			{
				String perspectiveId = perspective.getId();

				if(fCurrentPerspective==null||fCurrentPerspective!=perspectiveId)
				{
					fCurrentPerspective = perspectiveId;

					fImplementation = new SHTextHover();
//					fImplementation.setEditor(fEditor);
				}
			}
		}
	}

	/*
	 * @see IInformationProvider#getSubject(ITextViewer, int)
	 */
	public IRegion getSubject(ITextViewer textViewer, int offset)
	{
		if(textViewer!=null)
			return SHWordFinder.findWord(textViewer.getDocument(),offset);

		return null;
	}

	/*
	 * @see IInformationProvider#getInformation(ITextViewer, IRegion)
	 */
	public String getInformation(ITextViewer textViewer, IRegion subject)
	{
		if(fImplementation!=null)
		{
			String s = fImplementation.getHoverInfo(textViewer,subject);
			if(s!=null&&s.trim().length()>0)
				return s;
		}

		return null;
	}
}