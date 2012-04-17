/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.actions;

import java.util.ResourceBundle;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.ITextViewerExtension4;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.jface.text.information.InformationPresenter;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.eclipse.ui.texteditor.TextOperationAction;

/**
 * This action behaves in two different ways: If there is no current text
 * hover, the javadoc is displayed using information presenter. If there is
 * a current text hover, it is converted into a information presenter in
 * order to make it sticky.
 * 
 * @author Doug - Something Software Ltd.
 * @version $Id: TextHoverFocusAction.java,v 1.1 2004/08/17 19:56:58 dougsatch Exp $
 */
public class TextHoverFocusAction extends TextEditorAction 
{
	
	/** The wrapped text operation action. */
	private final TextOperationAction textOperationAction;
	private ISourceViewer sourceViewer;
	private InformationPresenter informationPresenter;
	
	/**
	 * Creates a dispatch action.
	 * 
	 * @param resourceBundle the resource bundle
	 * @param prefix the prefix
	 * @param textOperationAction the text operation action
	 */
	public TextHoverFocusAction(ResourceBundle resourceBundle, String prefix, final TextOperationAction textOperationAction, ITextEditor editor,ISourceViewer sourceViewer,InformationPresenter informationPresenter) 
	{
		super(resourceBundle, prefix, editor);
		if (textOperationAction == null)
			throw new IllegalArgumentException();
		this.textOperationAction= textOperationAction;
		this.sourceViewer = sourceViewer;
		this.informationPresenter = informationPresenter;
	}
	
	/*
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {

		/**
		 * Information provider used to present the information.
		 * 
		 * @since 3.0
		 */
		class InformationProvider implements IInformationProvider, IInformationProviderExtension2 {

			private IRegion fHoverRegion;
			private String fHoverInfo;
			private IInformationControlCreator fControlCreator;
			
			InformationProvider(IRegion hoverRegion, String hoverInfo, IInformationControlCreator controlCreator) {
				fHoverRegion= hoverRegion;
				fHoverInfo= hoverInfo;
				fControlCreator= controlCreator;
			}
			/*
			 * @see org.eclipse.jface.text.information.IInformationProvider#getSubject(org.eclipse.jface.text.ITextViewer, int)
			 */
			public IRegion getSubject(ITextViewer textViewer, int invocationOffset) {					
				return fHoverRegion;
			}
			/*
			 * @see org.eclipse.jface.text.information.IInformationProvider#getInformation(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
			 */
			public String getInformation(ITextViewer textViewer, IRegion subject) {
				return fHoverInfo;
			}
			/*
			 * @see org.eclipse.jface.text.information.IInformationProviderExtension2#getInformationPresenterControlCreator()
			 * @since 3.0
			 */
			public IInformationControlCreator getInformationPresenterControlCreator() {
				return fControlCreator;
			}
		}

		if (sourceViewer == null) {	
			textOperationAction.run();
			return;
		}
			
		if (sourceViewer instanceof ITextViewerExtension4)  {
			ITextViewerExtension4 extension4= (ITextViewerExtension4) sourceViewer;
			if (extension4.moveFocusToWidgetToken())
				return;
		}
		
		ITextViewerExtension2 textViewerExtension2= (ITextViewerExtension2) sourceViewer;
		
		// does a text hover exist?
		ITextHover textHover= textViewerExtension2.getCurrentTextHover();
		if (textHover == null) {
			textOperationAction.run();
			return;				
		}

		Point hoverEventLocation= textViewerExtension2.getHoverEventLocation();
		int offset= computeOffsetAtLocation(sourceViewer, hoverEventLocation.x, hoverEventLocation.y);
		if (offset == -1) {
			textOperationAction.run();
			return;				
		}				

		try {
			// get the text hover content
			String contentType= TextUtilities.getContentType(sourceViewer.getDocument(), IDocument.DEFAULT_CONTENT_TYPE, offset, true);

			IRegion hoverRegion= textHover.getHoverRegion(sourceViewer, offset);						
			if (hoverRegion == null)
				return;
			
			String hoverInfo= textHover.getHoverInfo(sourceViewer, hoverRegion);

			IInformationControlCreator controlCreator= null;				
			if (textHover instanceof IInformationProviderExtension2)
				controlCreator= ((IInformationProviderExtension2)textHover).getInformationPresenterControlCreator();

			IInformationProvider informationProvider= new InformationProvider(hoverRegion, hoverInfo, controlCreator);

			this.informationPresenter.setOffset(offset);	
			this.informationPresenter.setDocumentPartitioning(IDocument.DEFAULT_CONTENT_TYPE);
			this.informationPresenter.setInformationProvider(informationProvider, contentType);
			this.informationPresenter.showInformation();

		} catch (BadLocationException e) {				
		}
	}

	// modified version from TextViewer
	private int computeOffsetAtLocation(ITextViewer textViewer, int x, int y) {
		
		StyledText styledText= textViewer.getTextWidget();
		IDocument document= textViewer.getDocument();
		
		if (document == null)
			return -1;		

		try {
			int widgetLocation= styledText.getOffsetAtLocation(new Point(x, y));
			if (textViewer instanceof ITextViewerExtension5) {
				ITextViewerExtension5 extension= (ITextViewerExtension5) textViewer;
				return extension.widgetOffset2ModelOffset(widgetLocation);
			} else {
				IRegion visibleRegion= textViewer.getVisibleRegion();
				return widgetLocation + visibleRegion.getOffset();
			}
		} catch (IllegalArgumentException e) {
			return -1;	
		}

	}
}
