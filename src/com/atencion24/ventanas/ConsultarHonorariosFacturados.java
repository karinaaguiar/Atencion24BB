package com.atencion24.ventanas;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

public class ConsultarHonorariosFacturados extends MainScreen {

	ConsultarHonorariosFacturados() {
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		add(new LabelField("Honorarios Facturados"));
		// TODO Auto-generated constructor stub
	}
}
