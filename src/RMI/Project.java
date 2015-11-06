/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class Project implements Serializable{
    float saldo;
    float saldo_objectivo;
    String nome_proj;
    String description;
    int id;
    Calendar inicio,fim;
    User admin;
    boolean deleted;
    public static ArrayList <Doacoes> listaDoacoes= new ArrayList <Doacoes>(); //list de qt e o id do user que doou
    public static ArrayList <Recompensa> listaRecompProj= new ArrayList <Recompensa>(); //lista de recompensas deste projeto
    ArrayList <Mensagem> mensagens = new ArrayList <Mensagem>();
    HashMap <Integer, ArrayList<Mensagem>> inbox = new HashMap <Integer, ArrayList<Mensagem>>();
    ArrayList <Voto> votos = new ArrayList <Voto>();
    public Project (User admin, String nome_proj,String decrp, int id,Calendar inicio, Calendar fim, float saldo_objectivo,float saldo ) {
	this.admin= admin;
	this.nome_proj= nome_proj;
        this.description = decrp;
	this.id=id;
        this.inicio=inicio;
        this.fim=fim;
        this.saldo_objectivo=saldo_objectivo;
        this.saldo=saldo;
        this.deleted = false;
    }
    public String [] rosto (){
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
       String array [] = new String [6];
       array[0] = Integer.toHexString(id);
       array[1] = nome_proj;
       array[2] = description;
       array[3] = admin.nome;
       array[4] = formatter.format(fim.getTime());
       array[5] = Float.toString(saldo);
       array[5] = Float.toString(saldo_objectivo);
       
       return array;
    }
    
    public int ver_prazo() {
        Calendar hoje= new GregorianCalendar();
        if(fim.compareTo(hoje)>=0)
            return 1;
        else
            return 0;
    }
    
    public void ToString(){
        if(this.ver_prazo()==1)
            System.out.println("Nome:"+nome_proj+"\nid:"+id+"\nData inicio:"+inicio+"\nData de fim:"+fim+"\nObjectivo(euros):"+saldo_objectivo+"\nPresente saldo:"+saldo);
        else{
             System.out.println("Já passou o prazo!!");
             System.out.println("Nome:"+nome_proj+"\nid:"+id+"\nData inicio:"+inicio+"\nData de fim:"+fim+"\nObjectivo(euros):"+saldo_objectivo+"\nSaldo atingido:"+saldo);
        }
        System.out.println("Recompensas:");
        for(int i=0;i<listaRecompProj.size();i++){
            System.out.print(i+"-");
            listaRecompProj.get(i).imprimeR();
        }
            
    }
    
    
	
    public float getSaldo() {
	return saldo;
    }
    
    public void actualizarSaldo(float i){
        saldo+=i;
            
    }

	
    public String imprimeRecompProj(Project proj) {
	//...
        String st="";
        return st;
    }
	
    public String imprimePrazo(Project proj) {
		
	String str=""+ proj.fim.get(Calendar.DAY_OF_MONTH)+"."+proj.fim.get(Calendar.MONTH)+"."+proj.fim.get(Calendar.YEAR);
	return str;
    }
	
	
	
    public void addRecompensa(Project proj, Recompensa r) {
	proj.listaRecompProj.add(r);
    }
                    
 }
    

