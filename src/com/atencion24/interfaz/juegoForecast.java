
/*
 * juegoForecast.java
 *
 * © <your company here>, 2003-2005
 * Confidential and proprietary.
 */


package com.atencion24.interfaz;
public class juegoForecast {
        
        String nombreJuego;
        int tam;
        mesaForecast[] mesas;
        
        public juegoForecast(){}
        
        public juegoForecast(String nom, int tam) {  
            
            this.nombreJuego = nom;
            this.mesas = new mesaForecast[tam];
            this.tam = 0;
            
        }
        
        public void agregarMesa(String nom){
            this.mesas[this.tam] = new mesaForecast(nom);
            this.tam++;
        }
        
        public String obtenerNombre(){
            return nombreJuego;
        }
        
        
        public String[] obtenerMesas(){
            String [] resultado = new String[tam + 1];
            if (tam > 0)
                resultado[0] = "Todas";
            for (int i = 0; i < tam; i++){
                resultado[i + 1] = mesas[i].obtenerNombre();   
            }
            return resultado;
        }
        
            
    }      
