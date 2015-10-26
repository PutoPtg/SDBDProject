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

public class Main_Client extends Thread {

    static ObjectInputStream in = null;
    static ObjectOutputStream out = null;
    static Socket s;

    static String IP1 = "0.0.0.0";
    static String IP2 = "192.158.56.13";

    static String main_ip = IP1;

    public static void main(String args[]) {
        connect();
        menu_splash();
    }

    private static void connect() {

        int counter = 0;
        boolean connected = false;
        boolean first_round = true;

        while (!connected && counter < 4) {
            counter++;
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("Socket Closing error: " + e.getMessage());
                }
            }

            s = null;
            try {

                Thread.sleep(2000);
                s = new Socket(main_ip, 6000);
                out = new ObjectOutputStream(s.getOutputStream());
                in = new ObjectInputStream(s.getInputStream());
                counter = 0;
                connected = true;
            } catch (UnknownHostException e) {
                System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO:" + e.getMessage());
            } catch (InterruptedException e) {
                //System.out.println("IO:" + e.getMessage());
            }
        }

        if (!connected && first_round == false) {
            System.out.println("Erro de connecção");
            System.exit(-1);
        } else {
            first_round = false;
            counter = 0;
            if (main_ip == IP1) {
                main_ip = IP2;
            } else {
                main_ip = IP1;
            }
        }
    }

    private static Message courier(Message msg) {

        do {
            try {
                out.writeObject(msg);
                msg = (Message) in.readObject();
            } catch (UnknownHostException e) {
                System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
                System.out.println("IO ligaçao com o caralho:" + e.getMessage());
                connect();
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFound:" + e.getMessage());
            }
        } while (msg.get_answer_boolean() == false);

        return msg;
    }

    /* Menu de arranque do sistema
     *  by Manuel
     */
    private static void menu_splash() {

        int o;
        Get_Option op = new Get_Option(3);

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
        System.out.print("Username: ");
        username = str.get_string("Username");
        System.out.print("Password: ");
        password = str.get_string("Password");
        System.out.println("");

        Message msg = new Message(username, password, "login");
        msg.set_request("user_login");

        msg = courier(msg);

        if (msg.get_answer_string() == "user_found") {
            System.out.println("Accepted");
            menu_welcome(username);
        } else {
            System.out.println("false");
        }
    }

    private static void menu_register() {
        
        String username;
        String password;
        Get_String str = new Get_String();
        boolean ok = false;
        
        while(ok == false){
        System.out.println("Login");
        System.out.print("Username: ");
        username = str.get_string("Username");
        System.out.print("Password: ");
        password = str.get_string("Password");
        System.out.println("");

        Message msg = new Message(username, password, "login");
        msg.set_request("register_user");

        msg = courier(msg);

        if (msg.get_answer_string() == "user_found") {
            System.out.println("Username already in use, choose another.");    
        } else {if(msg.get_answer_string() == "user_created"){
            System.out.println("New Valid User!");  
            ok = true;
        }
            
        }       
        }
        menu_login();

    }

    private static void menu_welcome(String username) {

        int o;
        boolean active;
        Get_Option op = new Get_Option(2);

        System.out.println("Wiki Starter");
        System.out.println("Welcome " + username);
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

        System.exit(0);
    }

    private static void menu_projects(boolean active) {

    }

    private static void menu_userInfo() {

    }

}
