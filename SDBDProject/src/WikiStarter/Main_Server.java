package WikiStarter;

import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main_Server {
    
    
    
    static boolean primary = false;
    static String ownIP;
    static int ownPORT;
    static String twinIP;
    static int twinPORT;
    static String databaseIP;
    static int databasePORT;
    static String twinNAME;
    static int udpPORT;

    ServerSocket myServerSocket;
    boolean ServerOn = true;

    public Main_Server() { 
        //UDP
        UDP_Ping_Pong pong = new UDP_Ping_Pong();
        pong.start();
    
        //TCP
        try 
        { 
            myServerSocket = new ServerSocket(ownPORT); 
        } 
        catch(IOException ioe) 
        { 
            System.out.println("Could not create server socket on port " + ownPORT +". Quitting."); 
            System.exit(-1); 
        } 
        Calendar now = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        System.out.println("It is now : " + formatter.format(now.getTime()));
        // Successfully created Server Socket. Now wait for connections. 
        while(ServerOn) 
        {                        
            try 
            { 
                // Accept incoming connections. 
                Socket clientSocket = myServerSocket.accept(); 
                // accept() will block until a client connects to the server.               
                // Start a Service thread 
                ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
                cliThread.start(); 
            } 
            catch(IOException ioe) 
            { 
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :"); 
                ioe.printStackTrace(); 
            } 
        }
        try 
        { 
            myServerSocket.close(); 
            System.out.println("Server Stopped"); 
        } 
        catch(Exception ioe) 
        { 
            System.out.println("Problem stopping server socket"); 
            System.exit(-1); 
        } 
    }  
    private static void read_config_file(){
       
        // The name of the file to open.
        String fileName = "Config1.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();
                if("Primary".equals(line)){
                    primary = true;
                } 
           line = bufferedReader.readLine();
           ownIP = line;
           System.out.println("My IP: " + ownIP);
           line = bufferedReader.readLine();
           ownPORT = Integer.parseInt(line);
           System.out.println("My PORT: " + ownPORT);
           line = bufferedReader.readLine();
           twinIP = line;
           System.out.println("My Twin IP: " + twinIP);
           line = bufferedReader.readLine();
           twinPORT = Integer.parseInt(line);
           System.out.println("My Twin PORT: " + twinPORT);
           line = bufferedReader.readLine();
           databaseIP = line;
           System.out.println("My Database IP: " + databaseIP);
           line = bufferedReader.readLine();
           databasePORT = Integer.parseInt(line);
           System.out.println("My Database PORT: " + databasePORT);
            line = bufferedReader.readLine();
           twinNAME = line;
           System.out.println("My twin's name: " + twinNAME);
           line = bufferedReader.readLine();
           udpPORT = Integer.parseInt(line);
           System.out.println("Conventioned UDP Port: " + udpPORT);
           
           
           // Always close files.
            bufferedReader.close();    
            System.out.println("Successfull Configuration");
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");    
            System.exit(-1);
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");  
            System.exit(-1);
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        
    }
    
    private static void udp_ping() {
        System.out.println("Starting Ping Pong Match!");
        System.out.println("Player TWO");
        DatagramSocket skt = null;
        String str;
        int count = 0;
        try {
            skt = new DatagramSocket();

            while (count < 6) {
                try {
                    str = "Ping";
                    System.out.println(str);
                    byte[] n = str.getBytes();
                    InetAddress endereco = InetAddress.getByName(twinNAME);
                    DatagramPacket enviar = new DatagramPacket(n, n.length, endereco, udpPORT);
                    skt.send(enviar);
                    Thread.sleep(1000);

                    byte[] buffer = new byte[1000];
                    DatagramPacket resposta = new DatagramPacket(buffer, buffer.length);

                    skt.setSoTimeout(2000); //Waits 2 seconds for the answer
                    skt.receive(resposta);
                    str = new String(resposta.getData(), 0, resposta.getLength());
                    System.out.println(str + " " + count + "Points");
                    count = 0;
                } catch (IOException es) {
                    count++;
                    System.out.println(count + "point(s) for me!");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SocketException e) {
            System.out.println("UDP Socket Error: " + e);
        }

    }

    
    /**
     * Performs a fake login to check if twin is working as primary.
     * Created so that the server knows if he was out.
     */
    private static void check_twin_status() {
      
        try {
            Socket s = new Socket(twinIP, twinPORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

            out.println("login Twin_Fake TwinServer Checking");
            out.flush();

            String reply = in.readLine();

            if ("user_found".equals(reply)) {
                if (primary == true){
                    System.out.println("Sorry about that crash....");
                }
                primary = false;
                System.out.println("Becoming Secondary Server.");
                
            }
            
            in.close();
            out.close();
            s.close();
            
        } catch (Exception e) {
            primary = true;
            System.out.println("Twin Server Not Responding");
            System.out.println("Becoming Primary.");
        }
        
        
    }
    
    public static void main (String[] args) 
    { 
        //reads configuration file
        read_config_file();
        
        //tests twin to check if he is working as primary    
        check_twin_status();
        
        //blocks in secondary mode if it is on
        if(primary == false){
         do{
            udp_ping();
            check_twin_status();
         }while(primary == false);
        }      
              
        //starts TCP server
        new Main_Server();        
    } 


    class UDP_Ping_Pong extends Thread {

        public void run() {
             System.out.println("Starting Ping Pong Match!");
              System.out.println("Player ONE");
              
              DatagramSocket skt = null;
                String str;
              
            try {
                skt = new DatagramSocket(udpPORT);
                while (true) {
                    
                   
                    byte[] buffer = new byte[1000];
                    DatagramPacket recebido = new DatagramPacket(buffer, buffer.length);
                    skt.receive(recebido);
                    str = new String(recebido.getData(), 0, recebido.getLength());
                    System.out.println(str);             
                    
                    str = "Pong";
                    System.out.println(str);
                    buffer = str.getBytes();
                    DatagramPacket enviar = new DatagramPacket(buffer, buffer.length, recebido.getAddress(), recebido.getPort());
                    skt.send(enviar);

                }
            } catch (IOException e) {
                System.out.println("Ping Pong Error!" + e);
            }

        }

    }
    
    
    class ClientServiceThread extends Thread 
    { 
        Socket myClientSocket;
        boolean m_bRunThread = true; 

        public ClientServiceThread() 
        { 
            super(); 
        } 

        ClientServiceThread(Socket s) 
        { 
            myClientSocket = s; 

        } 

        public void run() 
        {            
            // Obtain the input stream and the output stream for the socket 
            // A good practice is to encapsulate them with a BufferedReader 
            // and a PrintWriter as shown below. 
            BufferedReader in = null; 
            PrintWriter out = null; 
            String command;

            // Print out details of this connection 
            System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName()); 

            try 
            {                                
                in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream())); 
                out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream())); 

                // At this point, we can read for input and reply with appropriate output. 

                // Run in a loop until m_bRunThread is set to false 
                while(m_bRunThread) 
                {                    
                    // read incoming stream 
                    String clientCommand = in.readLine(); 
                    System.out.println("Client Says :" + clientCommand);

                    if(!ServerOn) 
                    { 
                        // Special command. Quit this thread 
                        System.out.print("Server has already stopped"); 
                        out.println("Server has already stopped"); 
                        out.flush(); 
                        m_bRunThread = false;   

                    } 

                    if(clientCommand.equalsIgnoreCase("quit")) { 
                        // Special command. Quit this thread 
                        m_bRunThread = false;   
                        System.out.print("Stopping client thread for client : "); 
                    } else if(clientCommand.equalsIgnoreCase("end")) { 
                        // Special command. Quit this thread and Stop the Server
                        m_bRunThread = false;   
                        System.out.print("Stopping client thread for client : "); 
                        ServerOn = false;
                    } else {
                        StringTokenizer commandst = new StringTokenizer(clientCommand);
                        command = commandst.nextToken();
                        if (command.equalsIgnoreCase("login")){  
                            StringTokenizer log_in = new StringTokenizer(clientCommand);
                            log_in.nextToken();
                            String username = log_in.nextToken();
                            String password = log_in.nextToken();
                            long msgid = Long.valueOf(log_in.nextToken());
                            System.out.println(msgid);
                            
                            Message msg = new Message (username, password, msgid);
                            msg.set_request("login");
                           
                            //part where I send message via RMI
                            msg.set_answer_string("user_found");
                            //and receive a reply
                            out.println(msg.get_answer_string()); 
                            out.flush(); 
                        }
                        if (command.equalsIgnoreCase("register")){  
                            StringTokenizer reg_ter = new StringTokenizer(clientCommand);
                            reg_ter.nextToken();
                            String username = reg_ter.nextToken();
                            String password = reg_ter.nextToken();
                            long msgid = Long.valueOf(reg_ter.nextToken());
                            System.out.println(msgid);
                            
                            Message msg = new Message (username, password, msgid);
                            msg.set_request("register");
                           
                            //part where I send message via RMI
                            msg.set_answer_string("accepted_new_user");
                            //and receive a reply
                            out.println(msg.get_answer_string()); 
                            out.flush(); 
                        }
                        if (command.equalsIgnoreCase("list")){ 
                            StringTokenizer li_st = new StringTokenizer(clientCommand);
                            li_st.nextToken();
                            String condition = li_st.nextToken();
                            long msgid = Long.valueOf(li_st.nextToken());
                            System.out.println(msgid);
                            
                            Message msg = new Message (msgid);
                            msg.set_request("list "+ condition);
                           
                            //part where I send message via RMI
                            
                            //just for tests
                            String [][] anArray;
                            anArray = new String [4][3];
                            anArray [0][0] = "0";
                            anArray [0][1] = "Smart Bulbs";
                            anArray [0][2] = "active";
                            anArray [1][0] = "1";
                            anArray [1][1] = "Smart Pants";
                            anArray [1][2] = "old";
                            anArray [2][0] = "2";
                            anArray [2][1] = "Test Tubes";
                            anArray [2][2] = "active";
                            anArray [3][0] = "3";
                            anArray [3][1] = "Robot Cookies";
                            anArray [3][2] = "active";
                            
                            msg.set_answer_table(anArray);
                            msg.set_answer_int(4); // tells how many lines the array has
                          //and receive a reply
                            int count = 0;
                            String std;
                            while (count < 4){
                                std = msg.get_answer_table()[count][0] + " " + msg.get_answer_table()[count][1] + " " + msg.get_answer_table()[count][2];
                                System.out.println(std);
                                out.println(std);
                                out.flush(); 
                                count ++;
                            }
                             out.println("end");
                                out.flush(); 
                            
                             
                            
                        }
                        
                        
                        
                            // Process it 
                            
                            System.out.println("Processed!");
                    }
                } 
            } 
            catch(Exception e) 
            { 
                e.printStackTrace(); 
            } 
            finally 
            { 
                // Clean up 
                try 
                {                    
                    in.close(); 
                    out.close(); 
                    myClientSocket.close(); 
                    System.out.println("...Stopped"); 
                } 
                catch(IOException ioe) 
                { 
                    ioe.printStackTrace(); 
                } 
            } 
        } 

        
        /**
         * Warnig! Testing Area
         * 
         */
        
        
        
        

    } 
}
