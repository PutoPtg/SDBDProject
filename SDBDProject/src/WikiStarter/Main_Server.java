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
/**
 *
 * Servidor Eco de teste
 *
 *
 */
package WikiStarter;

/**
 * @author Manuel Madeira Amado
 * @author Alexandra Leandro
 * @author Inês Fidalgo
 */
// TCPServer2.java: Multithreaded server
import java.net.*;
import java.io.*;

public class Main_Server {

    public static void main(String args[]) {
        int numero = 0;

        try {
            int serverPort = 6000;
            System.out.println("A Escuta no Porto 6000");
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("LISTEN SOCKET=" + listenSocket);
            while (true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                numero++;
                new Connection(clientSocket, numero);
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }
    }
}
//= Thread para tratar de cada canal de comunicação com um cliente

class Connection extends Thread {

    ObjectInputStream in;
    ObjectOutputStream out;
    Socket clientSocket;
    int thread_number;
    Message msg = null;
    String o;

    public Connection(Socket aClientSocket, int numero) {
        thread_number = numero;
        try {
            clientSocket = aClientSocket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }
    //=============================

    public void run() {

        try {
            while (true) {

                msg = (Message) in.readObject();

                System.out.println("T[" + thread_number + "] Recebeu pedido: " + msg.get_message_number());

                o = msg.get_request();

//                switch (o) {
//                    case "user_login":
//                        msg.set_answer_string("user_found");
//                        out.writeObject(msg);
//                    case "register_user":
//                        msg.set_answer_string("user_created");
//                        out.writeObject(msg);
//                    //case "user_login": msg.set_answer_string("user_found");
//                    //  case "user_login": msg.set_answer_string("user_found");
//                }
                out.writeObject(msg);
                System.out.println("eviou");
            }
        } catch (EOFException e) {
            System.out.println("EOF:" + e);
        } catch (IOException e) {
            System.out.println("IO:" + e);
        } catch (ClassNotFoundException e) {
            System.out.println("CNF:" + e);
        }

    }
}
