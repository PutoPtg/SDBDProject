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
import java.security.Timestamp;
import java.util.*;
import java.text.SimpleDateFormat;

public class Message implements Serializable {

    private long message_number;
    private String username;
    private String password;
    private String last_menu;
    private String request;
    private String [][] answer_table;
    private Boolean answer_boolean;
    private String answer_string;
    private int answer_int;

    public Message(String a_username, String a_password, String a_menu){

        message_number = Calendar.getInstance().getTimeInMillis();
        username = a_username;
        password = a_password;
        last_menu = a_menu;
        request = "";
        answer_table = new String [0][0];
        answer_boolean = false;
        answer_string = "";
        answer_int = 0;
    }
    
    public long get_message_number(){
        return message_number;
    }
    
    public String get_username(){
        return username;
    }
    
    public String get_password(){
        return password;
    }
    
    public String get_last_menu(){
        return last_menu;
    }

    public void set_request (String req){
        request = req;
    }
    
    public String get_request() {
        return request;
    }

    public void set_answer_table(String [][] table){
        answer_table = table;
    }
    
    public String [][] get_answer_table(){
        return answer_table;
    }
    
    public void set_answer_boolean(boolean bool){
        answer_boolean = bool;
    }
    
    public boolean get_answer_boolean(){
        return answer_boolean;
    }
    
    public void set_answer_string(String str){
        answer_string = str;
    }

    public String get_answer_string(){
        return answer_string;
    }
    
    public void set_answer_int(int it){
        answer_int = it;
    }
    
    public int get_answer_int(){
        return answer_int;
    }
}
