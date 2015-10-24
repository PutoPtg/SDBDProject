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

package WikiStarter;

/**
 * @author Manuel Madeira Amado
 * @author Alexandra Leandro
 * @author Inês Fidalgo
 */

import java.io.*;
import java.net.*;

public class Main_Server {

   public static void main(String[] arg) {

      Message msg = null;

      try {

         ServerSocket socketConnection = new ServerSocket(6000);

         System.out.println("Server Waiting");

         Socket pipe = socketConnection.accept();

         ObjectInputStream serverInputStream = new ObjectInputStream(pipe.getInputStream());

         ObjectOutputStream serverOutputStream = new ObjectOutputStream(pipe.getOutputStream());

         msg = (Message)serverInputStream.readObject();

         msg.set_answer_boolean(true);
         
         serverOutputStream.writeObject(msg);

         serverInputStream.close();
         serverOutputStream.close();


      }  catch(Exception e) {System.out.println(e); 
      }
   }

}