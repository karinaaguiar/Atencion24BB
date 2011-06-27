package com.atencion24.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.RadioInfo;

import com.atencion24.ventanas.plantilla_screen;

public class HttpConexion extends Thread {

	//IP y ubicaci�n donde se encuentra el Web Service de Atencion24
	private String url = "http://localhost/Atencion24WebServices/WebServices.asmx";	
    private String method; // GET or POST
    private plantilla_screen ventana;
    private byte[] postData;
  
  //Constructor m�todo get
	public HttpConexion(String datosAEnviar, String method, plantilla_screen screen) {
		this.url = url + datosAEnviar;
		this.method = method;
		this.ventana = screen;
	}
	
	//Constructor m�todo post
    public HttpConexion(String method, plantilla_screen screen, byte[] postData) {
        this.method = method;
        this.ventana = screen;
        this.postData = postData;
    }
    
    /*No se por qu� razon marcel agreg� esto
     * public String replace(String text, String searchString, String replacementString)
    {
        StringBuffer sBuffer = new StringBuffer();
        int pos = 0;
    
        while ((pos = text.indexOf(searchString)) != -1)
        {
            sBuffer.append(text.substring(0, pos) + replacementString);
            text = text.substring(pos + searchString.length());
        }
    
        sBuffer.append(text);
        return sBuffer.toString();
    }*/
    
    //Metodo para determinar el registro del service book
    //correspondiente al servicio de Wap2.0
    //Requiere de firmas (Key Signatures),
    //por lo tanto no se utilizar� por ahora
    /*private ServiceRecord getWAP2ServiceRecord() {
        ServiceBook sb = ServiceBook.getSB();
        ServiceRecord[] records = sb.getRecords();

        for (int i = 0; i < records.length; i++) {
            String cid = records[i].getCid().toLowerCase();
            String uid = records[i].getUid().toLowerCase();
            if (cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") == -1
                    && uid.indexOf("mms") == -1) {
                return records[i];
            }
        }

        return null;
    }*/
    
    /**
        * Generates a connection using the WIFI transport if available
        * 
        * @return An {@link HttpConnection} if this transport is available, otherwise null
        * @throws IOException throws exceptions generated by {@link getConnection( String transportExtras1, String transportExtras2 )}
    */
    private boolean wifiConnection() throws IOException {
    if(    RadioInfo.areWAFsSupported( RadioInfo.WAF_WLAN )
        && ( RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN ) != 0
        && CoverageInfo.isCoverageSufficient( 1 /* CoverageInfo.COVERAGE_DIRECT */, RadioInfo.WAF_WLAN, false) ) {
         
           return true;                       
    }
    return false;
   }

   /**
    * Generates a connection using the BES transport if available
    * 
    * @return An {@link HttpConnection} if this transport is available, otherwise null
    * @throws IOException throws exceptions generated by {@link getConnection( String transportExtras1, String transportExtras2 )}
    */
    private boolean besConnection( ) throws IOException {
                if( CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS) ) {
                    return true;   
             }
              return false;
   }
   
    public void run() {
        try {
        	
        	System.out.println("Estoy empezando a establecer conexion");
        	//SE DEFINE EL TIPO DE CONEXION A ESTABLECER
        	
        	String connectionParameters = "";
            //if (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED) {
        	if (wifiConnection()) 
        	{
                // Connected to a WiFi access point
                connectionParameters = ";interface=wifi";
            }
        	else 
        	{
                int coverageStatus = CoverageInfo.getCoverageStatus();
                //Usar cuando se tenga el key signature
                /*ServiceRecord record = getWAP2ServiceRecord();
                if (record != null && 
                	(coverageStatus & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT) {
                    // Have network coverage and a WAP 2.0 service book record
                    connectionParameters = ";deviceside=true;ConnectionUID="
                            + record.getUid();
                } else */
                
                /*if ((coverageStatus & CoverageInfo.COVERAGE_MDS) == 
                        CoverageInfo.COVERAGE_MDS) {*/
                if (besConnection()) 
                {
                    // Have an MDS service book and network coverage
                    connectionParameters = ";deviceside=false";
                } 
                else if ((coverageStatus & CoverageInfo.COVERAGE_DIRECT) == 
                        CoverageInfo.COVERAGE_DIRECT) {
                    // Have network coverage but no WAP 2.0 service book record
                    connectionParameters = ";deviceside=true";
                    //connectionParameters = ""; xq marcel usa esto?
                }
            }    
            
        	//SE ESTABLECE LA CONEXION
            //url = replace(url, " ", "%20"); xq marcel usa esto?
            HttpConnection connection = (HttpConnection) Connector.open(url + connectionParameters);
            connection.setRequestMethod(method);
            
            System.out.println("Se estableci� la conexion");
            System.out.println(connectionParameters);
            
            //REALIZA ACCIONES SEGUN EL METODO (GET/POST)
            //Si el m�todo es post
			if (method.equals("POST") && postData != null) {
                connection.setRequestProperty("Content-type",
                        "application/x-www-form-urlencoded");
                OutputStream requestOutput = connection.openOutputStream();
                requestOutput.write(postData);
                requestOutput.close();
            }
			
			//Se obtiene el c�digo de respuesta 200 Ok 401 No autorizado
			int responseCode = connection.getResponseCode();
			
			if(responseCode!=HttpConnection.HTTP_OK) {
				ventana.llamadaFallada("Error realizando la conexi�n: " + responseCode);
				connection.close();
				return;
			}
			
			//GET method. Se lee el response
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			InputStream responseData = connection.openInputStream();
			byte[] buffer = new byte[10000];
			int bytesRead = responseData.read(buffer);
			while(bytesRead > 0){
				baos.write(buffer, 0, bytesRead);
				bytesRead = responseData.read(buffer);
			}
			baos.close();
			connection.close();
			System.out.println("Termin� de leer los datos");
            ventana.llamadaExitosa(new String(baos.toByteArray()));

        } catch (IOException ex) {
            ventana.llamadaFallada("Puede que su se�al de datos sea debil o no este configurada correctamente");
            //ventana.llamadaFallada("Error en clase Connection.java: " + ex.toString());
        }
    }    
}
