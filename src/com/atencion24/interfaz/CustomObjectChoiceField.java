package com.atencion24.interfaz;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;

/**
 * CustomChoiceField provides a reasonable-looking implementation of ObjectChoiceField, for legacy
 * OS versions (e.g. 4.6) for which the default implementation is too clunky.  Newer OS versions
 * (4.7+) should use ObjectChoiceField directly, and avoid this custom class.
 * @author Nathan Scandella
 */
public class CustomObjectChoiceField extends ObjectChoiceField {

   private static final int HIGHLIGHT_COLOR = 0xFFEFB5;  // blue-ish (TODO: make theme-sensitive?)
   private boolean _hasFocus = false;
   
   /** @see ObjectChoiceField(String, Object[], int) */
   public CustomObjectChoiceField(String label, Object[] choices, int initialIndex) {
      super(label, choices, initialIndex);
   }
   
   protected void layout(int width, int height) {
      super.layout(width, height);
   }

   protected void onFocus(int direction) {
	      _hasFocus = true;
	      super.onFocus(direction);
	      invalidate();
	   }

	   protected void onUnfocus() {
	      _hasFocus = false;
	      super.onUnfocus();
	      invalidate();  // required to clear focus
	   }

   /**
    * Paint is overridden to make a custom, semi-transparent, rounded looking field.
    * @param g the Graphics context used for painting
    */
   protected void paint(Graphics g) {
	   if(_hasFocus) 
		   g.setColor(HIGHLIGHT_COLOR);
	   super.paint(g);
   }
}