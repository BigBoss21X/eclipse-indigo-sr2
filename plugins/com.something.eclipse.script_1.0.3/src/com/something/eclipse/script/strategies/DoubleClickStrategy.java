/* $Id: DoubleClickStrategy.java,v 1.1 2004/08/17 19:39:56 dougsatch Exp $ */
package com.something.eclipse.script.strategies;

import java.text.CharacterIterator;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.rules.IWordDetector;

/**
 * A double click strategy that makes use of an IWordDetector to determine whether characters 
 * are part of a word.
 * 
 * @author Doug Satchwell
 * @version $Id: DoubleClickStrategy.java,v 1.1 2004/08/17 19:39:56 dougsatch Exp $
 */
public class DoubleClickStrategy implements ITextDoubleClickStrategy
{
	private IWordDetector wordDetector;
	private DocumentCharacterIterator fDocIter= new DocumentCharacterIterator();
	
	public DoubleClickStrategy(IWordDetector wordDetector)
	{
		this.wordDetector = wordDetector;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextDoubleClickStrategy#doubleClicked(org.eclipse.jface.text.ITextViewer)
	 */
	public void doubleClicked(ITextViewer viewer)
	{
		int position= viewer.getSelectedRange().x;		
		if (position < 0)
			return;
			
		IDocument document= viewer.getDocument();
		try
		{
			IRegion line = document.getLineInformationOfOffset(position);
			if (position == line.getOffset() + line.getLength())
				return;
			
			fDocIter.setDocument(document, line);
			
			for (char c = fDocIter.setIndex(position);c != CharacterIterator.DONE && wordDetector.isWordPart(c);c = fDocIter.next())
			{}
			int end = fDocIter.getIndex();
			for (char c = fDocIter.setIndex(position);c != CharacterIterator.DONE && wordDetector.isWordPart(c);c = fDocIter.previous()) 
			{}
			int start = fDocIter.getIndex()+1;

			if (start<end)
				viewer.setSelectedRange(start, end - start);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}
	
	private static class DocumentCharacterIterator implements CharacterIterator 
	{
		/** Document to iterate over. */
		private IDocument fDocument;
		/** Start offset of iteration. */
		private int fOffset= -1;
		/** Endoffset of iteration. */
		private int fEndOffset= -1;
		/** Current offset of iteration. */
		private int fIndex= -1;
		
		/**
		 * Configures this document iterator with the document section to be iteratored. 
		 *
		 * @param document the document to be iterated
		 * @param iteratorRange the range in the document to be iterated
		 */
		public void setDocument(IDocument document, IRegion iteratorRange) {
			fDocument= document;
			fOffset= iteratorRange.getOffset();
			fEndOffset= fOffset + iteratorRange.getLength()+1;
			fOffset--;
		}
		
		/*
		 * @see CharacterIterator#first()
		 */
		public char first() {
			fIndex= fOffset;
			return current();
		}
		
		/*
		 * @see CharacterIterator#last()
		 */
		public char last() {
			  fIndex= fOffset < fEndOffset ? fEndOffset -1 : fEndOffset;
			return current();
		}
		
		/*
		 * @see CharacterIterator#current()
		 */
		public char current() {
			if (fOffset <= fIndex && fIndex < fEndOffset) {
				try {
					return fDocument.getChar(fIndex);
				} catch (BadLocationException x) {
				}
			}
			return DONE;
		}
		
		/*
		 * @see CharacterIterator#next()
		 */
		public char next() {
			if (fIndex == fEndOffset -1)
				return DONE;
			
			if (fIndex < fEndOffset)
				++ fIndex;
				
			return current();
		}
		
		/*
		 * @see CharacterIterator#previous()
		 */
		public char previous() {
			if (fIndex == fOffset)
				return DONE;
				
			if (fIndex > fOffset)
				-- fIndex;
			
			return current();
		}
		
		/*
		 * @see CharacterIterator#setIndex(int)
		 */
		public char setIndex(int index) {
			fIndex= index;
			return current();
		}
		
		/*
		 * @see CharacterIterator#getBeginIndex()
		 */
		public int getBeginIndex() {
			return fOffset;
		}
		
		/*
		 * @see CharacterIterator#getEndIndex()
		 */
		public int getEndIndex() {
			return fEndOffset;
		}
		
		/*
		 * @see CharacterIterator#getIndex()
		 */
		public int getIndex() {
			return fIndex;
		}
		
		/*
		 * @see CharacterIterator#clone()
		 */
		public Object clone() {
			DocumentCharacterIterator i= new DocumentCharacterIterator();
			i.fDocument= fDocument;
			i.fIndex= fIndex;
			i.fOffset= fOffset;
			i.fEndOffset= fEndOffset;
			return i;
		}
	};

}
