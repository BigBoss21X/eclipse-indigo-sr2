/* Copyright (c) 2004 Something Software Ltd. All rights reserved.*/ 
package com.something.eclipse.shelled.ui.preference;

import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.internal.layout.LayoutUtil;

import com.something.eclipse.script.preferences.ScriptPreferencePage;
import com.something.eclipse.shelled.ui.UIPlugin;

/**
 * @author Doug Satchwell
 * @version $Id: MainPreferencePage.java,v 1.1 2004/08/17 19:57:09 dougsatch Exp $
 */
public class MainPreferencePage extends ScriptPreferencePage
{
	private Dialog newExecutableDialog;
	private IStructuredContentProvider contentProvider;
	private TableViewer tableViewer;
	private Button editButton;
	private Button removeButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(final Composite parent)
	{
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent,SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		
		createTable(composite);
		tableViewer.setInput(new String[]{"1","2"});
		
		Composite buttonComposite = new Composite(composite,SWT.TOP);
		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		buttonComposite.setLayout(layout);

		this.editButton = new Button(buttonComposite, SWT.PUSH | SWT.TOP);
		editButton.setText("Edit");
		this.removeButton = new Button(buttonComposite, SWT.PUSH | SWT.TOP);
		removeButton.setText("Remove");

/*		fFilterListField.doFillIntoGrid(composite,3);
		LayoutUtil.setHorizontalSpan(fFilterListField.getLabelControl(null),2);
		LayoutUtil.setWidthHint(fFilterListField.getLabelControl(null),convertWidthInCharsToPixels(40));
		LayoutUtil.setHorizontalGrabbing(fFilterListField.getListControl(null));

		fFilterListField.getTableViewer().setSorter(new ViewerSorter()
			{});*/

		Dialog.applyDialogFont(composite);
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.something.eclipse.script.preferences.ScriptPreferencePage#doGetPreferenceStore()
	 */
	protected IPreferenceStore doGetPreferenceStore()
	{
		return UIPlugin.getDefault().getPreferenceStore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.something.eclipse.script.preferences.ScriptPreferencePage#initializePreferences(org.eclipse.jface.preference.IPreferenceStore)
	 */
	protected void initializePreferences(IPreferenceStore store)
	{}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.something.eclipse.script.preferences.ScriptPreferencePage#storePreferences(org.eclipse.jface.preference.IPreferenceStore)
	 */
	protected void storePreferences(IPreferenceStore store)
	{}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.something.eclipse.script.preferences.ScriptPreferencePage#restoreDefaultPreferences(org.eclipse.jface.preference.IPreferenceStore)
	 */
	protected void restoreDefaultPreferences(IPreferenceStore store)
	{}

	/**
	 * Creates the table viewer.
	 */
	protected void createTable(Composite parent)
	{
		Table table = new Table(parent,SWT.MULTI|SWT.FULL_SELECTION|SWT.BORDER);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		data.heightHint = table.getItemHeight();
		data.horizontalSpan = 1;
		table.setLayoutData(data);
		table.setFont(parent.getFont());

		this.contentProvider = getContentProvider();
		this.tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(contentProvider);
		tableViewer.setLabelProvider(getLabelProvider());
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener()
			{
				public void selectionChanged(SelectionChangedEvent event)
				{
					tableSelectionChanged((IStructuredSelection)event.getSelection());
				}
			});

/*		tableViewer.addDoubleClickListener(new IDoubleClickListener()
			{
				public void doubleClick(DoubleClickEvent event)
				{
					if(!event.getSelection().isEmpty() && editButton.isEnabled())
					{
						edit((IStructuredSelection)event.getSelection());
					}
				}
			});

		table.addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent event)
				{
					if(editButton.isEnabled() && event.character==SWT.DEL&&event.stateMask==0)
					{
						remove(tableViewer);
					}
				}
			}); */
	}

	/**
	 * Returns the content provider to use for the table viewer
	 * 
	 * @return TableContentProvider
	 */
	protected IStructuredContentProvider getContentProvider()
	{
		return new TableContentProvider();
	}

	/**
	 * Returns the content provider to use for the table viewer
	 * 
	 * @return TableContentProvider
	 */
	protected ITableLabelProvider getLabelProvider()
	{
		return new SHTableLabelProvider();
	}

	private class SHTableLabelProvider extends LabelProvider implements ITableLabelProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex)
		{
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex)
		{
			// TODO Auto-generated method stub
			return element.toString();
		}

	}

	/**
	 * Handles selection changes in the table viewer.
	 */
	protected void tableSelectionChanged(IStructuredSelection newSelection)
	{
		int size = newSelection.size();
		boolean enabled = true;

		Iterator itr = newSelection.iterator();
		/*		while(itr.hasNext())
		{
			Object element = itr.next();
			if(element instanceof AntObject)
			{
				AntObject antObject = (AntObject)element;
				if(antObject.isDefault())
				{
					enabled = false;
					break;
				}
			}
		}*/
		editButton.setEnabled(enabled&&size==1);
		removeButton.setEnabled(enabled&&size>0);
	}

	/**
	 * Handles the remove button pressed event
	 */
	protected void remove()
	{
		remove(tableViewer);
	}

	protected void remove(TableViewer viewer)
	{
	// TODO: fill this in
	/*
	 * AntContentProvider antContentProvider= (AntContentProvider)viewer.getContentProvider(); IStructuredSelection sel = (IStructuredSelection) viewer.getSelection(); Iterator itr = sel.iterator();
	 * while (itr.hasNext()) { antContentProvider.remove(itr.next()); }
	 */
	}

	/**
	 * Allows the user to edit a custom Ant object.
	 * 
	 * @param selection
	 *            The selection containing the object to edit
	 */
	protected void edit(IStructuredSelection selection)
	{
	// TODO: fill this in
	}

}