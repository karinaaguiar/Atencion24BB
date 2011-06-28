package com.atencion24.interfaz;

/*
 * informacionNivel.java
 *
 * © <your company here>, 2003-2005
 * Confidential and proprietary.
 */


import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Color;
import net.rim.device.api.util.Arrays;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;

/**
 * 
 */
public class InformacionNivel {
        
        String nombreId;
        String nombre;
        String valor;
        // En el caso de los reportes Producto Consolidad,
        // salaEnv represanta el establecimiento, juegoEnv el año
        // y mesaEnv representa el mes.
        String salaEnv, juegoEnv, mesaEnv;
        int nivel;
        int[] pos;
        boolean mostrar = false;
        public InformacionNivel[] hijo = null;
        
        public InformacionNivel(){}
        
        public InformacionNivel(String nombreId, String nombre, String valor, int nivel, int[] pos, String salaEnv, String juegoEnv, String mesaEnv) {  
            
            this.nombreId = nombreId;
            this.nombre = nombre;
            this.valor = valor;
            this.nivel = nivel;
            this.pos = pos;
            this.salaEnv = salaEnv;
            this.juegoEnv = juegoEnv;
            this.mesaEnv = mesaEnv;
            
        }
        
        public void guardarHijo(String[][] datos){
            hijo = new InformacionNivel[datos[0].length];
            for (int i = 0; i < datos[0].length; i++){
                if (nivel == 4)
                    hijo[i] = new InformacionNivel(datos[0][i],datos[0][i],datos[1][i], this.nivel-1, mezclarArrayInt(i,pos), datos[0][i], juegoEnv, mesaEnv);
                else if (nivel == 3)
                    hijo[i] = new InformacionNivel(datos[0][i],datos[0][i],datos[1][i], this.nivel-1, mezclarArrayInt(i,pos), datos[0][i], juegoEnv, mesaEnv);
                else if (nivel == 2)
                    hijo[i] = new InformacionNivel(datos[0][i],datos[0][i],datos[1][i], this.nivel-1, mezclarArrayInt(i,pos), salaEnv, datos[0][i], mesaEnv);
                else if (nivel == 1)
                    hijo[i] = new InformacionNivel(datos[0][i],datos[0][i],datos[1][i], this.nivel-1, mezclarArrayInt(i,pos), salaEnv, juegoEnv, datos[0][i]);
                }
        }
        
        public int[] mezclarArrayInt(int a, int[] b){
            
            int[] c = new int[  b.length + 1 ];
            int tam = 0;
            for(int j = 0; j < b.length; j++){
                c[tam] = b[j];
                tam++; 
            }
            c[tam] = a;
            return c;
        }
        
        public CustomButtonTable[] mezclarArray(CustomButtonTable[] a, CustomButtonTable[] b){
            
            CustomButtonTable[] c = new CustomButtonTable[ a.length + b.length ];
            int tam = 0;
            for(int i = 0; i < a.length; i++){
                c[tam] = a[i];
                tam++;  
            }
            for(int j = 0; j < b.length; j++){
                c[tam] = b[j];
                tam++; 
            }
            return c;
        }
        
        public CustomButtonTable[]  mostrarBotones(){
            
            CustomButtonTable[] botones, auxBotones, aux2Botones;
            
            try {
                FontFamily alphaSansFamily = FontFamily.forName("BBClarity");
                Font appFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt);
                if (mostrar && nivel != 0 && this.hijo != null){
                    auxBotones = new CustomButtonTable[0];
                    for (int i = 0; i < hijo.length; i++){
                        aux2Botones = this.hijo[i].mostrarBotones();
                        auxBotones  = mezclarArray(auxBotones, aux2Botones);
                    }
                    botones = new CustomButtonTable[1];
                    switch (this.nivel){
                        case (3): botones[0] = new CustomButtonTable(this.nombre, this.valor, 0xC8A8A8, Color.LIGHTYELLOW, 0x600808, Color.LIGHTYELLOW, 0xA06B6B, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                        case (2): botones[0] = new CustomButtonTable(this.nombre, this.valor, 0x704B4B, Color.BLACK, 0xFFC0CB, Color.BLACK, 0xFFC0CB, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                        case (1): botones[0] = new CustomButtonTable(this.nombre, this.valor, 0x704B4B, Color.BLACK, 0xF8DCDC, Color.BLACK, 0xF8DCDC, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                        case (0): botones[0] = new CustomButtonTable(this.nombre, this.valor, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                        default:  botones[0] = new CustomButtonTable(this.nombre, this.valor, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                    }
                    botones[0].setFont(appFont);
                    return mezclarArray(botones, auxBotones);
                }else{
                    botones = new CustomButtonTable[1];
                    switch (this.nivel){
                        case (3): botones[0] = new CustomButtonTable(this.nombre, this.valor, 0xC8A8A8, Color.LIGHTYELLOW, 0x600808, Color.LIGHTYELLOW, 0xA06B6B, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                        case (2): botones[0] = new CustomButtonTable(this.nombre, this.valor, 0x704B4B, Color.BLACK, 0xFFC0CB, Color.BLACK, 0xFFC0CB, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                        case (1): botones[0] = new CustomButtonTable(this.nombre, this.valor, 0x704B4B, Color.BLACK, 0xF8DCDC, Color.BLACK, 0xF8DCDC, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                        case (0): botones[0] = new CustomButtonTable(this.nombre, this.valor, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                        default:  botones[0] = new CustomButtonTable(this.nombre, this.valor, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                                  break;
                    }
                    botones[0].setFont(appFont);
                    return botones;
                }
            }catch (ClassNotFoundException e) {
                return null;
            }
        }
        
        
    public String buscarReporteEscf(String fechaIn, String fechaFn, String idInst, int idO, int opc, boolean sockets){ 
    
        String p = "";
        //Creo xml con datos a enviar (Sockets)
            if (opc == 12)
                p = "/verForecastDetalle?FechaOper=" + fechaIn + "&Turno=" + fechaFn + "&idEstablecimiento=" + idInst + "&salaI=" + salaEnv + "&juegoI=" + juegoEnv + "&mesaI=" + mesaEnv + "&IdO=" + String.valueOf(idO);
            if (opc == 5)
                p = "/verMasterGamingEspecifico?FechaIni=" + fechaIn + "&FechaFin=" + fechaFn + "&salaI=" + salaEnv + "&juegoI=" + juegoEnv + "&mesaI=" + mesaEnv + "&id_establ=" + idInst + "&idO=" + String.valueOf(idO);
        return p;
    }
        
    public String buscarNivel(int [] pos, String fechaIn, String fechaFn, String idInst, int idO, int opc, boolean sockets){
            int[] auxPos = new int[0];
            
            for (int i = 0; i < pos.length; i++){
                System.out.println(this.nivel+": "+pos[i]);
            }
            if (pos.length > 0){
                auxPos = Arrays.copy(pos, 1, pos.length-1);
                if (hijo != null){
                    return this.hijo[pos[0]].buscarNivel(auxPos, fechaIn, fechaFn, idInst, idO, opc, sockets);
                }else{
                    return null;
                }
            }else{
                if (hijo == null){
                    return buscarReporteEscf(fechaIn, fechaFn, idInst, idO, opc, sockets);
                }else{
                    mostrar = !mostrar;
                    return null;
                }
            }
        }
        
        
    public void guardarNivel(int [] pos, String[][] nivel, String fechaIn, String fechaFn, String idInst){
            int[] auxPos = new int[0];
            if (pos.length > 0){
                auxPos = Arrays.copy(pos, 1, pos.length-1);
                this.hijo[pos[0]].guardarNivel(auxPos, nivel, fechaIn, fechaFn, idInst);
            }else{
                guardarHijo(nivel);
                mostrar = true;
            }
        }
            
    }          
