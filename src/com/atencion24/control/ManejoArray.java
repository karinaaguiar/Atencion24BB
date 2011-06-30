package com.atencion24.control;

import com.atencion24.interfaz.ListStyleButtonField;

public class ManejoArray {

	public ManejoArray (){}
	
	public ListStyleButtonField[] mezclarArray(ListStyleButtonField[] botones, ListStyleButtonField[] aux){
        
		ListStyleButtonField[] c = new ListStyleButtonField[ botones.length + aux.length ];
        int tam = 0;
        for(int i = 0; i < botones.length; i++){
            c[tam] = botones[i];
            tam++;  
        }
        for(int j = 0; j < aux.length; j++){
            c[tam] = aux[j];
            tam++; 
        }
        return c;
    }
}
