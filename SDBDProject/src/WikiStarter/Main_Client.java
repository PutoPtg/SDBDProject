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
import java.util.Calendar;
import java.util.StringTokenizer;

public class Main_Client extends Thread {

    static BufferedReader in = null;
    static PrintWriter out = null;
    static Socket s;

    static String IP1 = "192.168.56.11";
    static String IP2 = "192.168.56.13";

    static String main_ip = IP1;
    static String username;
    static String password;
    static boolean loggedin = false;

    public static void main(String args[]) {
        connect();
        terminal_command();
    }

    private static void connect() {

        int counter = 0;
        boolean connected = false;
        boolean first_round = true;

        while (counter < 4) {
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
                s = new Socket(main_ip, 6666);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
                counter = 7;
                connected = true;
                System.out.println("Connected");
            } catch (UnknownHostException e) {
                System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: IP " + main_ip + " out of reach");
                if (counter % 2 == 1) {
                    main_ip = IP2;
                } else {
                    main_ip = IP1;
                }
            } catch (InterruptedException e) {
                System.out.println("IO2:" + e.getMessage());
            }
        }

        if (connected == false) {
            System.out.println("Connection Error. Exiting.");
            System.exit(-1);
        }

    }

    private static void terminal_command() {

        Get_String str = new Get_String();
        String s;

        boolean treated;

        System.out.println("Wellcome to WikiStarter!");
        System.out.println("Need Help? Type 'Help'");

        while (true) {
            treated = false;
            s = str.get_string("Command. Type help for a list of commands");
            if (s.equalsIgnoreCase("help")) {
                System.out.println("login <username> <password>");
                System.out.println("register <username> <password>");
            }
            if (s.contains("login")) {
                if (loggedin == false) {
                    command_login(s);
                } else {
                    System.out.println("Already logged in as " + username);
                    System.out.println("Please logout first.");
                }
                treated = true;
            }
            if (s.contains("register")) {
                if (loggedin == false) {
                    command_register(s);
                } else {
                    System.out.println("Already logged in as " + username);
                    System.out.println("Please logout before register new user.");
                }
                treated = true;
            }
            if (s.equalsIgnoreCase("logout")) {
                if (loggedin == false) {
                    System.out.println("Already logged out.");
                } else {
                    System.out.println("See you soon " + username + "!");
                    username = null;
                    password = null;
                    loggedin = false;
                }
                treated = true;
            }
            if (s.contains("list")) {
                if (loggedin == false) {
                    System.out.println("Please login first.");
                } else {
                    command_list(s);
                }
                treated = true;
            }

            //glued to last if
            if (s.equalsIgnoreCase("quit")) {
                exit(s);
                treated = true;
            }
            //last if
            if (treated == false) {
                System.out.println("Unknown Command");
                treated = true;
            }

        }//end of while
    }

    /**
     *
     * Commands Methods
     *
     */
    /**
     * Performs Login into database
     *
     * @param s
     */
    private static void command_login(String s) {

        int count = 0;
        String reply;
        String msgID;
        String msg;
        long message_number;

        //counts the number of arguments
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            st.nextToken();
            count++;
        }
        if (count == 3) {
            message_number = Calendar.getInstance().getTimeInMillis();
            msgID = Long.toString(message_number);
            try {
                msg = s + " " + msgID;

                out.println(msg);
                out.flush();

                reply = in.readLine();

                if ("user_found".equals(reply)) {
                    System.out.println("Accepted");
                    loggedin = true;
                    StringTokenizer st2 = new StringTokenizer(s);
                    st2.nextToken();
                    username = st2.nextToken();
                    password = st2.nextToken();
                } else {
                    if ("wrong_password".equals(reply)) {
                        System.out.println("Wrong Password!");
                    } else {
                        if ("unknown_user".equals(reply)) {
                            System.out.println("User Not Found!");
                        }
                    }
                }
            } catch (UnknownHostException e) {
                System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
                System.out.println("IO:" + e.getMessage());
                connect();
                command_login(s);
            }
        } else {
            System.out.println("Invalid Command: too few arguments.");
        }
    }

    private static void command_register(String s) {

        int count = 0;
        String reply;
        String msgID;
        String msg;
        long message_number;

        //counts the number of arguments
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            st.nextToken();
            count++;
        }
        if (count == 3) {
            message_number = Calendar.getInstance().getTimeInMillis();
            msgID = Long.toString(message_number);
            try {
                msg = s + " " + msgID;

                out.println(msg);
                out.flush();

                reply = in.readLine();

                if ("accepted_new_user".equals(reply)) {
                    System.out.println("User Registered Successfully!");
                    System.out.println("Please Login.");
                } else if ("user_found".equals(reply)) {
                    System.out.println("Unable to Register. User Already Exists.");
                }
            } catch (UnknownHostException e) {
                System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
                System.out.println("IO:" + e.getMessage());
                connect();
                command_register(s);
            }
        } else {
            System.out.println("Invalid Command: too few arguments.");
        }
    }

    private static void command_list(String s) {

        int count = 0;
        String reply = "List:";
        String msgID;
        String msg;
        long message_number;

        //counts the number of arguments
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            st.nextToken();
            count++;
        }
        if (count == 2) {
            message_number = Calendar.getInstance().getTimeInMillis();
            msgID = Long.toString(message_number);

            StringTokenizer st2 = new StringTokenizer(s);
            st2.nextToken();
            String temp = st2.nextToken();
            if (temp.equals("all") || temp.equals("active") || temp.equals("old") || temp.equals("mine") || temp.equals("bidded")) {
                try {
                    msg = s + " " + msgID;

                    out.println(msg);
                    out.flush();

                    do{                       
                    System.out.println(reply);
                    reply = in.readLine();
                    
                    }while (!reply.contains("end"));
                    
                } catch (UnknownHostException e) {
                    System.out.println("Sock:" + e.getMessage());
                } catch (EOFException e) {
                    System.out.println("EOF:" + e.getMessage());
                } catch (IOException | NullPointerException e) {
                    System.out.println("IO:" + e.getMessage());
                    connect();
                    command_list(s);
                }
            }else {
                System.out.println("Invalid Command: argument unknown.");
            }
        } else {
            System.out.println("Invalid Command: too few arguments.");
        }
    }

    private static void exit(String s) {

        try {
            out.println(s);
            out.flush();
        } catch (Exception e) {
            System.out.println("Error Trying to Exit.");
        }

        System.exit(0);
    }

    private static void menu_projects(boolean active) {

    }

    private static void menu_userInfo() {

    }

}
