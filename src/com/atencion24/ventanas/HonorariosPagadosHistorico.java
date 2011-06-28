package com.atencion24.ventanas;

import java.util.Vector;

import com.atencion24.interfaz.CustomLabelField;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.container.MainScreen;

public class HonorariosPagadosHistorico extends MainScreen {

	static Vector pagos;
	
	public HonorariosPagadosHistorico(Vector historicoPagos) {
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		pagos = historicoPagos;
		
		add(new CustomLabelField("HonorariosPagadosHistorico" , Color.WHITE,  0x990000 , Field.USE_ALL_WIDTH));
		
	}
}
