/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.text.hover;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommand;
import org.eclipse.ui.commands.ICommandManager;
import org.eclipse.ui.commands.IKeySequenceBinding;
import org.eclipse.ui.keys.KeySequence;

import com.something.eclipse.script.contentassist.HTMLTextPresenter;
import com.something.eclipse.script.text.ScriptWordFinder;
import com.something.eclipse.shelled.ui.actions.ActionDefinitionIds;

/**
 * @author Doug Satchwell
 * @version $Id: SHTextHover.java,v 1.1 2004/08/17 19:57:04 dougsatch Exp $
 */
public class SHTextHover implements ITextHover,ITextHoverExtension
{
	
	private ICommand fCommand;
	{
		ICommandManager commandManager= PlatformUI.getWorkbench().getCommandSupport().getCommandManager();
		fCommand= commandManager.getCommand(ActionDefinitionIds.SHOW_MANPAGE);
		if (!fCommand.isDefined())
			fCommand= null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion)
	{
		StringBuffer hoverInfo = null;
		if (hoverRegion != null) 
		{
			if (hoverRegion.getLength() > -1)
			{
				URL url = null;
				try
				{
					String text = textViewer.getDocument().get(hoverRegion.getOffset(), hoverRegion.getLength());								
					url = this.getClass().getClassLoader().getResource("/com/something/eclipse/shelled/resources/manpages/"+text+".html");
 //					  url = new URL("http://man.he.net/man1/chgrp");
 //                   url = new URL("file:///C:\\chgrp.html");
					if (url!=null)
					{
						URLConnection connection = url.openConnection();
						connection.connect();
			            InputStream is = null;
			            for (int i = 0; i < 3; i++) 
			            {
			                try 
							{
			                    is = connection.getInputStream();
			                    break;
			                } 
			                catch (IOException ex) 
							{}
			            }
			            if (is != null) 
			            {
			                try 
							{
			                    byte[] buffer = new byte[100 * 1024];
			                    int length;
			                    hoverInfo = new StringBuffer();
			                    while ((length = is.read(buffer)) >= 0) 
			                    {
			                    	for(int i=0;i<length;i++)
			                    		hoverInfo.append((char)buffer[i]);
			                    }
			                } 
			                finally 
							{
			                    is.close();
			                }
			            }
					}
				}
				catch(MalformedURLException x1)
				{
					hoverInfo = new StringBuffer();
					hoverInfo.append("Malformed URL "+url+".\nPlease correct using Rosetta preference pages.");
				}
				catch(IOException x)
				{
					hoverInfo = new StringBuffer();
					hoverInfo.append("Error connecting to URL "+url);
				} 
				catch (BadLocationException e) 
				{}
			}
		}
		return hoverInfo == null ? null : hoverInfo.toString(); 
	}
	
	/*
	 * @see ITextHoverExtension#getHoverControlCreator()
	 * @since 3.0
	 */
	public IInformationControlCreator getHoverControlCreator() {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, SWT.NONE, new HTMLTextPresenter(true), getTooltipAffordanceString());
			}
		};
	}
	
	/**
	 * Returns the tool tip affordance string.
	 * 
	 * @return the affordance string or <code>null</code> if disabled or no key binding is defined
	 * @since 3.0
	 */
	protected String getTooltipAffordanceString() 
	{
		KeySequence[] sequences= getKeySequences();
		if (sequences == null)
			return null;
		
		String keySequence= sequences[0].format();
		return "Press '"+keySequence+"' for focus";
	}

	/**
	 * Returns the array of valid key sequence bindings for the
	 * show tool tip description command.
	 * 
	 * @return the array with the {@link KeySequence}s
	 * 
	 * @since 3.0
	 */
	private KeySequence[] getKeySequences() {
		if (fCommand != null) {
			List list= fCommand.getKeySequenceBindings();
			if (!list.isEmpty()) {
				KeySequence[] keySequences= new KeySequence[list.size()];
				for (int i= 0; i < keySequences.length; i++) {
					keySequences[i]= ((IKeySequenceBinding) list.get(i)).getKeySequence();
				}
				return keySequences;
			}		
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextHover#getHoverRegion(org.eclipse.jface.text.ITextViewer, int)
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) 
	{
		return ScriptWordFinder.findWord(textViewer.getDocument(), offset);
	}
}
