package com.shashwat.rcp.message.translator.editors;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.MultiPageEditorPart;

public class MTEditor extends MultiPageEditorPart implements IResourceChangeListener {
	private TextEditor editor;
	private StyledText text;

	public MTEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input");
		super.init(site, editorInput);
	}


	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	@Override
	protected void createPages() {
		createTextEditorPage();
		createPreviewPage();
	}

	private void createPreviewPage() {
		Composite comp = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		comp.setLayout(layout);
		this.text = new StyledText(comp, SWT.H_SCROLL | SWT.V_SCROLL);
		this.text.setEditable(false);
		int index = addPage(comp);
		setPageText(index, "Preview");
	}

	private void createTextEditorPage() {
		try {
			this.editor = new TextEditor();
			int index = addPage(this.editor, getEditorInput());
			setPageText(index, this.editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating text editor", null, e.getStatus());
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.getEditor(0).doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		IEditorPart editor = this.getEditor(0);
		editor.doSaveAs();
		setPageText(0, this.editor.getTitle());
		setInput(this.editor.getEditorInput());
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
	}
}
