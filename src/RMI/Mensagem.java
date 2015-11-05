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
public class Mensagem implements Serializable{
    int id_proj;
    int id_user;
    String coment;
	
    public Mensagem(int id_proj, int id_user, String mensagem) {
	this.id_user=id_user;
	this.id_proj=id_proj;
	this.coment=mensagem;
    }
}
