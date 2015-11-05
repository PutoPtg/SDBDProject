/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class Recompensa implements Serializable {
    float valor;
    boolean entregue;
    Project proj;
    int id;
    String nom;
    public Recompensa(String nom,int id, Project proj,float valor, boolean entregue ) {
        this.nom=nom;
	this.valor = valor;
	this.id = id; //posiçao do array do projeto em que esta
        this.proj=proj;
        this.entregue=entregue;

    }
    public String imprimeR(){
        
	return ("Nome:"+this.nom+"\nValor: "+this.valor+"\nId: "+id+"\n");
	
    }
    
}
