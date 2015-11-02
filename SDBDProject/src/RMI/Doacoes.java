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
public class Doacoes implements Serializable{
    float investido;
    
    int id;
    
    //id nas listas dos projetos vai ser do user e nas listas do user vai ser do projeto 
    public Doacoes(float investido, int id) {
	this.investido = investido;
	this.id=id;
	
    }
    
    
}
