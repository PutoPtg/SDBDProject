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
public class User implements Serializable{
    float saldo;
    String nome,pass;
    int id;
   
    public static ArrayList <Doacoes> doacoesUser= new ArrayList <Doacoes>();
    public static ArrayList <Integer> listaRecompUser= new ArrayList <Integer>(); //modificar para ids das recomp que estao nos projetos
    public static ArrayList <Project> listaProjAdmin= new ArrayList <Project>();
    
    public User(String nome, String pass, float saldo) {
	this.nome = nome;
	this.pass = pass;
	this.saldo=saldo;
    }
    
    public float getSaldo(){
        return saldo;
    }
    
    public void setId(int id){
        this.id=id;
    }
    
}
