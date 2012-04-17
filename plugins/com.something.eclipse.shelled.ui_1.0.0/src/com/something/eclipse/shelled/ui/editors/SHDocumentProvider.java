/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import com.something.eclipse.shelled.ui.text.SHPartitionScanner;

/**
 * 
 * @author Doug Satchwell
 * @version $Id: SHDocumentProvider.java,v 1.1 2004/08/17 19:57:03 dougsatch Exp $
 */
public class SHDocumentProvider extends FileDocumentProvider
{
	private SHEditor editor;
	
	public SHDocumentProvider(SHEditor editor)
	{
		super();
		this.editor = editor;
	}
	
	protected IDocument createDocument(Object element) throws CoreException 
	{
		IDocument document = super.createDocument(element);
		if (document != null) 
		{
			IDocumentPartitioner partitioner =
				new DefaultPartitioner(
					new SHPartitionScanner(),SHPartitionScanner.CONTENT_TYPES);
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
	
	protected IDocument createEmptyDocument() {
		return new SHDocument();
	}

}