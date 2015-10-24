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
package WikiStarter;

/**
 * @author Manuel Madeira Amado
 * @author Alexandra Leandro
 * @author Inês Fidalgo
 */
import java.net.*;
import java.io.*;

public class Main_Client {

    static ObjectInputStream in = null;
    static ObjectOutputStream out = null;
    static Socket s;

    public static void main(String args[]) {
        connect();
        menu_splash();
    }

    private static void connect() {

        try {
            s = new Socket("127.0.0.1", 6000);
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Menu de arranque do sistema
     *  by Manuel
     */
    private static void menu_splash() {

        int o;
        Get_Option op = new Get_Option(2);

        System.out.println("Wiki Starter");
        System.out.println("");
        System.out.println("1 - Login");
        System.out.println("2 - Register");
        System.out.println("0 - Quit");

        o = op.get_option();

        switch (o) {
            case 1:
                menu_login();
            case 2:
                menu_register();
            case 0:
                menu_exit();
        }

        //return o;
    }

    private static void menu_login() {

        String username;
        String password;
        Get_String str = new Get_String();

        System.out.println("Login");
        System.out.println("Username:");
        username = str.get_string("Username");
        System.out.println("Password:");
        password = str.get_string("Password");

        Message msg = new Message(username, password, "login");
        msg.set_request("Verify Login");
        
        try{
        out.writeObject(msg);
        msg= (Message)in.readObject();
        }catch (Exception e) {System.out.println(e); }
        
        if(msg.get_answer_boolean()== true){
        
        System.out.println("true");
        }else{
        System.out.println("false");    
        }
    }

    private static void menu_register() {

    }

    private static void menu_welcome() {

        int o;
        boolean active;
        Get_Option op = new Get_Option(2);

        System.out.println("Wiki Starter");
        System.out.println("Welcome");
        System.out.println("");
        System.out.println("1 - List Open Projects");
        System.out.println("2 - List Old Projects");
        System.out.println("3 - Personal Info");
        System.out.println("0 - Quit");

        o = op.get_option();

        switch (o) {
            case 1:
                menu_projects(active = true);
            case 2:
                menu_projects(active = false);
            case 3:
                menu_userInfo();
            case 0:
                menu_exit();
        }
    }

    private static void menu_exit() {

    }

    private static void menu_projects(boolean active) {

    }

    private static void menu_userInfo() {

    }

}
