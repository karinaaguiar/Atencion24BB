/*
 * mesaForecast.java
 *
 * © <your company here>, 2003-2005
 * Confidential and proprietary.
 */
package com.atencion24.interfaz;
class mesaForecast {
       
        String nombre;
        
        public mesaForecast() {    }
        
        public mesaForecast(String nom) {  
            
            this.nombre = nom;
            
        }
        
        public String obtenerNombre(){
            return nombre;
        }
} 
