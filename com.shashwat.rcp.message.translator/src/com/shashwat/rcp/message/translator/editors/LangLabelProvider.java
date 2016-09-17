package com.shashwat.rcp.message.translator.editors;

import org.eclipse.jface.viewers.LabelProvider;

import com.memetix.mst.language.Language;

public class LangLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		try {
			if (element instanceof Language) {
				Language lang = (Language) element;
				return lang.getName(lang);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.getText(element);
	}
}
