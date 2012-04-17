/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.editors;

import java.util.ArrayList;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IAutoIndentStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.InformationPresenter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.something.eclipse.script.contentassist.HTMLTextPresenter;
import com.something.eclipse.script.text.IndentType;
import com.something.eclipse.script.text.ScriptAutoIndentStrategy;
import com.something.eclipse.script.text.WhitespaceDetector;
import com.something.eclipse.shelled.ui.text.SHPartitionScanner;
import com.something.eclipse.shelled.ui.text.SHScannerManager;
import com.something.eclipse.shelled.ui.text.hover.SHTextHover;

/**
 * @author Doug Satchwell
 * @version $Id: SHEditorConfiguration.java,v 1.1 2004/08/17 19:57:03 dougsatch Exp $
 */
public class SHEditorConfiguration extends SourceViewerConfiguration
{
	private SHScannerManager scannerManager;
	private SHEditor editor;

	
	public SHEditorConfiguration(SHEditor editor, ColorManager colorManager, int shellType) 
	{
		this.scannerManager = new SHScannerManager(colorManager, shellType);
		this.editor = editor;
	}

	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) 
	{
		return new SHTextHover();
	}
	
	/**
	 * Needed for text hover
	 */
	public IInformationControlCreator getInformationControlCreator(ISourceViewer sourceViewer)
	{
		return new IInformationControlCreator()
		{
			public IInformationControl createInformationControl(Shell parent)
			{
				return new DefaultInformationControl(
					parent,
					SWT.NONE,
					new HTMLTextPresenter(false));
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getInformationPresenter(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IInformationPresenter getInformationPresenter(ISourceViewer sourceViewer)
	{
		InformationPresenter presenter= new InformationPresenter(getInformationPresenterControlCreator(sourceViewer));
		presenter.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		IInformationProvider provider= new SHInformationProvider(editor);
		presenter.setInformationProvider(provider, IDocument.DEFAULT_CONTENT_TYPE);
		presenter.setSizeConstraints(42, 15, true, true);
		return presenter;
	}
		
	/**
	 * Returns the information presenter control creator. The creator is a factory creating the
	 * presenter controls for the given source viewer. This implementation always returns a creator
	 * for <code>DefaultInformationControl</code> instances.
	 * 
	 * @param sourceViewer the source viewer to be configured by this configuration
	 * @return an information control creator
	 * @since 2.1
	 */
	private IInformationControlCreator getInformationPresenterControlCreator(ISourceViewer sourceViewer)
	{
		return new IInformationControlCreator()
			{
				public IInformationControl createInformationControl(Shell parent)
				{
					int shellStyle = SWT.RESIZE;
					int style = SWT.V_SCROLL|SWT.H_SCROLL;
					return new DefaultInformationControl(parent,shellStyle,style,new HTMLTextPresenter(false));
				}
			};
	}
	
	public IAutoIndentStrategy getAutoIndentStrategy(ISourceViewer sourceViewer,String contentType)
	{
		ScriptAutoIndentStrategy strategy = new ScriptAutoIndentStrategy();
		
		ArrayList rules = new ArrayList();
		rules.add(new WhitespaceRule(new WhitespaceDetector()));
		rules.add(getKeywords(new Token(IndentType.INCREMENT),new String[]{"do","case","{","then"},Token.UNDEFINED));
		rules.add(getKeywords(new Token(IndentType.DECREMENT),new String[]{"done","esac","}","fi","else","elif"},Token.UNDEFINED));
//		rules.add(getKeywords(new Token(IndentType.INFLEXION),new String[]{"else","elif"},Token.UNDEFINED));
		
		strategy.setRules((IRule[])rules.toArray(new IRule[0]));
		return strategy;
	}
		
	private static IRule getKeywords(IToken keywordToken,final String[] words, IToken defaultToken)
	{
		WordRule wordL = new WordRule(new IWordDetector(){

			public boolean isWordStart(char c)
			{
				return !Character.isWhitespace(c);
			}

			public boolean isWordPart(char c)
			{
				return !Character.isWhitespace(c);
			}},defaultToken);	
					
		for (int i = 0;i < words.length; i++)
			wordL.addWord(words[i],keywordToken);
		return wordL;
	}

	
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) 
	{
		return SHPartitionScanner.CONTENT_TYPES;
	}
	
	/**
	 * Provides the reconciler that deals with syntax highlighting
	 */
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) 
	{
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scannerManager.getScanner(IDocument.DEFAULT_CONTENT_TYPE));
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		dr = new DefaultDamagerRepairer(scannerManager.getScanner(SHPartitionScanner.COMMENT_CONTENT_TYPE));	
		reconciler.setDamager(dr, SHPartitionScanner.COMMENT_CONTENT_TYPE);
		reconciler.setRepairer(dr, SHPartitionScanner.COMMENT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(scannerManager.getScanner(SHPartitionScanner.HASHBANG_CONTENT_TYPE));	
		reconciler.setDamager(dr, SHPartitionScanner.HASHBANG_CONTENT_TYPE);
		reconciler.setRepairer(dr, SHPartitionScanner.HASHBANG_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(scannerManager.getScanner(SHPartitionScanner.SINGLE_QUOTE_CONTENT_TYPE));	
		reconciler.setDamager(dr, SHPartitionScanner.SINGLE_QUOTE_CONTENT_TYPE);
		reconciler.setRepairer(dr, SHPartitionScanner.SINGLE_QUOTE_CONTENT_TYPE);
		
		dr = new DefaultDamagerRepairer(scannerManager.getScanner(SHPartitionScanner.DOUBLE_QUOTE_CONTENT_TYPE));	
		reconciler.setDamager(dr, SHPartitionScanner.DOUBLE_QUOTE_CONTENT_TYPE);
		reconciler.setRepairer(dr, SHPartitionScanner.DOUBLE_QUOTE_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(scannerManager.getScanner(SHPartitionScanner.EVAL_CONTENT_TYPE));	
		reconciler.setDamager(dr, SHPartitionScanner.EVAL_CONTENT_TYPE);
		reconciler.setRepairer(dr, SHPartitionScanner.EVAL_CONTENT_TYPE);

		return reconciler;
	}
}
