/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Voto implements Serializable {
    String nome;
    int contador=0;
    ArrayList <Integer> utilizadores = new ArrayList <Integer>();
	
    public Voto(String nome) {
	this.nome = nome;
    }
	
    public String imprime(int i) {
	return i+"-"+nome+"\n";
    }
	
}

