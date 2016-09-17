package com.shashwat.rcp.message.translator.dialog;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.memetix.mst.language.Language;
import com.shashwat.rcp.message.translator.editors.LangLabelProvider;

public class LanguageDialog extends Dialog{
	private IFile fileToTranslate;
	private Text txtFileNameToTranslate;
	private ComboViewer comboViewer;
	private Text txtWriteToFileName;
	private Language langauage;
	private String writeToFileName;

	/**
	 * Constructor
	 * @param parent
	 * @param fileToTranslate
	 */
	public LanguageDialog(Shell parent, IFile fileToTranslate) {
		super(parent);
		this.fileToTranslate = fileToTranslate;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		((GridLayout)composite.getLayout()).numColumns = 2;
		
		Label lblReadFromLang = new Label(composite, SWT.NONE);
		lblReadFromLang.setText("Read From");
		GridDataFactory.fillDefaults().grab(false, false).align(SWT.RIGHT, SWT.CENTER).span(1, 1).applyTo(lblReadFromLang);
		
		this.txtFileNameToTranslate = new Text(composite, SWT.BORDER);
		this.txtFileNameToTranslate.setText(this.fileToTranslate.getFullPath().toOSString());
		this.txtFileNameToTranslate.setEnabled(false);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.RIGHT, SWT.CENTER).span(1, 1).applyTo(this.txtFileNameToTranslate);
		
		Label lblLanguage = new Label(composite, SWT.NONE);
		lblLanguage.setText("Language");
		GridDataFactory.fillDefaults().grab(false, false).align(SWT.RIGHT, SWT.CENTER).span(1, 1).applyTo(lblLanguage);
		
		this.comboViewer = new ComboViewer(composite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.RIGHT, SWT.CENTER).span(1, 1).applyTo(this.comboViewer.getControl());
		this.comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		this.comboViewer.setLabelProvider(new LangLabelProvider());
		this.comboViewer.setInput(Language.values());
		
		Label lblWriteToLang = new Label(composite, SWT.NONE);
		lblWriteToLang.setText("Language");
		GridDataFactory.fillDefaults().grab(false, false).align(SWT.RIGHT, SWT.CENTER).span(1, 1).applyTo(lblWriteToLang);
		
		this.txtWriteToFileName = new Text(composite, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.RIGHT, SWT.CENTER).span(1, 1).applyTo(this.txtWriteToFileName);
		this.txtWriteToFileName.setText("Write to file");
		this.txtWriteToFileName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String str = txtWriteToFileName.getText();
				if (!str.endsWith(".properties")) {
					str += ".properties";
				}
				IContainer pFolder = fileToTranslate.getParent();
				IFile file = pFolder.getFile(new Path(str));
				if (file.exists()) {
					getButton(OK).setEnabled(false);
					txtWriteToFileName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
				} else {
					getButton(OK).setEnabled(true);
					txtWriteToFileName.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				}
			}
		});
		
		return composite;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(400, 200);
	}
	
	public Language getLanguage() {
		return this.langauage;
	}
	
	@Override
	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) this.comboViewer.getSelection();
		if (!selection.isEmpty()) {
			this.langauage = (Language) selection.getFirstElement();
		}
		this.writeToFileName = this.txtWriteToFileName.getText();
		if (!this.writeToFileName.endsWith(".properties")) {
			this.writeToFileName += ".properties";
		}
		super.okPressed();
	}
	
	public String getFileName() {
		return this.writeToFileName;
	}
}
