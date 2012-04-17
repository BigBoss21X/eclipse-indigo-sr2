/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.editors;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.information.InformationPresenter;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ResourceAction;
import org.eclipse.ui.texteditor.TextOperationAction;

import com.something.eclipse.script.contentassist.HTMLTextPresenter;
import com.something.eclipse.shelled.ui.actions.ActionDefinitionIds;
import com.something.eclipse.shelled.ui.actions.TextHoverFocusAction;
import com.something.eclipse.shelled.ui.text.SHWords;

/**
 * @author Doug Satchwell
 * @version $Id: SHEditor.java,v 1.1 2004/08/17 19:57:03 dougsatch Exp $
 */
public class SHEditor extends TextEditor
{
	private ColorManager colorManager;
	/** The information presenter. */
	private InformationPresenter fInformationPresenter;
	public static final int SHELL_TYPE = SHWords.SH;
	
	public SHEditor() 
	{
		super();	
		colorManager = new ColorManager();
		setKeyBindingScopes(keyBindingScopes());
		SHDocumentProvider provider = new SHDocumentProvider(this);
		setDocumentProvider(provider);
		SHEditorConfiguration config = new SHEditorConfiguration(this,colorManager,SHELL_TYPE);
		setSourceViewerConfiguration(config);
	}
	
	/** Configure the key binding scopes for this */
	protected String[] keyBindingScopes()
	{
		return new String[] {"com.something.eclipse.shelled.ui.SHEditorScope"};	
	}
	

	/** 
	 * Method declared on IAdaptable. 
	 * Classes are passed in, this method searches for any known implementors  
	 * of this class and returns an instanced of it. Used for example to 
	 * get an instance of a ContentOutlinePage. 
	 */
	public Object getAdapter(Class key) 
	{
/*		if (key.equals(IContentOutlinePage.class)) 
		{
			IEditorInput input = getEditorInput();
			if (input instanceof IFileEditorInput) 
			{
				page = new P4ContentOutlinePage(this,((IFileEditorInput)input).getFile());
				return page;
			}
		}*/
		return super.getAdapter(key);
	}
	
	public void dispose() 
	{
		colorManager.dispose();
		super.dispose();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextEditor#createSourceViewer(org.eclipse.swt.widgets.Composite, org.eclipse.jface.text.source.IVerticalRuler, int)
	 */
	protected ISourceViewer createSourceViewer(Composite parent,IVerticalRuler ruler,int styles)
	{
		SourceViewer viewer = (SourceViewer)super.createSourceViewer(parent,ruler,styles);
//		viewer.prependAutoEditStrategy(new SmartBracesAutoEditStrategy(viewer), IDocument.DEFAULT_CONTENT_TYPE);
		return viewer;
		//return new P4SourceViewer(parent, ruler, fOverviewRuler, isOverviewRulerVisible(), styles);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextEditor#createActions()
	 */
	protected void createActions()
	{
		super.createActions();
		
		this.fInformationPresenter = createInformationPresenter();
		
		ResourceAction resAction= new TextOperationAction(EditorMessages.getResourceBundle(), "ShowManPage.", this, ISourceViewer.INFORMATION, true);
		resAction= new TextHoverFocusAction(EditorMessages.getResourceBundle(), "ShowManPage.", (TextOperationAction) resAction, this, getSourceViewer(),fInformationPresenter);
		resAction.setActionDefinitionId(ActionDefinitionIds.SHOW_MANPAGE);
		setAction("ShowManPage", resAction);
	}
	
	private InformationPresenter createInformationPresenter()
	{
		IInformationControlCreator informationControlCreator= new IInformationControlCreator() 
		{
			public IInformationControl createInformationControl(Shell shell) 
			{
				boolean cutDown= false;
				int style= cutDown ? SWT.NONE : (SWT.V_SCROLL | SWT.H_SCROLL);
				return new DefaultInformationControl(shell, SWT.RESIZE, style, new HTMLTextPresenter(cutDown));
			}
		};

		InformationPresenter informationPresenter= new InformationPresenter(informationControlCreator);
		informationPresenter.setSizeConstraints(45, 15, true, true);		
		informationPresenter.install(getSourceViewer());
		return informationPresenter;
	}
	
	/*
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) 
	{	
		super.createPartControl(parent);		
	}
}
