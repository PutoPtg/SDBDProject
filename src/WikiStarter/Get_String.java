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
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Get_String {
    
    public Get_String() {
    }

    public String get_string(String name) {

        String input;
        String str_name = name;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            input = br.readLine();
            if (input == null) {
                Exception e = new Exception();
                throw e;
            } else {
            }
        } catch (Exception e) {
            System.out.println("Invalid " + str_name);
            input = get_string(str_name); //possível problema de recursão
        }

        return input;
    }

}
