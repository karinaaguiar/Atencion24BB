package com.atencion24.interfaz;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ButtonField;

class CustomButtonList extends ButtonField {
	 private static final int HIGHLIGHT_COLOR = 0xF77100; // naranja
	 private static final int BACKGROUND_C0LOR = 0xB4ACA0; //Gris
	 protected String mItem;
	 boolean focused;
	 int mWidth;
	 int mHeight;

	 public CustomButtonList(String item, boolean focused) 
	 {
		  super(CONSUME_CLICK);
		  mItem = item;
		  mWidth = getFont().getAdvance(mItem) + 6;
		  mHeight = getFont().getHeight();
		  setMargin(0, 0, 0, 0);
		  setPadding(0, 0, 0, 0);
		  //setBorder(BorderFactory.createSimpleBorder(new XYEdges(0, 0, 0, 0)));
		  //setBorder(VISUAL_STATE_ACTIVE, BorderFactory
		    //.createSimpleBorder(new XYEdges(0, 0, 0, 0)));
	 }

	 protected void paint(Graphics graphics) 
	 {
		  int color = (focused) ? HIGHLIGHT_COLOR: BACKGROUND_C0LOR;
		  graphics.setColor(color);
		  //graphics.drawRect(1, 1, mWidth - 2, mHeight - 2);
		  graphics.drawText(mItem, 0, 0);

	 }

	 public int getPreferredWidth() {
	  return mWidth;
	 }

	 public int getPreferredHeight() {
	  return mHeight;
	 }

	 protected void layout(int width, int height) {
	  setExtent(mWidth, mHeight);
	 }
}
