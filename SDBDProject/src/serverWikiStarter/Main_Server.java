/*
 * [PT]
 * Código de servidor para ligação ao cliente
 * Projecto de Sistemas Distribuídos e Bases de Dados 
 * 2015
 * Universidade de Coimbra
 * 
 * [EN]
 * Server side code to connect to client
 * Academic project to Distributed Systems and Database
 * 2015
 * University of Coimbra
 */

package serverWikiStarter;

/**
 * @author Manuel Madeira Amado
 * @author Alexandra Leandro
 * @author Inês Fidalgo
 */

import java.net.*;
import java.io.*;

public class Main_Server {
    
    public static void main(String args[]){
        int numero=0;
        
        try{
            int serverPort = 6000;
            System.out.println("A Escuta no Porto 6000");
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("LISTEN SOCKET="+listenSocket);
            while(true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket);
                numero ++;
                new Connection(clientSocket, numero);
            }
        }catch(IOException e)
        {System.out.println("Listen:" + e.getMessage());}
    }
}

//= Thread para tratar de cada canal de comunicação com um cliente
class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;
    
    public Connection (Socket aClientSocket, int numero) {
        thread_number = numero;
        try{
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        }catch(IOException e){System.out.println("Connection:" + e.getMessage());}
    }
    //=============================
    public void run(){
        String resposta;
        try{
            while(true){
                //an echo server
                String data = in.readUTF();
                System.out.println("T["+thread_number + "] Recebeu: "+data);
                resposta=data.toUpperCase();
                out.writeUTF(resposta);
            }
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);}
    }
    
}
