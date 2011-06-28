package com.atencion24.control;

/*
 * MasterGamingReport.java
 *
 * © <your company here>, 2003-2005
 * Confidential and proprietary.
 */

import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.InformacionNivel;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.salaForecast;

import net.rim.device.api.util.Arrays;


/**
 * 
 */
public class ReporteForecast {
        
        ListStyleButtonSet contentCasino   = new ListStyleButtonSet();
        salaForecast[] salas;
        CustomButtonTable[] botones;
        int tam;
        
        //Valores del reporte raiz
        InformacionNivel[] info = new InformacionNivel[16];
        
        public ReporteForecast(){
          }
        
        public ReporteForecast(int tam) {  
            
            this.salas = new salaForecast[tam];
            this.tam = 0;
            
        }
        
        public void agregarSala(String nomSala, int tamSala){
            this.salas[this.tam] = new salaForecast(nomSala, tamSala);
            this.tam++;
        }
        
        public ListStyleButtonSet enviar(){
            return this.contentCasino;
        }
        
        
        public void agregarJuego(int i, String nom, int tamJuego){
            this.salas[i].agregarJuego(nom, tamJuego);
        }
        
        public void agregarMesa(int i, int j, String nom){
            this.salas[i].agregarMesa(j, nom);
        }
        
        
        public String[] nombreSalas(){
            String [] resultado = new String[tam + 1];
            if (tam > 0)
                resultado[0] = "Todas";
            for (int i = 0; i < tam; i++){
                resultado[i + 1] = salas[i].obtenerNombre();   
            }
            return resultado;
        }
        
        public String[] nombreJuegos(String Sala){
            for (int i = 0; i < tam; i++){
                if (salas[i].obtenerNombre().equals(Sala))
                    return salas[i].obtenerJuegos();   
            }
            return new String[] {""};
        }
        
        public String[] nombreMesas(String Sala, String Juego){
            for (int i = 0; i < tam; i++){
                if (salas[i].obtenerNombre().equals(Sala))
                    return salas[i].obtenerMesas(Juego);   
            }
            return new String[] {""};
        }
        
        public void valoresReporte(String[] datos, int nvl, String salaEnv, String juegoEnv, String mesaEnv){
        
            this.info[0]  = new InformacionNivel("EFECTIVO_ORIG","Efectivo",datos[0], nvl, new int[] {0}, salaEnv, juegoEnv, mesaEnv);
            this.info[1]  = new InformacionNivel("MATCH_PLAY","Match Play",datos[1], nvl, new int[] {1}, salaEnv, juegoEnv, mesaEnv);
            this.info[2]  = new InformacionNivel("CHEQUE_ORIG","Cheque",datos[2], nvl, new int[] {2}, salaEnv, juegoEnv, mesaEnv);
            this.info[3]  = new InformacionNivel("TD","T/Débito",datos[3], nvl, new int[] {3}, salaEnv, juegoEnv, mesaEnv);
            this.info[4]  = new InformacionNivel("TC","T/Crédito",datos[4], nvl, new int[] {4}, salaEnv, juegoEnv, mesaEnv);
            this.info[5]  = new InformacionNivel("USD","Divisa",datos[5], nvl, new int[] {5}, salaEnv, juegoEnv, mesaEnv);
            this.info[6]  = new InformacionNivel("GIROS","Giro",datos[6], nvl, new int[] {6}, salaEnv, juegoEnv, mesaEnv);
            this.info[7]  = new InformacionNivel("M_COIN_IN","Coin In",datos[7], nvl, new int[] {7}, salaEnv, juegoEnv, mesaEnv);
            this.info[8]  = new InformacionNivel("DROPP","Drop",datos[8], nvl, new int[] {8}, salaEnv, juegoEnv, mesaEnv);
            this.info[9]  = new InformacionNivel("CIERRE","Cierre",datos[9], nvl, new int[] {9}, salaEnv, juegoEnv, mesaEnv);
            this.info[10] = new InformacionNivel("APERTURA","Apertura",datos[10], nvl, new int[] {10}, salaEnv, juegoEnv, mesaEnv);
            this.info[11] = new InformacionNivel("DEVOLUCION","Devolución",datos[11], nvl, new int[] {11}, salaEnv, juegoEnv, mesaEnv);
            this.info[12] = new InformacionNivel("RELLENO","Relleno",datos[12], nvl, new int[] {12}, salaEnv, juegoEnv, mesaEnv);
            this.info[13] = new InformacionNivel("PRG_MESA","PRG Mesa",datos[13], nvl, new int[] {13}, salaEnv, juegoEnv, mesaEnv);
            this.info[14] = new InformacionNivel("WIN_LOSS","WL",datos[14], nvl, new int[] {14}, salaEnv, juegoEnv, mesaEnv);
            this.info[15] = new InformacionNivel("PC_WIN_LOSS","% W/L",datos[15], nvl, new int[] {15}, salaEnv, juegoEnv, mesaEnv);
        }
        
        public CustomButtonTable[]  mostrarBotones(){
            
            CustomButtonTable[] aux1, aux2;
            
            aux1 = this.info[0].mostrarBotones();
            aux2 = this.info[1].mostrarBotones();
            aux1 = this.info[0].mezclarArray(aux1,aux2);
            
            for (int i = 2; i < 16; i++){
                aux2 = this.info[i].mostrarBotones();
                aux1 = this.info[0].mezclarArray(aux1,aux2);
            }
            return aux1;
        }
        
        public String desplegarBotones(int [] pos, String fechaIn, String tipo, String idInst, boolean sockets){
            
            for (int i = 0; i < pos.length; i++)
                System.out.println(pos[i]);
            if(pos.length > 1)
                System.out.println("HOLA");
            int [] auxPos = Arrays.copy(pos, 1, pos.length-1);
            String p = this.info[pos[0]].buscarNivel( auxPos,  fechaIn,  tipo,  idInst, pos[0], 12, sockets);
            if (p == null)
                System.out.println("hola");
            return p;
        }
        
        public void guardarNivel(int [] pos, String[][] nivel, String fechaIn, String fechaFn, String idInst){
            
            int [] auxPos = Arrays.copy(pos, 1, pos.length-1);
            this.info[pos[0]].guardarNivel( auxPos, nivel,  fechaIn,  fechaFn,  idInst);
            
        }
        
    }               
