package com.atencion24.ventanas;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TreeField;
import net.rim.device.api.ui.component.TreeFieldCallback;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.atencion24.control.Sesion;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.CustomTreeField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.GridFieldManager;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.ListStyleLabelField;

public class ConsultarEstadoDeCuenta extends plantilla_screen implements FieldChangeListener {

    BitmapField bitmapField;
    
    Sesion usuario;
    ObjectChoiceField instiLista;
    DateField fechaInicial;
    DateField fechaFinal;
    
    CustomButtonField verRepor;
    
    ListStyleButtonSet contentCasino   = new ListStyleButtonSet();
    ListStyleButtonSet contentBanco    = new ListStyleButtonSet();
    ListStyleButtonSet contentSaldos   = new ListStyleButtonSet();
    ListStyleButtonSet contentReversos = new ListStyleButtonSet();
    
    VerticalFieldManager fieldManagerCasino;
    VerticalFieldManager fieldManagerBanco;
    VerticalFieldManager fieldManagerSaldos;
    VerticalFieldManager fieldManagerReversos;
    
    ListStyleLabelField Casino;
    ListStyleLabelField Banco;
    ListStyleLabelField Saldos;
    ListStyleLabelField Reversos;
    
    CustomButtonTable CasinoTD;
    CustomButtonTable CasinoTC;
    CustomButtonTable PtoVentaCas;
    CustomButtonTable CasinoChequeOrig;
    CustomButtonTable SubTotalCas;
    CustomButtonTable CasinoTC_Troq;
    CustomButtonTable TotalCas;
    CustomButtonTable CasinoUSD;
    CustomButtonTable BcoTD;
    CustomButtonTable BcoTC;
    CustomButtonTable PtoVentaBan;
    CustomButtonTable BcoChequeOrig;
    CustomButtonTable SubTotalBan;
    CustomButtonTable BcoTC_Troq;
    CustomButtonTable TotalBan;
    CustomButtonTable BcoTC_US;
    CustomButtonTable SdoCaja;
    CustomButtonTable SdoUSD;
    CustomButtonTable SdoFB;
    CustomButtonTable SdoTransf;
    CustomButtonTable SoftCountOrig;
    CustomButtonTable HardCountOrig;
    CustomButtonTable ReIP;
    CustomButtonTable RevMesa;
    CustomButtonTable RevMaq;
    CustomButtonTable RevOtros;
    CustomButtonTable RevGiros;
    CustomButtonTable TotalRev;
    CustomButtonTable EVEfectMesa;
    CustomButtonTable EVChequeMesa;
    CustomButtonTable EVEfectMaq;
    
	ConsultarEstadoDeCuenta(Sesion usu) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
        this.usuario = usu;
        String[] insti = {"uno", "dos"};
        
        
        Manager foreground = new ForegroundManager();
        
        Bitmap logoBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/gray-arrow.png");
        bitmapField = new BitmapField(logoBitmap);           
        foreground.add(new CustomLabelField(null, Color.WHITE, 0x600808, logoBitmap, FIELD_HCENTER));        
        
        foreground.add(new SeparatorField());
        Bitmap loginImage = Bitmap.getBitmapResource("com/atencion24/imagenes/logo.png");
        foreground.add(new CustomLabelField("Control de Caja", Color.WHITE, Color.DARKGRAY, null, FIELD_HCENTER));
        foreground.add(new SeparatorField());
        foreground.add(new CustomLabelField("Introduzca la información del reporte:", Color.WHITE, Color.DARKGRAY, loginImage, Field.USE_ALL_WIDTH));
        foreground.add(new SeparatorField());
        
        
        LabelField espacio = new LabelField("", Field.FIELD_RIGHT);
        LabelField espacio2 = new LabelField("", Field.FIELD_RIGHT);
                
        fechaInicial = new DateField("",  System.currentTimeMillis(),  new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT);                        
        LabelField fechaI = new LabelField("Fecha Inicial: ", Field.FIELD_RIGHT);
            
        fechaFinal = new DateField("",  System.currentTimeMillis() , new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT); 
        
        LabelField fechaF = new LabelField("Fecha Final: ", Field.FIELD_RIGHT);
        
        LabelField institucion = new LabelField("Institución: ", Field.FIELD_RIGHT);
        
        instiLista = new ObjectChoiceField("", insti, 0, FIELD_LEFT );
        GridFieldManager gridFieldManager = new GridFieldManager(2, 0);
        gridFieldManager.add(espacio);
        gridFieldManager.add(new NullField());
        gridFieldManager.add(fechaI);
        gridFieldManager.add(fechaInicial);
        gridFieldManager.add(fechaF);
        gridFieldManager.add(fechaFinal);
        gridFieldManager.add(institucion);
        gridFieldManager.add(instiLista);
        foreground.add(gridFieldManager);
        gridFieldManager.add(espacio2);
        gridFieldManager.add(new NullField());
        
        verRepor = new CustomButtonField("Ver Reporte", Color.WHITE, Color.DARKGRAY, Color.WHITE, Color.CORNFLOWERBLUE , 0);
        verRepor.setChangeListener(this);
        VerticalFieldManager buttonManager = new VerticalFieldManager(FIELD_HCENTER);
        buttonManager.add(verRepor); 
        foreground.add(buttonManager);
        
        foreground.add(new SeparatorField());
        foreground.add( contentCasino );
        foreground.add( contentBanco );
        foreground.add( contentSaldos );
        foreground.add( contentReversos );
        
        add( foreground );
		
		
		
		/*super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		add(new LabelField("Estado de Cuenta"));
		
		//AQUI
		        
        String fieldOne =  new String("FechaEnviadoAPagar1");
        String fieldTwo =  new String("FechaEnviadoAPagar2");

        CustomLabelField hijo1 =  new CustomLabelField("Monto Liberado", Color.WHITE,  0x990000, FIELD_LEFT);
        CustomLabelField hijo2 =  new CustomLabelField("Gastos Administrativos A",  Color.WHITE,  0x990000, FIELD_LEFT);
        CustomLabelField hijo3 =  new CustomLabelField("Arrendamiento Consultorios",  Color.WHITE,  0x990000, FIELD_LEFT);
        
        TreeField myTree = new CustomTreeField(new TreeCallback(), Field.FOCUSABLE);
        
        int node1 = myTree.addChildNode(0, fieldOne);
        myTree.addChildNode(0, fieldTwo);
        myTree.addChildNode(node1, hijo1);
        myTree.addChildNode(node1, hijo2);
        myTree.addChildNode(node1, hijo3);

       
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
	            else if (obj instanceof CustomLabelField) {
	            	g.drawText("Hola", indent, y);//((CustomLabelField) obj).paint(g); //
	            	/*g.setBackgroundColor(((CustomLabelField) obj).getBackgroundColor());
	            	g.clear();
	                g.setColor(((CustomLabelField) obj).getForegroundColor());
	                g.drawText(((CustomLabelField) obj).getLabel(), 0, 0, DrawStyle.ELLIPSIS, getWidth());*/
	           /* }
	            	
	        }*/
	
	}
	
	public void crearTablaCasino(String cj){
        
        contentCasino.deleteAll();
        Casino = new ListStyleLabelField( "Casino", DrawStyle.HCENTER);
        fieldManagerCasino = new VerticalFieldManager();
            try {
                FontFamily alphaSansFamily = FontFamily.forName(FontFamily.FAMILY_SYSTEM);
                Font appFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt);
                
                CasinoTD = new CustomButtonTable(" Tarjetas de débito:", cj, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                          
                CasinoTC = new CustomButtonTable(" Tarjetas de Crédito:", cj, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                
                PtoVentaCas = new CustomButtonTable(" Total Puntos de Venta:", cj, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                              
                CasinoChequeOrig = new CustomButtonTable(" Cheques:", cj, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                              
                SubTotalCas = new CustomButtonTable(" Sub Total:", cj, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                              
                CasinoTC_Troq = new CustomButtonTable(" Tarjetas Troqueladas:", cj, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                              
                TotalCas = new CustomButtonTable(" Total:", cj, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                              
                CasinoUSD = new CustomButtonTable(" Divisas:", cj, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                
                CasinoTD.setFont(appFont);
                CasinoTC.setFont(appFont);
                PtoVentaCas.setFont(appFont);
                CasinoChequeOrig.setFont(appFont);
                SubTotalCas.setFont(appFont);
                CasinoTC_Troq.setFont(appFont);
                TotalCas.setFont(appFont);
                CasinoUSD.setFont(appFont);
                        
                
                fieldManagerCasino.add(CasinoTD);
                fieldManagerCasino.add(CasinoTC);
                fieldManagerCasino.add(PtoVentaCas);
                fieldManagerCasino.add(CasinoChequeOrig);
                fieldManagerCasino.add(SubTotalCas);
                fieldManagerCasino.add(CasinoTC_Troq);
                fieldManagerCasino.add(TotalCas);
                fieldManagerCasino.add(CasinoUSD);
                
                
            }         
        catch (ClassNotFoundException e) {}
        
        fieldManagerCasino.add(new NullField());
        contentCasino.add(Casino);
        contentCasino.add(fieldManagerCasino);
        
    }
		
	public void llamadaExitosa(String respuesta) {
		crearTablaCasino("Hey");
		

	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}


	public void fieldChanged(Field field, int context) {
		if (field == verRepor) {
            llamadaExitosa("hola");
        }
		
	}

}
