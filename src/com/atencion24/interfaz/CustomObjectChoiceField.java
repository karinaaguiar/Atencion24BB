package com.atencion24.interfaz;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class CustomObjectChoiceField extends ObjectChoiceField implements FieldChangeListener{

   private String[] mItems;
   private int mIndex;

   /** @see ObjectChoiceField(String, Object[], int) */
   	public CustomObjectChoiceField(String label, Object[] choices, int initialIndex) 
   	{
   		super(label, choices, initialIndex);
   		mItems =(String[]) choices;
   		updateIndex(0);
   		setChangeListener(this);
   	}
   	
   	protected void paint(Graphics graphics) 
   	{
   		super.paint(graphics);
   	}


   	public void updateIndex(int index) 
   	{
	    mIndex = index;
	    //mItem = mItems[mIndex];
	    /*mWidth = mItem.mBitmap.getWidth() + 6 + 18 + 3;
	    mHeight = mItem.mBitmap.getHeight() + 6;
	    invalidate();*/
   	}

	public void fieldChanged(Field field, int context) {
		getScreen().getUiEngine().pushScreen(new DDImagesPopUp());
	}

	class DDImagesPopUp extends PopupScreen implements FieldChangeListener {

		public DDImagesPopUp() 
		{
			super(new VerticalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR));
			CustomButtonList button;
			for (int i = 0; i < mItems.length; i++) 
			{
				if(i==mIndex)
					 button = new CustomButtonList(mItems[i], true); 
				else
					button = new CustomButtonList(mItems[i], false);
				add(button);
				button.setChangeListener(this);
			}
			setFieldWithFocus(getField(mIndex));
		}
		
		protected boolean keyChar(char key, int status, int time) 
		{
		    if (Keypad.KEY_ESCAPE == key) {
		            this.close();
		            return true;
		    } else
		            return super.keyChar(key, status, time);
		}

		public void fieldChanged(Field field, int context) 
		{
			updateIndex(getFieldWithFocusIndex());
			close();
		}
	}
}