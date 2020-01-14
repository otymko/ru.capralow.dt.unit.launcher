/*******************************************************************************
 * Copyright (c) 2006, 2019 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 * Adapted by Alexander Kapralov
 *
 ******************************************************************************/
package ru.capralow.dt.coverage.internal.ui.annotation;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.xtext.ui.editor.XtextEditor;

import com._1c.g5.v8.dt.md.ui.editor.base.DtGranularEditor;
import com._1c.g5.v8.dt.metadata.mdclass.CommonModule;
import com._1c.g5.v8.dt.ui.editor.input.IDtEditorInput;

/**
 * Tracks the workbench editors and to attach coverage annotation models.
 */
public class EditorTracker {

	private static void annotateEditor(IWorkbenchPartReference partref) {
		IWorkbenchPart part = partref.getPart(false);
		if (part instanceof DtGranularEditor) {
			XtextEditor editor = getModuleEditor((DtGranularEditor) part);

			if (editor == null)
				return;

			CoverageAnnotationModel.attach(editor);
		}
	}

	private static XtextEditor getModuleEditor(DtGranularEditor<CommonModule> editor) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		for (IEditorReference editorReference : page.getEditorReferences()) {
			final IEditorPart[] editorPart = new IEditorPart[1];
			editorPart[0] = editorReference.getEditor(false);
			if (editorPart[0] == null)
				continue;

			if (editorPart[0] instanceof XtextEditor) {
				// Если же мы идем по первой ветке, то есть у нас нет "formEditor", то берем
				// EditorInput, он должен быть файловым, и по файлу уже определяем, например,
				// при помощи сервиса
				// com._1c.g5.v8.dt.core.filesystem.IQualifiedNameFilePathConverter

				return (XtextEditor) editorPart[0];

			} else if (editorPart[0] instanceof FormEditor) {
				FormEditor formEditor = (FormEditor) editorPart[0];
				if (!(formEditor instanceof DtGranularEditor))
					continue;

				@SuppressWarnings("unchecked")
				IDtEditorInput<CommonModule> editorInput = ((DtGranularEditor<CommonModule>) formEditor)
						.getEditorInput();
				if (!editorInput.exists())
					continue;

				if (!editorInput.getModel().getUuid().equals(editor.getModel().getUuid()))
					continue;

				return formEditor.findPage("editors.pages.module").getAdapter(XtextEditor.class); //$NON-NLS-1$

			} else if (editorPart[0] != null) {
				return editorPart[0].getAdapter(XtextEditor.class);

			}

		}

		return null;
	}

	private final IWorkbench workbench;

	private IWindowListener windowListener = new IWindowListener() {
		@Override
		public void windowActivated(IWorkbenchWindow window) {
			// Нечего делать
		}

		@Override
		public void windowClosed(IWorkbenchWindow window) {
			window.getPartService().removePartListener(partListener);
		}

		@Override
		public void windowDeactivated(IWorkbenchWindow window) {
			// Нечего делать
		}

		@Override
		public void windowOpened(IWorkbenchWindow window) {
			window.getPartService().addPartListener(partListener);
		}
	};

	private IPartListener2 partListener = new IPartListener2() {
		@Override
		public void partActivated(IWorkbenchPartReference partref) {
			// Нечего делать
		}

		@Override
		public void partBroughtToTop(IWorkbenchPartReference partref) {
			// Нечего делать
		}

		@Override
		public void partClosed(IWorkbenchPartReference partref) {
			// Нечего делать
		}

		@Override
		public void partDeactivated(IWorkbenchPartReference partref) {
			// Нечего делать
		}

		@Override
		public void partHidden(IWorkbenchPartReference partref) {
			// Нечего делать
		}

		@Override
		public void partInputChanged(IWorkbenchPartReference partref) {
			// Нечего делать
		}

		@Override
		public void partOpened(IWorkbenchPartReference partref) {
			annotateEditor(partref);
		}

		@Override
		public void partVisible(IWorkbenchPartReference partref) {
			// Нечего делать
		}
	};

	public EditorTracker(IWorkbench workbench) {
		this.workbench = workbench;
		for (final IWorkbenchWindow w : workbench.getWorkbenchWindows()) {
			w.getPartService().addPartListener(partListener);
		}
		workbench.addWindowListener(windowListener);
		annotateAllEditors();
	}

	public void dispose() {
		workbench.removeWindowListener(windowListener);
		for (final IWorkbenchWindow w : workbench.getWorkbenchWindows()) {
			w.getPartService().removePartListener(partListener);
		}
	}

	private void annotateAllEditors() {
		for (final IWorkbenchWindow w : workbench.getWorkbenchWindows()) {
			for (final IWorkbenchPage p : w.getPages()) {
				for (final IEditorReference e : p.getEditorReferences()) {
					annotateEditor(e);
				}
			}
		}
	}

}