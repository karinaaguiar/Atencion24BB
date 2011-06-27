package com.atencion24.control;

import com.atencion24.ventanas.V_InicioSesion;

import net.rim.device.api.ui.UiApplication;

public class Atencion24Application extends UiApplication {
	
	public Atencion24Application() {
		pushScreen(new V_InicioSesion());
	}

	public static void main(String[] args) {
		Atencion24Application app = new Atencion24Application();
		app.enterEventDispatcher();

	}

}
