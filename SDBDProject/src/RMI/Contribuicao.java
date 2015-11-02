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
public class Contribuicao implements Serializable{
    long investido;
    Project proj;
    Recompensa recompensa;
    public Contribuicao(long investido, Project proj, Recompensa recompensa) {
	this.investido = investido;
	this.proj = proj;
	this.recompensa=recompensa;
    }
    
}
