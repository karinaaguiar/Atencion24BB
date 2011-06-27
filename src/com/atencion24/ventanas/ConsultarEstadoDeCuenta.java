package com.atencion24.ventanas;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.TreeField;
import net.rim.device.api.ui.component.TreeFieldCallback;

import com.atencion24.control.Sesion;
import com.atencion24.interfaz.CustomTreeField;
import com.atencion24.interfaz.ListStyleButtonField;
import com.atencion24.interfaz.ListStyleButtonSet;

public class ConsultarEstadoDeCuenta extends plantilla_screen {

	ConsultarEstadoDeCuenta(Sesion sesion) {
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		add(new LabelField("Estado de Cuenta"));
		
		//AQUI
		        
        String fieldOne =  new String("FechaEnviadoAPagar1");
        String fieldTwo =  new String("FechaEnviadoAPagar2");

        ListStyleButtonSet hijo1 =  new ListStyleButtonSet();
        hijo1.add(new ListStyleButtonField( "Monto Liberado", 0 ));
        hijo1.add(new ListStyleButtonField( "Gastos Administrativos A", 0 ));
        hijo1.add(new ListStyleButtonField( "Arrendamiento Consultorios", 0 ));
        ListStyleButtonSet hijo2 =  new ListStyleButtonSet();
        hijo2.add(new ListStyleButtonField( "Monto Liberado", 0 ));
        
        TreeField myTree = new CustomTreeField(new TreeCallback(), Field.FOCUSABLE);
        
        int node1 = myTree.addChildNode(0, fieldOne);
        int node2 = myTree.addChildNode(0, fieldTwo);
        myTree.addChildNode(node1, hijo1);
        myTree.addChildNode(node2, hijo2);
       
        myTree.setDefaultExpanded(false);
        add(myTree);

	}
	 private class TreeCallback implements TreeFieldCallback
	    {
	        public void drawTreeItem(TreeField _tree, Graphics g, int node, int y, int width, int indent) 
	        {
	        	Object obj = _tree.getCookie(node);
	        	 
	            if (obj instanceof String)
	            {
	                String text = (String)obj;
	                // Draws the text of the treefield item.
	                g.drawText(text, indent, y);
	            }
	            else g.drawText("Hola", indent, y);
	        }
	    }
	public void llamadaExitosa(String respuesta) {
		// TODO Auto-generated method stub

	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}

}
