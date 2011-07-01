package com.atencion24.control;

public class Descuento 
{
	private String fecha;
	private String monto;
	
	public Descuento() {}
	
	public Descuento(String fecha, String monto) 
	{  
		this.setFecha(fecha);
		this.setMonto(monto);
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return fecha;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getMonto() {
		return monto;
	}
}
