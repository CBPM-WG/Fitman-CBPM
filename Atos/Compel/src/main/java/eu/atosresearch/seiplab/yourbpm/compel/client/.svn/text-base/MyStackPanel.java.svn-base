package eu.atosresearch.seiplab.yourbpm.compel.client;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A special purpose widget to allow scrollable stack panels.
 */
public class MyStackPanel extends StackPanel {
	private ArrayList<Widget> scrollers = new ArrayList<Widget>();

	public void insert(Widget w, int before) {
		ScrollPanel p = new ScrollPanel(w);
		p.setWidth("100%");		
		scrollers.add(before, p);
		super.insert(p, before);
	}

	public void onLoad() {
		setWidth("100%");		
		showStack(getSelectedIndex());
	}

	/**
	 * Shows the widget at the specified child index.
	 * 
	 * @param index the index of the child to be shown
	 */
	public void showStack(int index) {
		super.showStack(index);

		if (this.isAttached()) {
			ScrollPanel me = (ScrollPanel) scrollers.get(index);
			//me.setHeight("1px");
			me.setHeight("10px");
			Element tr = DOM.getChild(DOM.getFirstChild(getElement()),
					index * 2 + 1);
			int trHeight = DOM.getElementPropertyInt(tr, "offsetHeight");
			me.setHeight(trHeight + "px");
		}
	}
}