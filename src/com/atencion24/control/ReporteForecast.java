package com.atencion24.control;

/*
 * MasterGamingReport.java
 *
 * © <your company here>, 2003-2005
 * Confidential and proprietary.
 */

import net.rim.device.api.util.Arrays;

import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.InformacionNivel;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.salaForecast;

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
        
            this.info[0]  = new InformacionNivel("Efectivo",datos[0], nvl, new int[] {0});
            this.info[1]  = new InformacionNivel("Match Play",datos[1], nvl, new int[] {1});
            this.info[2]  = new InformacionNivel("Cheque",datos[2], nvl, new int[] {2});
            this.info[3]  = new InformacionNivel("T/Débito",datos[3], nvl, new int[] {3});
            this.info[4]  = new InformacionNivel("T/Crédito",datos[4], nvl, new int[] {4});
            this.info[5]  = new InformacionNivel("Divisa",datos[5], nvl, new int[] {5});
            this.info[6]  = new InformacionNivel("Giro",datos[6], nvl, new int[] {6});
            this.info[7]  = new InformacionNivel("Coin In",datos[7], nvl, new int[] {7});
            this.info[8]  = new InformacionNivel("Drop",datos[8], nvl, new int[] {8});
            this.info[9]  = new InformacionNivel("Cierre",datos[9], nvl, new int[] {9});
            this.info[10] = new InformacionNivel("Apertura",datos[10], nvl, new int[] {10});
            this.info[11] = new InformacionNivel("Devolución",datos[11], nvl, new int[] {11});
            this.info[12] = new InformacionNivel("Relleno",datos[12], nvl, new int[] {12});
            this.info[13] = new InformacionNivel("PRG Mesa",datos[13], nvl, new int[] {13});
            this.info[14] = new InformacionNivel("WL",datos[14], nvl, new int[] {14});
            this.info[15] = new InformacionNivel("% W/L",datos[15], nvl, new int[] {15});
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
            
            int [] auxPos = Arrays.copy(pos, 1, pos.length-1);
            System.out.println("Tamano arreglo aux "+ auxPos.length);
            String p = this.info[pos[0]].buscarNivel( auxPos,  fechaIn,  tipo,  idInst, pos[0], 12, sockets);
            return p;
        } 
        
        public void guardarNivel(int [] pos, String[][] nivel, String fechaIn, String fechaFn, String idInst){
            
            this.info[pos[0]].guardarNivel( nivel);
            
        }
       
    }               
