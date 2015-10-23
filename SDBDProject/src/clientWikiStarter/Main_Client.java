/*
 * [PT]
 * Código de cliente para ligação ao servidor
 * Projecto de Sistemas Distribuídos e Bases de Dados 
 * Contém código dado pelo professor
 * 2015
 * Universidade de Coimbra
 * 
 * [EN]
 * Client side code to connect to server
 * Academic project to Distributed Systems and Database
 * Contains code given by the teacher
 * 2015
 * University of Coimbra
 */

package clientWikiStarter;

/**
 * @author Manuel Madeira Amado
 * @author Alexandra Leandro
 * @author Inês Fidalgo
 */

import java.net.*;
import java.io.*;

public class Main_Client {

    public static void main(String args[]) {
        
        
        
	// args[0] <- hostname of destination
//	if (args.length == 0) {
//	    System.out.println("java TCPClient hostname");
//	    System.exit(0);
//	}

	Socket s = null;
	int serversocket = 6000;
	try {
	    // 1o passo

	    s = new Socket("0.0.0.0", serversocket);

	    System.out.println("SOCKET=" + s);
	    // 2o passo
	    DataInputStream in = new DataInputStream(s.getInputStream());
	    DataOutputStream out = new DataOutputStream(s.getOutputStream());

//	    String texto = "";
//	    InputStreamReader input = new InputStreamReader(System.in);
//	    BufferedReader reader = new BufferedReader(input);
//	    System.out.println("Introduza texto:");

	    // 3o passo
	    while (true) {
//		// READ STRING FROM KEYBOARD
//		try {
//		    texto = reader.readLine();
//		} catch (Exception e) {
//		}
            
           // int opt = menu_login();
            

		// WRITE INTO THE SOCKET
	//	out.writeUTF(Integer.toString(opt)); //texto

		// READ FROM SOCKET
		String data = in.readUTF();

		// DISPLAY WHAT WAS READ
		System.out.println("Received: " + data);
	    }

	} catch (UnknownHostException e) {
	    System.out.println("Sock:" + e.getMessage());
	} catch (EOFException e) {
	    System.out.println("EOF:" + e.getMessage());
	} catch (IOException e) {
	    System.out.println("IO:" + e.getMessage());
	} finally {
	    if (s != null)
		try {
		    s.close();
		} catch (IOException e) {
		    System.out.println("close:" + e.getMessage());
		}
	}
    }

    private static int connection(){
        
        return 0;
    }
    
    
    
    /* Menu de arranque do sistema
    *  by Manuel
    */
    private static void menu_splash(){
        
        int o;
        Get_Option op = new Get_Option(2);
        
        System.out.println("Wiki Starter");
        System.out.println("");
        System.out.println("1 - Login");
        System.out.println("2 - Register");
        System.out.println("0 - Quit");
        
        o = op.get_option();
        
        switch (o) {
            case 1: menu_login();
            case 2: menu_register();
            case 0: menu_exit();
        } 
        
        
        //return o;
        
    }
    
    private static void menu_login(){
        
        
        
    }
        
    private static void menu_register(){
        
    }
    
    private static void menu_exit(){
        
    }
    
}
