package com.atencion24.ventanas;

import com.atencion24.control.Pago;
import com.atencion24.interfaz.CustomLabelField;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.container.MainScreen;

public class HonorariosPagadosEnProceso extends MainScreen {

	static Pago pago;
	
	public HonorariosPagadosEnProceso(Pago pagoEnProceso) {
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		pago = pagoEnProceso;
		
		add(new CustomLabelField("HonorariosPagadosEnProceso" , Color.WHITE,  0x990000 , Field.USE_ALL_WIDTH));

	}
}
