package com.atencion24.interfaz;

/*
 * saleForecast.java
 *
 * © <your company here>, 2003-2005
 * Confidential and proprietary.
 */

public class salaForecast {
        
        String nombreSala;
        int tam;        
        juegoForecast[] juegos;
        
        public salaForecast(){}
        
        public salaForecast(String nom, int tam) {  
            
            this.nombreSala = nom;
            juegos = new juegoForecast[tam];
            this.tam = 0;
            
        }
        
        public void agregarJuego(String nom, int tam){
            this.juegos[this.tam] = new juegoForecast(nom,tam);
            this.tam++;
        }
        
        public void agregarMesa(int j, String nom){
            this.juegos[j].agregarMesa(nom);
        }     
        
        public String obtenerNombre(){
            return nombreSala;
        }
        
        public String[] obtenerJuegos(){
            String [] resultado = new String[tam + 1];
            if (tam > 0)
                resultado[0] = "Todos";
            for (int i = 0; i < tam; i++){
             resultado[i + 1] = juegos[i].obtenerNombre();   
            }
            return resultado;
        }
        
        public String[] obtenerMesas(String Juego){
            for (int i = 0; i < tam; i++){
                if (juegos[i].obtenerNombre().equals(Juego))
                    return juegos[i].obtenerMesas();   
            }
            return new String[] {""};
        }
            
    }          
