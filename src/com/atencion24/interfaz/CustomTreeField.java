package com.atencion24.interfaz;

import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.TreeField;
import net.rim.device.api.ui.component.TreeFieldCallback;

public class CustomTreeField extends TreeField{

	public CustomTreeField(TreeFieldCallback callback, long style) {
		super(callback, style);
		// TODO Auto-generated constructor stub
	}
	
	//Permitir que con darle click al trackball se expanda y colapse el arbol
	protected boolean navigationClick(int status, int time) 
	{
		  // we'll only override unvarnished navigation click behavior
		  if ((status & KeypadListener.STATUS_ALT) == 0 &&
		      (status & KeypadListener.STATUS_SHIFT) == 0)
		  {
		    int node = getCurrentNode();
		    if (getFirstChild(node) != -1) {
		      // node is an internal node
		      setExpanded(node, !getExpanded(node));
		      return true;
		    } else {
		      // click is on a leaf node. Do some default action or else fall through
		    }
		  }
		  return super.navigationClick(status, time);
	}
}
/*
	//aqui
	public class DeviceTreeScreen extends MainScreen
	{    
		TreeField _devTreeField;   
		TreeCallback _treeCallback;    

		public DeviceTreeScreen(HardwareDeviceList deviceList)    
		{       
			_deviceList = deviceList;        
			_treeCallback = new TreeCallback();       
			_devTreeField = new TreeField(_treeCallback, Field.FOCUSABLE);        // we want the rows to be collapsed by default      
			_devTreeField.setDefaultExpanded(false);        // row height should be the height of our largest image       
			_devTreeField.setRowHeight(_readerBmp.getHeight());        
			setDeviceTreeField(null);       
			this.add(_devTreeField);   
		}    
		
		private class TreeCallback implements TreeFieldCallback     
		{        
			public void drawTreeItem(TreeField treeField, Graphics graphics, int node, int y, int width, int indent) 
			{         
				HardwareDevice nodeDevice = (HardwareDevice)treeField.getCookie(node);                        
				Bitmap icon = GetBitmapFromDevice(nodeDevice);           
				graphics.drawBitmap(indent, y, icon.getWidth(), icon.getHeight(), icon, 0, 0);   
				indent += icon.getWidth();          
				if (nodeDevice.getName() == null || nodeDevice.getName() == "")           
					graphics.drawText("(unnamed device)", indent, y, Graphics.ELLIPSIS, width);           
				else               
					graphics.drawText(nodeDevice.getName(), indent, y, Graphics.ELLIPSIS, width);          
				icon = null;           
				nodeDevice = null;                   
			}  
		}
	}
}
*/