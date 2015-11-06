package RMI;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

// BEGIN main
public class DatabaseImplementation extends UnicastRemoteObject implements DatabaseInterface {

    /**
     * Construct the object that implements the remote server. Called from main,
     * after it has the SecurityManager in place.
     */
    AtomicInteger contadorIdUsers;
    AtomicInteger contadorIdProj;
    AtomicInteger contadorIdRecomp;
    public static ArrayList<User> listaUtilizadores;
    public static ArrayList<Project> listaProjetos;
    public static long[] done_work;
    private static int count;

    /**
     *
     * @throws RemoteException
     */
    public DatabaseImplementation() throws RemoteException {

        super();	// sets up networking

        int i;
        count = 0; //exclusive of log_check;
        contadorIdUsers = new AtomicInteger(0);
        contadorIdProj = new AtomicInteger(0);
        contadorIdRecomp = new AtomicInteger(0);
        listaUtilizadores = new ArrayList<User>();
        listaProjetos = new ArrayList<Project>();
        done_work = new long[10];
        for (i = 0; i < 10; i++) {
            done_work[i] = 0;
        }
        Object temp;
        try {
            
        carregar();
       
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        /**
         * Test Area
         * 
         */
//        try{
//            
//        }catch (Exception e){
//            //Create user
//            
//            //Create projects
//            criaProjeto("Macaco", "Teste", "Teste", Calendar inicio, Calendar fim, ) throws RemoteException;
//   
//            //Create rewards
//            
//            
//        }
        
        
        
    }

    /**
     * Verifies if a ticket has been processed to avoid ticket repetitions in
     * case of server breakdown. Logs only a pile of the 10 previous tickets
     * id's, so it is not completely failsafe.
     *
     * @author Manuel
     * @param id ticket identification number
     * @return true if it is found done or false if it is the first time
     * commited.
     */
    @Override
    synchronized public boolean log_check(long id) {
        if (count < 10) {
            count++;
        } else {
            count = 0;
        }

        int i;
        for (i = 0; i < 10; i++) {
            if (done_work[i] == id) {
                done_work[count] = id;
                return true;
            }
        }
        done_work[count] = id;
        return false;

    }

    /**
     * Creates a new user by requiring username and password. Doesn't accept
     * duplicated usernames.
     *
     * @author Alexandra
     * @param nome
     * @param pass
     * @return
     */
    @Override
    synchronized public String adicionarUser(String nome, String pass) {

        for (int i = 0; i < listaUtilizadores.size(); i++) {
            if ((listaUtilizadores.get(i).nome.compareToIgnoreCase(nome) == 0)) {
                return "user_foud";
            }
        }
        float saldoInicial = 100;
        User utilizador = new User(nome, pass, saldoInicial);
        utilizador.id = contadorIdUsers.getAndIncrement();
        listaUtilizadores.add(utilizador);
        try {
            guardar();
	} catch (IOException e) {
            System.out.println("erro a guardar");
	}

        return "accepted_new_user";

    }

        // ver se dados de login sao validos
    /**
     * Authenticates users by username and password.
     *
     * @author Alexandra
     * @author Manuel
     * @param nome
     * @param pass
     * @return
     */
    @Override
    synchronized public String login(String nome, String pass) {

        for (int i = 0; i < listaUtilizadores.size(); i++) {
            if ((listaUtilizadores.get(i).nome.compareToIgnoreCase(nome) == 0) && (listaUtilizadores.get(i).pass.compareToIgnoreCase(pass) == 0)) {
                return "user_found";
            }
            if ((listaUtilizadores.get(i).nome.compareToIgnoreCase(nome) == 0) && (listaUtilizadores.get(i).pass.compareToIgnoreCase(pass) != 0)) {
                return "wrong_password";
            }
        }
        return "unknown_user";
    }

    /**
     * Returns present balance in user account
     *
     * @author Alexandra
     * @param nome
     * @return
     */
    @Override
    public String consultarSaldo(String nome) {
        User u = findUser(nome);
        return "Saldo: " + u.getSaldo() + "\n";
    }

    /**
     * Creates a Project
     * @author Alexandra
     * @param username
     * @param nome
     * @param description
     * @param inicio
     * @param fim
     * @param valor_objetivo
     * @return
     */
    @Override
    synchronized public String criaProjeto(String username, String nome, String description, Calendar inicio, Calendar fim, float valor_objetivo) {

        int id = contadorIdProj.getAndIncrement();
        User u = findUser(username);
        Project proj = new Project(u, nome, description, id, inicio, fim, valor_objetivo, 0);
        u.listaProjAdmin.add(proj);
        listaProjetos.add(proj);
        try {
            guardar();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
        return Integer.toString(id);
    }

    /**
     * Adds Rewards to projects
     *
     * @author Alexandra
     * @param id_proj
     * @param nome
     * @param valor
     */
    synchronized public String adicionarRecompensaProj(int id_proj, String nome, String desc, float valor) {
        //failsafe code:
        Project proj = procuraProjetoId(id_proj);
        
        if(proj == null){
            return "Project_not_found";
        }  
        
        int id = proj.listaRecompProj.size();
        Recompensa rec = new Recompensa(nome,id,proj, valor, false);
        proj.listaRecompProj.add(rec);
        
        try {
            guardar();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return proj.nome_proj;
    }

    /**
     * Deletes a reward given the username, project id and name of the reward
     * @param username
     * @param projID
     * @param nome
     */
    synchronized public String removeRecompensa(String username, int projID, String nome) {
		// e se for cancelada? users? dinheiro? saldo?
		
	Project proj = procuraProjetoId(projID); 
        User u = findUser(username);
	Recompensa r = acharRecompensa(projID, nome,"P");
        if(r == null){
            return ("reward not Found");
        }
	proj.listaRecompProj.remove(r);
        u.listaRecompUser.remove(r.id);
        
        
	try {
            guardar();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
        return ("acomplished");
		
    }
    
    /**
     * list projects from user
     * @Alexandra
     * @Manuel
     * @param username
     * @return
     */
    public String[] projetosAdmin(String username) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        User u=findUser(username);
        
        int size = u.listaProjAdmin.size();
        String reply []= new String [size+1];
       int j = 0;
        for (int i=0;i<u.listaProjAdmin.size();i++) {
            reply [j+1] =u.listaProjAdmin.get(i).id + " "+  
                    u.listaProjAdmin.get(i).nome_proj + " " +
                    formatter.format(u.listaProjAdmin.get(i).fim.getTime()) + " " +
                    u.listaProjAdmin.get(i).saldo + " " +
                    u.listaProjAdmin.get(i).saldo_objectivo;
            j++;
        }
         reply [0] = Integer.toString(j);
        return reply;
    }
    
    synchronized public String[] listaProjActuais() {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	int size = listaProjetos.size();
        int j = 0;
        String reply []= new String [size+1];
        
	for (int i=0;i<listaProjetos.size();i++) {
            if (listaProjetos.get(i).ver_prazo()==1)
               reply [j+1] = listaProjetos.get(i).id + " " +
                       listaProjetos.get(i).nome_proj +" " +
                       formatter.format(listaProjetos.get(i).fim.getTime()) + " " +
                       listaProjetos.get(i).saldo + " " + 
                       listaProjetos.get(i).saldo_objectivo;  
            j++;
        }
        reply [0] = Integer.toString(j);
        return reply;
          
    }
    
    synchronized public String[] listaProjAntigos() {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	int size = listaProjetos.size();
        String reply []= new String [size+1];
        reply [0] = Integer.toString(size);
	for (int i=0;i<listaProjetos.size();i++) {
            if (listaProjetos.get(i).ver_prazo()==0)
               reply [i+1] = listaProjetos.get(i).id + " " +
                       listaProjetos.get(i).nome_proj +" " +
                       formatter.format(listaProjetos.get(i).fim.getTime()) + " " +
                       listaProjetos.get(i).saldo + " " + 
                       listaProjetos.get(i).saldo_objectivo;              
        }
        return reply;
          
    }
    
    synchronized public String[] listaProjTodos() {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	int size = listaProjetos.size();
        String reply []= new String [size+1];
        reply [0] = Integer.toString(size);
	for (int i=0;i<listaProjetos.size();i++) {
            if (listaProjetos.get(i).ver_prazo()==0 || listaProjetos.get(i).ver_prazo()==1)
               reply [i+1] = listaProjetos.get(i).id + " " +
                       listaProjetos.get(i).nome_proj +" " +
                       formatter.format(listaProjetos.get(i).fim.getTime()) + " " +
                       listaProjetos.get(i).saldo + " " + 
                       listaProjetos.get(i).saldo_objectivo;              
        }
        return reply;
          
    }
    
    synchronized public void adicionarMensagem(int id_proj, String mensagem,int id_user) {
        
        Mensagem m = new Mensagem(id_user, id_proj, mensagem);
	ArrayList <Mensagem> me = new ArrayList <Mensagem>();
	Project proj = procuraProjetoId(id_proj);
	
	if (proj.inbox.get(id_user)!=null) {
            me = proj.inbox.get(id_user);
	}
	me.add(m);
	proj.inbox.put(id_user, me);
		
	try {
            guardar();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
		
    }
	// hashmap no projeto faz corresponder user a arraylist de mensagens.
	
    public String consultarMsgs(int id_proj) {
        Project proj = procuraProjetoId(id_proj);
        String str="";
        int id_user;
       
        ArrayList <Mensagem> msg;
        Set<Map.Entry<Integer, ArrayList<Mensagem>>> set = proj.inbox.entrySet();
        Iterator<Map.Entry<Integer, ArrayList<Mensagem>>> i= set.iterator();
        str=str.concat("Mensagens:\n");
        while (i.hasNext()) {
            Map.Entry <Integer, ArrayList<Mensagem>> mentry = (Map.Entry<Integer, ArrayList<Mensagem>>) i.next();
            id_user = (int) mentry.getKey();
            msg = (ArrayList<Mensagem>) mentry.getValue();
			
            str = str.concat("/"+procuraUserId(id_user).nome+"/[id="+procuraUserId(id_user).id+"]\n");
            for (int j=0;j<msg.size();j++) { 
                
                if (msg.get(j).id_user==proj.admin.id) {
					
                    str=str.concat("A: ");
                }
                str = str.concat(msg.get(j).coment+"\n");
            }
			
        }
        return str;
    }

	
    synchronized public void responderMsgs(int id_proj,String mensagem,int id_admin, int id_user) {
	Project proj = procuraProjetoId(id_proj);
	Mensagem msg = new Mensagem( id_proj,id_admin, mensagem);
	ArrayList <Mensagem> msgs = proj.inbox.get(id_user);
	msgs.add(msg);
	proj.inbox.put(id_user, msgs);
		
	try {
            guardar();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
    }
    
    //*****************+VOTOS***************//
        
    public String imprimeVotos(int id_proj) {
        Project p = procuraProjetoId(id_proj);
	String str="";
		
	for (int i=0;i<p.votos.size();i++) {
            str=""+(p.votos.get(i).imprime(i));
	}
	return str;
    }
	
    synchronized public void escolheVoto(int id_user, int id_proj, int index) {
	Project p = procuraProjetoId(id_proj);
	p.votos.get(index).utilizadores.add(id_user);
	p.votos.get(index).contador+=1;
    }
   
    
    
    
    
     public void projetosAdmin(int id_user) {
	String str="";
        User u=procuraUserId(id_user);
		
        for (int i=0;i<u.listaProjAdmin.size();i++) {
			
            u.listaProjAdmin.get(i).toString();
			
        }
    
     }
    
     
     public String imprimeDoacoesUser(int id_user) {
	String str="";
	User user = procuraUserId(id_user); 
	int idP;
       
	float doacao;
        
	for(int i=0;i<user.doacoesUser.size();i++){
            idP=(int)user.doacoesUser.get(i).id;
             Project proj = procuraProjetoId(idP);
            doacao=(float)user.doacoesUser.get(i).investido;
            str=str.concat("Projeto: "+proj.nome_proj+"\nDoacao: "+doacao+"\n");
        }
            		
        return str;
    }
    
    public void consultarRecompensas(int id_user) {
	User user = procuraUserId(id_user); 

	for (int i=0;i<user.listaRecompUser.size();i++) {
            procuraIdRecomp(user.listaRecompUser.get(i)).imprimeR();
			
	}
		
		
    }
    
    public String eliminaProjeto(String username, int id_proj) {
	//dinheiro volta para lista de backers
        User user= findUser(username);
        Project proj= procuraProjetoId(id_proj);
        if (proj == null){
            return ("error");
        }
        devolveTudo(proj);
	user.listaProjAdmin.remove(proj.id); 
	listaProjetos.remove(proj);
        try {
            guardar();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
        return ("done");
    }
    public void devolveTudo(Project proj) {
	
	int userid;
	float doacao;
	for (int i=0;i<proj.listaDoacoes.size();i++){
            userid=proj.listaDoacoes.get(i).id;
            User u=procuraUserId(userid);
            doacao=proj.listaDoacoes.get(i).investido;
            proj.saldo-=doacao;
            u.saldo+=doacao;
            retiraRecompensa(u, proj);
            for(int j=0;j<u.doacoesUser.size();j++){
                if(u.doacoesUser.get(j).id==proj.id)
                    u.doacoesUser.remove(j);
            }
        }
        
        try {
            guardar();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
		
    }
	
    synchronized public void retiraRecompensa(User u, Project proj) {
		
        for (int i=0;i<u.listaRecompUser.size();i++) {
            
            if (procuraIdRecomp(u.listaRecompUser.get(i)).proj == proj) {
		u.listaRecompUser.remove(i);
            }			
			
	}
		
	try {
            guardar();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
		
    }
	
    synchronized public Recompensa procuraIdRecomp(int id){
        for(int i=0;i<listaProjetos.size();i++){
            for(int j=0;j<listaProjetos.get(i).listaRecompProj.size();j++){
                if(listaProjetos.get(i).listaRecompProj.get(j).id==id){
                    return listaProjetos.get(i).listaRecompProj.get(j);
                }
                
            }
        }
        return null;
        
    }
    
    //lista recompensas projetos
    public String [] listaRecompensas(int projID) {
	
	String str = "";
	Project p = procuraProjetoId(projID);
	String array[] = new String [p.listaRecompProj.size()+1];
        for (int i=0;i<p.listaRecompProj.size();i++) {
            array [i+1] = p.listaRecompProj.get(i).imprimeR();
	}
        array[0] = Integer.toString(p.listaRecompProj.size());
	return array;
    }
    
    synchronized public String doarDinheiro(String username, int id_proj, float valor) {
	User user = findUser(username);
	
	Project proj = procuraProjetoId(id_proj);
		
	float saldo = user.getSaldo();
        if(saldo>=valor){
            Doacoes d =new Doacoes(valor,user.id);
            Doacoes dp =new Doacoes(valor,id_proj);
            
            proj.listaDoacoes.add(d);
            user.doacoesUser.add(dp);
            System.out.println("Doar ao projeto "+proj.nome_proj);
		
	    			
            proj.saldo+=valor;
            user.saldo-=valor;
		
            return ("O seu saldo é agora de:"+user.getSaldo()+" uma vez que doou"+valor);
		
		
        }
        else{
            return ("User tem saldo insuficiente");
        }
            		
							
		
    }
    
    synchronized public void carregar() throws IOException, ClassNotFoundException {
        //onde vai ficar BD , esta implementado com objectos
        FileInputStream r = new FileInputStream("ficheiros/users.txt");
        ObjectInputStream obj_r = new ObjectInputStream(r);
        listaUtilizadores = (ArrayList) obj_r.readObject();
        contadorIdRecomp.set((Integer) obj_r.readObject());
        contadorIdProj.set((Integer) obj_r.readObject());
        contadorIdUsers.set((Integer) obj_r.readObject());
        obj_r.close();
		
        r = new FileInputStream("ficheiros/ideias.txt");
        obj_r = new ObjectInputStream(r);
        //começar atomic no ultimo definido para ids serem sempre diferentes
        listaProjetos = (ArrayList) obj_r.readObject();
        
       
       
        obj_r.close();
    }
	
    // guarda dados em ficheiro 
    synchronized public void  guardar() throws IOException {
		
        FileOutputStream w = new FileOutputStream("ficheiros/ideias.txt");
        ObjectOutputStream obj_w = new ObjectOutputStream(w);
        obj_w.writeObject(listaProjetos); // escrever arrayList
        obj_w.close();
        
        w = new FileOutputStream("ficheiros/users.txt");
        obj_w = new ObjectOutputStream(w);

        obj_w.writeObject(listaUtilizadores);
        //guardar atomics de ids para ids serem sempre diferentes
        obj_w.writeObject(contadorIdRecomp.getAndIncrement());
        obj_w.writeObject(contadorIdProj.getAndIncrement());
        obj_w.writeObject(contadorIdUsers.getAndIncrement());
        
        obj_w.close();
		
    }
    
    
    //********************* RECOMPENSAS ********************************//
    synchronized public void escolherRecompensa(int id_user, int id_proj,int i, float dinheiro) {
	
	Project proj = procuraProjetoId(id_proj);
        User u=procuraUserId(id_user);
	Recompensa r= proj.listaRecompProj.get(i);
		
	if (dinheiro >= r.valor) 
            u.listaRecompUser.add(r.id);
			
        try {
            guardar();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
			
	    
	
		
    }
	
        
    //ver projectos que contribui
    synchronized public void verProjetosContribui(int id_user) {
		
        String s="";
        User u= procuraUserId(id_user);
        for (int i=0;i<u.doacoesUser.size();i++) {
            for (int j=0;j<listaProjetos.size();j++) {
                if (listaProjetos.get(j).id == u.doacoesUser.get(i).id){
                    s =""+listaProjetos.get(j).toString();
                    System.out.println(s);
                }
                System.out.println("Investido:"+u.doacoesUser.get(i).investido);
                
                        
            }
	}
    }
    
    synchronized public String [] verProjeto(int id_proj) {
        String ne [] = new String [1];
        ne [0] = "unknown";
        
        if(procuraProjetoId(id_proj)==null){
            return ne;
        }else return listaProjetos.get(id_proj).rosto();
    }
    

  //----------Auxiliar Methods-----------------------------
    /**
     * Finds user objecs by key username
     *
     * @author Manuel
     * @author Alexandra
     * @param username
     * @return
     */
    synchronized public User findUser(String username) {
        for (int i = 0; i < listaUtilizadores.size(); i++) {
            if (listaUtilizadores.get(i).nome.equalsIgnoreCase(username)) {
                return listaUtilizadores.get(i);
            }
        }
        return null;
    }

    /**
     * Finds projects by given id
     *
     * @author Alexandra
     * @param id_proj
     * @return
     */
    synchronized public Project procuraProjetoId(int id_proj) {
        int i;
        //System.out.println("O tamanho da lista de Projetos e "+listaProjetos.size());
        for (i = 0; i < listaProjetos.size(); i++) {
            if (listaProjetos.get(i).id == id_proj) {
                return listaProjetos.get(i);
            }
        }
        return null;
    }
    //-------deprecated-----

    /**
     * Finds user objecs by key id_user
     *
     * @author Alexandra
     * @param id_user
     * @return
     */
    synchronized public User procuraUserId(int id_user) {

        for (int i = 0; i < listaUtilizadores.size(); i++) {
            if (listaUtilizadores.get(i).id == id_user) {
                return listaUtilizadores.get(i);
            }
        }
        return null;
    }

    
    
    /**
     * Finds reward
     * @author Alexandra
     * @param id_proj
     * @param nome
     * @param onde
     * @return
     */
    synchronized public Recompensa acharRecompensa(int id_proj,String nome, String onde){
        if("P".equals(onde)){ //classe de projectos
            Project proj = procuraProjetoId(id_proj);
            for (int i=0;i<proj.listaRecompProj.size();i++) {
		if (proj.listaRecompProj.get(i).nom.compareToIgnoreCase(nome)==0) {
                    return proj.listaRecompProj.get(i);
		}
	    }
		
        }
        else if("U".equals(onde)){ //classe de users 
            
        }
        return null;  
        
    }

    
    synchronized public void removeRecompensa(int userID, int projID, String nome) {
		// e se for cancelada? users? dinheiro? saldo?
		
	Project proj = procuraProjetoId(projID); 
        User u=procuraUserId(userID);
	Recompensa r = acharRecompensa(projID, nome,"P");
	proj.listaRecompProj.remove(r);
        u.listaRecompUser.remove(r.id);
        
        
		
	try {
            guardar();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
		
    }
    
    
    //***********************VALIDADE*******************//
    public void actualizar(){
        for(int i=0;i<listaProjetos.size();i++){
            if (listaProjetos.get(i).ver_prazo()==0) { //terminou prazo
                update(listaProjetos.get(i));
             }
        }
    }
        
    public void update(Project proj) {
	
	if (proj.saldo >= proj.saldo_objectivo) { 
            proj.admin.saldo+=proj.saldo;
            entregarRecomp(proj);
	}
	else { 
            devolveTudo(proj);
	}
		
	try {
            guardar();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
    }

    
    public void entregarRecomp(Project proj) {
	for (int i=0;i<proj.listaRecompProj.size();i++) { 
            proj.listaRecompProj.get(i).entregue=true;
            
	}
        
    }
}
