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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main_Client extends Thread {

    static BufferedReader in = null;
    static PrintWriter out = null;
    static Socket s;

    static String IP1;
    static String IP2; 
static int soc;
    
    static String main_ip;
    static String username;
    static String password;
    static boolean loggedin = false;

    public static void main(String args[]) {
        
        // The name of the file to open.
        String fileName = "ficheiros/ConfigClient.txt";

        // This will reference one line at a time
        String line = null;
        
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();
            IP1 = line;
            line = bufferedReader.readLine();
            IP2 = line;
            line = bufferedReader.readLine();
            soc = Integer.valueOf(line);
            // Always close files.
            bufferedReader.close();
            main_ip=IP1;
            
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '"+ fileName + "'");
                    
                    
            System.exit(-1);
        } catch (IOException ex) {
            System.out.println( "Error reading file '"+ fileName + "'");
                   
                    
            System.exit(-1);
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        connect();
        terminal_command();
    }

    private static void connect() {

        int counter = 0;
        boolean connected = false;
        boolean first_round = true;

        while (counter < 6) {
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
                Thread.sleep(1000);
                s = new Socket(main_ip, soc);
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
            if (loggedin == true) {
                System.out.print("@" + username + "> ");
            } else {
                System.out.print("@JohnDoe> ");
            }

            s = str.get_string("Command. Type help for a list of commands");
            if (s.equalsIgnoreCase("help")) {
                System.out.println("login <username> <password>");
                System.out.println("register <username> <password>");
                System.out.println("list <option> - lists projecs by flag");
                System.out.println("list all      - lists all projects");
                System.out.println("list old      - lists closed projects");
                System.out.println("list active   - lists active projects");
                System.out.println("list mine     - lists user started projects");
                System.out.println("list bidded   - lists user bidded projects");
                System.out.println("open <project_id> - shows project description");
                System.out.println("wallet        - show user's ammount of credits");
                treated = true;
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
            if (s.contains("open")) {
                if (loggedin == false) {
                    System.out.println("Please login first.");
                } else {
                    command_open(s);
                }
                treated = true;
            }
            if (s.equalsIgnoreCase("wallet")) {
                if (loggedin == false) {
                    System.out.println("Please login first.");
                } else {
                    command_wallet(s);
                }
                treated = true;
            }

            if (s.equalsIgnoreCase("rewards")) {
                if (loggedin == false) {
                    System.out.println("Please login first.");
                } else {
                    command_rewards(s);
                }
                treated = true;
            }
            if (s.contains("create-project")) {
                if (loggedin == false) {
                    System.out.println("Please login first.");
                } else {
                    create_project(s);
                }
                treated = true;
            }
            if (s.contains("delete-project")) {
                if (loggedin == false) {
                    System.out.println("Please login first.");
                } else {
                    delete_project(s);
                }
                treated = true;
            }
            if (s.contains("add-reward")) {
                if (loggedin == false) {
                    System.out.println("Please login first.");
                } else {
                    add_reward(s);
                }
                treated = true;
            }
            if (s.contains("delete-reward")) {
                if (loggedin == false) {
                    System.out.println("Please login first.");
                } else {
                    delete_reward(s);
                }
                treated = true;
            }
            
            if (s.contains("pledge")) {
                if (loggedin == false) {
                    System.out.println("Please login first.");
                } else {
                    pledge(s);
                }
                treated = true;
            }
            

            //glued to last if
            if (s.equalsIgnoreCase("quit")) {
                exit(s);
                treated = true;
            }
            if (s.equalsIgnoreCase("terminate")) {
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
               // System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
               // System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
               // System.out.println("IO:" + e.getMessage());
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
                    return;
                } else if ("user_found".equals(reply)) {
                    System.out.println("Unable to Register. User Already Exists.");
                    return;
                }
                if ("already_done".equals(reply)) {
                    return;
                }
            } catch (UnknownHostException e) {
               // System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
               // System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
               // System.out.println("IO:" + e.getMessage());
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
            if (temp.equals("all") || temp.equals("active") || temp.equals("old") || temp.equals("mine") ) {
                try {
                    msg = s + " " + username + " " + msgID;

                    out.println(msg);
                    out.flush();

                    do {
                        System.out.println(reply);
                        reply = in.readLine();

                    } while (!reply.contains("end"));

                } catch (UnknownHostException e) {
                   // System.out.println("Sock:" + e.getMessage());
                } catch (EOFException e) {
                   // System.out.println("EOF:" + e.getMessage());
                } catch (IOException | NullPointerException e) {
                   // System.out.println("IO:" + e.getMessage());
                    connect();
                    command_list(s);
                }
            } else {
                System.out.println("Invalid Command: argument unknown.");
            }
        } else {
            System.out.println("Invalid Command: too few/many arguments.");
        }
    }

    private static void command_open(String s) {

        int count = 0;
        String reply = "Description:";
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

            try {
                msg = s + " " + msgID;

                out.println(msg);
                out.flush();

                reply = in.readLine();
                if ("unknown".equals(reply)) {
                    System.out.println("Project not found.");
                } else 
                    do {
                        System.out.println(reply);
                        reply = in.readLine();

                    } while (!reply.contains("end"));
                

            } catch (UnknownHostException e) {
               // System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
               // System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
               // System.out.println("IO:" + e.getMessage());
                connect();
                command_open(s);
            }
        } else {
            System.out.println("Invalid Command: too few/many arguments.");
        }

    }
    
    private static void delete_project(String s){
        
        int count = 0;
        String msgID;
        String reply;
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
            try {
                msg = s + " " + username + " " + msgID;

                out.println(msg);
                out.flush();
                
                reply = in.readLine();
                if ("done".equals(reply)) {
                    System.out.println("Project Deleted");
                    
                } else {
                    if ("error".equals(reply)) {
                        System.out.println("Not Found or not Allowed.");
                    }
                }

            } catch (UnknownHostException e) {
               // System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
               // System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
               // System.out.println("IO:" + e.getMessage());
                connect();
                command_login(s);
            }
        } else {
            System.out.println("Invalid Command: too few/many arguments.");
        }

    }
        
    

    private static void command_wallet(String s) {

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
        if (count == 1) {
            message_number = Calendar.getInstance().getTimeInMillis();
            msgID = Long.toString(message_number);
            try {
                msg = s + " " + username + " " + msgID;

                out.println(msg);
                out.flush();

                System.out.println(reply = in.readLine());

            } catch (UnknownHostException e) {
               // System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
               // System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
               // System.out.println("IO:" + e.getMessage());
                connect();
                command_login(s);
            }
        } else {
            System.out.println("Invalid Command: too few/many arguments.");
        }

    }

    private static void command_rewards(String s) {

        int count = 0;
        String reply = "Rewards:";
        String msgID;
        String msg;
        long message_number;

        //counts the number of arguments
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            st.nextToken();
            count++;
        }
        if (count == 1) {
            message_number = Calendar.getInstance().getTimeInMillis();
            msgID = Long.toString(message_number);

            try {
                msg = s + " " + username + " " + msgID;

                out.println(msg);
                out.flush();

                do {
                    System.out.println(reply);
                    reply = in.readLine();

                } while (!reply.contains("end"));

            } catch (UnknownHostException e) {
               // System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
               // System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
               // System.out.println("IO:" + e.getMessage());
                connect();
                command_open(s);
            }
        } else {
            System.out.println("Invalid Command: too few/many arguments.");
        }

    }

    private static void create_project(String s) {

        boolean failsafe = true;

        Get_String str = new Get_String();
        String reply;
        String pname;
        String description;
        String deadline;
        String ammount;

        int count = 0;
        int i = 0;
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
            String command = st2.nextToken();
            pname = st2.nextToken();
            System.out.println("Project " + pname + ":");
            System.out.print("Description: ");
            description = str.get_string("Description");
            System.out.print("Deadline in format HHmmddMMyyyy: ");//no failsafe implemented!
            deadline = str.get_string("Deadline");
            System.out.print("Ammount: ");
            ammount = str.get_string("Ammount");
            System.out.println("Rewards can be added by the command:");
            System.out.println("add-reward <project id>");

            while (failsafe == true) {
                try {
                    msg = command + " " + pname + "." + description + "." + deadline + "." + ammount + "." + username + "." + msgID;

                    out.println(msg);
                    out.flush();

                    reply = in.readLine();
                    if ("already_done".equals(reply)) {
                        failsafe = false;
                        return;
                    } else {
                        System.out.println("Your Project ID is: " + reply);
                        failsafe = false;
                    }
                } catch (UnknownHostException e) {
                   // System.out.println("Sock:" + e.getMessage());
                } catch (EOFException e) {
                   // System.out.println("EOF:" + e.getMessage());
                } catch (IOException | NullPointerException e) {
                   // System.out.println("IO:" + e.getMessage());
                    connect();
                }
            }
        } else {
            System.out.println("Invalid Command: too few/many arguments.");
        }

    }
    
    private static void add_reward(String s) {

        boolean failsafe = true;

        Get_String str = new Get_String();
        String reply;
        String id;
        String rname;
        String description;
        String ammount;

        int count = 0;
        int i = 0;
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
            id = st2.nextToken();
            System.out.print("Reward's Name: ");
            rname = str.get_string("Name");
            System.out.print("Reward's Description: ");//no failsafe implemented!
            description = str.get_string("Description");
            System.out.print("Ammount: ");
            ammount = str.get_string("Ammount");

            //while (failsafe == true) {
                try {
                    msg = s + " " + description + "." + rname + " " + ammount + " " + username + " " + msgID;

                    out.println(msg);
                    System.out.println("aqui! sent");
                    out.flush();

                    reply = in.readLine();
                    System.out.println("aqui! repy: "+reply);
                    if ("already_done".equals(reply)) {
                       // failsafe = false;
           
                        return;
                    } else {
                        if("Project_not_found".equalsIgnoreCase(reply)){
                            System.out.println("Project Not Found!");
                            //failsafe = false;
                            return;
                        }
                        System.out.println("Project " + reply + " has a new reward!");
                       // failsafe = false;
                    }
                } catch (UnknownHostException e) {
                   // System.out.println("Sock:" + e.getMessage());
                } catch (EOFException e) {
                   // System.out.println("EOF:" + e.getMessage());
                } catch (IOException | NullPointerException e) {
                   // System.out.println("IO:" + e.getMessage());
                    connect();
          //      }
            }
        } else {
            System.out.println("Invalid Command: too few/many arguments.");
        }

    }


    private static void pledge (String s) {

        boolean failsafe = true;

        Get_String str = new Get_String();
        String reply;
        

        int count = 0;
        int i = 0;
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


            while (failsafe == true) {
                try {
                    msg = s +  " " + username + " " + msgID;

                    out.println(msg);
                    out.flush();

                    reply = in.readLine();
                    if ("already_done".equals(reply)) {
                        failsafe = false;
                        return;
                    } else {
                        System.out.println(reply);
                        failsafe = false;
                    }
                } catch (UnknownHostException e) {
                   // System.out.println("Sock:" + e.getMessage());
                } catch (EOFException e) {
                   // System.out.println("EOF:" + e.getMessage());
                } catch (IOException | NullPointerException e) {
                   // System.out.println("IO:" + e.getMessage());
                    connect();
                }
            }
        } else {
            System.out.println("Invalid Command: too few/many arguments.");
        }

    }

    private static void delete_reward(String s) {

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
                msg = s + " " + username + " " + msgID;

                out.println(msg);
                out.flush();

                reply = in.readLine();

                if ("already_done".equals(reply)) {
                    return;
                } else {
                    System.out.println(reply);
                }

            } catch (UnknownHostException e) {
               // System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
               // System.out.println("EOF:" + e.getMessage());
            } catch (IOException | NullPointerException e) {
               // System.out.println("IO:" + e.getMessage());
                connect();
                delete_reward(s);
            }
        } else {
            System.out.println("Invalid Command: too few/many arguments.");
        }

    }

    private static void exit(String s) {

        try {
            out.println(s);
            out.flush();
        } catch (Exception e) {
           // System.out.println("Error Trying to Exit.");
        }

        System.exit(0);
    }

}
