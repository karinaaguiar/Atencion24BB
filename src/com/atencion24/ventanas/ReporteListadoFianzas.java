package com.atencion24.ventanas;

import net.rim.device.api.ui.component.LabelField;

import com.atencion24.control.Sesion;

public class ReporteListadoFianzas extends plantilla_screen {

	ReporteListadoFianzas(Sesion sesion) {
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		add(new LabelField("Listado de fianzas"));
		// TODO Auto-generated constructor stub
	}

	public void llamadaExitosa(String respuesta) {
		// TODO Auto-generated method stub

	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}

}
