package com.atencion24.control;

public class EstadoCuentaAS {
	
	private String a30dias;
	private String a60dias;
	private String a90dias;
	private String a180dias;
	private String a360dias;
	private String mas360;
	private String totalDeuda;
	
	public EstadoCuentaAS() {}
	
	public EstadoCuentaAS(String a30dias, String a60dias, String a90dias, String a180dias, String a360dias, String mas360, String totalDeuda) 
	{  
		this.setA30dias(a30dias);
		this.setA60dias(a60dias);
		this.setA90dias(a90dias);
		this.setA180dias(a180dias);
		this.setA360dias(a360dias);
		this.setMas360(mas360);
		this.setTotalDeuda(totalDeuda);
	}

	public void setA30dias(String a30dias) {
		this.a30dias = a30dias;
	}

	public String getA30dias() {
		return a30dias;
	}

	public void setA60dias(String a60dias) {
		this.a60dias = a60dias;
	}

	public String getA60dias() {
		return a60dias;
	}

	public void setA90dias(String a90dias) {
		this.a90dias = a90dias;
	}

	public String getA90dias() {
		return a90dias;
	}

	public void setA180dias(String a180dias) {
		this.a180dias = a180dias;
	}

	public String getA180dias() {
		return a180dias;
	}

	public void setTotalDeuda(String totalDeuda) {
		this.totalDeuda = totalDeuda;
	}

	public String getTotalDeuda() {
		return totalDeuda;
	}

	public void setA360dias(String a360dias) {
		this.a360dias = a360dias;
	}

	public String getA360dias() {
		return a360dias;
	}

	public void setMas360(String mas360) {
		this.mas360 = mas360;
	}

	public String getMas360() {
		return mas360;
	}
}
