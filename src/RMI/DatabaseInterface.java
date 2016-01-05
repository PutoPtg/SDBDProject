/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.rmi.RemoteException;
import java.sql.SQLException;

public interface DatabaseInterface extends java.rmi.Remote {
    
public String updateVAL() throws RemoteException, SQLException;
public String verificarValidade() throws RemoteException, SQLException;
public String addUser(String uname, String password) throws RemoteException;
public String login(String uname, String pword) throws RemoteException;
public String depositarSaldo(String user, float valor) throws RemoteException, SQLException;
public String consultarSaldo(String uname) throws RemoteException;
public String projetosAdmin(String admin) throws RemoteException;
public String recompensasUser(String user) throws RemoteException;
public String criarProjeto(String nome, String username, String descricao, String inicio, String fim, float pretendido) throws RemoteException;
public String cancelarProjeto(String uname, int proj_id) throws RemoteException, SQLException;
public String atualizar(String uname, int proj_id, int cumprir) throws RemoteException, SQLException;
public String adicionarRecompensas(int idP, String nome, float valor) throws RemoteException;
public String adicionarVotos(int idP, String nome) throws RemoteException;
public String mostrarRecompensasProj(int id) throws RemoteException;
public String mostrarVotosProj(int id) throws RemoteException;
public String listaProjetosActuais() throws RemoteException;
public String listaProjetosAntigos() throws RemoteException;
public String listaProjTodos() throws RemoteException;
public String doar(String user, int proj, float doacao, String voto) throws RemoteException, SQLException;
public String eliminarDoacao(String user, int idP) throws RemoteException, SQLException;
public String mostrarMensagens(int idPro) throws RemoteException;
public String mandarMsg(String user, int idP, String mensagem) throws RemoteException, SQLException;
public String responderMensagens(String user, int idP, int idM, String resposta) throws RemoteException, SQLException;
public boolean log_check(long msgid) throws RemoteException;   
public String openProject(String id)throws RemoteException, SQLException;

    public final static String LOOKUPNAME = "DatabaseInterface";
}
