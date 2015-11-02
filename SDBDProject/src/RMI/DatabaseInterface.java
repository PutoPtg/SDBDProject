/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.rmi.RemoteException;
import java.util.Calendar;

public interface DatabaseInterface extends java.rmi.Remote {
    
    public void adicionarMensagem(int id_proj, String mensagem,int id_user) throws RemoteException;
    public String consultarMsgs(int id_proj)throws RemoteException;
    public void responderMsgs(int id_proj,String mensagem,int id_admin, int id_user) throws RemoteException;
    //Votos    
    public String imprimeVotos(int id_proj) throws RemoteException;
    public void escolheVoto(int id_user, int id_proj, int index) throws RemoteException;
    //Users
    public String adicionarUser(String nome, String pass) throws RemoteException;
    public String login(String nome, String pass) throws RemoteException;
    public String consultarSaldo(String nome) throws RemoteException;
    public String[] projetosAdmin(String username) throws RemoteException;
    public String imprimeDoacoesUser(int id_user) throws RemoteException;
    public void consultarRecompensas(int id_user) throws RemoteException;
    public String doarDinheiro(String username, int id_proj, float valor) throws RemoteException;
    public void verProjetosContribui(int id_user) throws RemoteException;
    //Projectos
    public String [] verProjeto(int id_proj) throws RemoteException;
    public String criaProjeto(String username, String nome, String description, Calendar inicio, Calendar fim, float valor_objetivo) throws RemoteException;
    public String eliminaProjeto(String id_user, int id_proj) throws RemoteException;
    public String [] listaRecompensas(int projID) throws RemoteException; //ver recompensas projetos
    public String[] listaProjActuais() throws RemoteException;
    public String[] listaProjAntigos() throws RemoteException; 
    public String[] listaProjTodos() throws RemoteException;
  //Recompensas 
    public void escolherRecompensa(int id_user, int id_proj,int i, float dinheiro) throws RemoteException;
     public String removeRecompensa(String username, int projID, String nome) throws RemoteException;
     public String adicionarRecompensaProj(int id_proj, String nome, String desc, float valor) throws RemoteException;
//auxiliar
    public boolean log_check(long id) throws RemoteException;

    

   

    

    

    

    

    
    

    public final static String LOOKUPNAME = "DatabaseInterface";
}
