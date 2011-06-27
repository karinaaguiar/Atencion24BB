package com.atencion24.ventanas;

import java.util.TimeZone;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.atencion24.control.ControlDates;
import com.atencion24.control.HttpConexion;
import com.atencion24.control.Sesion;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.GridFieldManager;

public class ConsultarHonorariosPagados extends plantilla_screen implements FieldChangeListener{
	
	static String dateTime = "01/01/2008";
	RadioButtonField reciente;
	RadioButtonField historico;
    DateField fechaInicial;
    DateField fechaFinal;
    CustomButtonField verRepor;
    
    static Sesion sesion;
    
    BitmapField bitmapField;
    
    int insertIndex;
    GridFieldManager gridFieldManager;
    NullField nulo;
    
   
	ConsultarHonorariosPagados(Sesion ses) {
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		
		sesion = ses;
		
		//Cambiar el font de la aplicación
		try {
				FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
				Font appFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
				setFont(appFont);
			}catch (ClassNotFoundException e){}
		
		//Logo CSS alineado al centro
		Bitmap logoBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/logo.png");
		bitmapField = new BitmapField(logoBitmap);
		HorizontalFieldManager hfmLabel = new HorizontalFieldManager(FIELD_HCENTER);
        hfmLabel.add(bitmapField);
        add(hfmLabel);
       
        //**Label field simple**
		add(new CustomLabelField("Honorarios Pagados", Color.WHITE, 0x990000, FIELD_HCENTER));
		add(new SeparatorField());

		add(new CustomLabelField("Bienvenido " +sesion.getNombre() + " " + sesion.getApellido() , Color.WHITE,  0x990000 , Field.USE_ALL_WIDTH));
      
		//Manager foreground = new ForegroundManager();        
        
        //RadioButton para escoger el tipo de consulta a realizar
		RadioButtonGroup tipoConsulta = new RadioButtonGroup();
        reciente = new RadioButtonField("Pago en Proceso ",tipoConsulta,true);
        historico = new RadioButtonField("Histórico",tipoConsulta,false);	
        //foreground.
        add(reciente);
        //foreground.
        add(historico);
        tipoConsulta.setChangeListener(this);
        
        ControlDates dates = new ControlDates();
        fechaInicial = new DateField("", System.currentTimeMillis(), new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT);                        
        fechaInicial.setTimeZone(TimeZone.getDefault());
        fechaInicial.setDate(dates.stringToDate(dateTime)); 
        LabelField fechaI = new LabelField("Desde: ", Field.FIELD_RIGHT);
            
        fechaFinal = new DateField("",  System.currentTimeMillis() , new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT); 
        LabelField fechaF = new LabelField("Hasta: ", Field.FIELD_RIGHT);
        
	  	gridFieldManager = new GridFieldManager(2, 0);
	  	gridFieldManager.add(fechaI);
        gridFieldManager.add(fechaInicial);
        gridFieldManager.add(fechaF);
        gridFieldManager.add(fechaFinal);
        
        nulo = new NullField();
        add(nulo);
        insertIndex = this.getFieldCount();
        
        //foreground.add(gridFieldManager);
        
        //Boton de consultar
        verRepor = new CustomButtonField(" Consultar ", Color.WHITE, 0x990000 , Color.WHITE, 0xE38311, 0);
        verRepor.setChangeListener(this);
        VerticalFieldManager buttonManager = new VerticalFieldManager(FIELD_HCENTER);
        buttonManager.add(verRepor); 
        //foreground.
        add(buttonManager);
	}

	public void llamadaExitosa(String respuesta) {
		// TODO Auto-generated method stub

	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}

	private void ConsultarHistoricoPagos(String medico){
		//Comparo las fechas. Fecha Desde < Fecha Hasta
		if(fechaInicial.getDate() > fechaFinal.getDate() || fechaInicial.getDate() == fechaFinal.getDate()){
			Dialog.alert("Error al ingresar las fechas. Fecha 'Desde' debe ser menor que fecha 'Hasta'");
		}
		else{
			String fechaI = fechaInicial.toString();
			System.out.println(fechaI);
			String fechaF = fechaFinal.toString();
			System.out.println(fechaF);
			HttpConexion thread = new HttpConexion("/ConsultarHistoricoPagos?medico_tb=" + medico + "&fechaI_tb=" + fechaI.toString() + "&fechaF_tb=" + fechaF.toString(), "GET", this);
			thread.start();
		}
			
	}
	
	private void ConsultarProximoPago(String medico)
	{
		HttpConexion thread = new HttpConexion("/ConsultarProximoPago?medico_tb=" + medico, "GET", this);
		thread.start();
	}
	
	
	public void fieldChanged(Field field, int context) 
	{
	      if (field == historico) 
	      {
	    	  if (historico.isSelected()){	
	    		  	replace(nulo, gridFieldManager);
	    		  	//insert(gridFieldManager, insertIndex);
	    	  }
	    	  else 
	    	  {
		    	   	replace(gridFieldManager, nulo); 
		    	   	//delete(gridFieldManager);
	    	  }
          }
	      if(field == verRepor) {
	    	  String medico = sesion.getCodigoMedico();
	    	  if(historico.isSelected()){
	    		  ConsultarHistoricoPagos(medico);
	    	  }
	    	  else if (reciente.isSelected()){
	    		  ConsultarProximoPago(medico);
	    	  } 
	      }
	}

}
