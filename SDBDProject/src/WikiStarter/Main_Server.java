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

public class Main_Server {
    
    
    
    static boolean primary = false;
    static String ownIP;
    static int ownPORT;
    static String twinIP;
    static int twinPORT;
    static String databaseIP;
    static int databasePORT;

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
           line = bufferedReader.readLine();
           ownPORT = Integer.parseInt(line);
           line = bufferedReader.readLine();
           twinIP = line;
           line = bufferedReader.readLine();
           twinPORT = Integer.parseInt(line);
           line = bufferedReader.readLine();
           databaseIP = line;
           line = bufferedReader.readLine();
           databasePORT = Integer.parseInt(line);
  
           // Always close files.
            bufferedReader.close();         
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
    
    private static void udp_ping(){
        
        try {
            //while(true){
            DatagramSocket primarySocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
     
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            
            String sentence = "Ping!";
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9877);
            primarySocket.send(sendPacket);
            
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            primarySocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);
            primarySocket.close();
           // }
        } catch (IOException e) {
System.out.println("udp ping: "+e);
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

            out.println("Twin_Fake");
            out.println("TwinServer");
            out.println("Checking");
            out.println("login");
            out.flush();

            String reply = in.readLine();

            if ("user_found".equals(reply)) {
                primary = false;
            }
            
            in.close();
            out.close();
            s.close();
            
        } catch (Exception e) {
            System.out.println("Twin Server Not Responding:" + e.getMessage());
        }
        
        
    }
    
    public static void main (String[] args) 
    { 
        //reads configuration file
        read_config_file();
        
        //tests twin to check if he is working as primary
        if(primary == true){
            check_twin_status();
        }
        //blocks in secondary mode if it is on
        if(primary == false){
         udp_ping();   
        }      
        //starts TCP server
        new Main_Server();        
    } 


    class UDP_Ping_Pong extends Thread {

        public void run() {
            try {
                DatagramSocket serverSocket = new DatagramSocket(9876);
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];
//                while (true) {
//                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//                    serverSocket.receive(receivePacket);
//                    String sentence = new String(receivePacket.getData());
//                    System.out.println("RECEIVED: " + sentence);
//                    InetAddress IPAddress = receivePacket.getAddress();
//                    int port = receivePacket.getPort();
//                    sentence = "Pong!";
//                    sendData = sentence.getBytes();
//                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//                    serverSocket.send(sendPacket);
//                }
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
                            // Process it 
                            out.println("user_found"); 
                            out.flush(); 
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


    } 
}
