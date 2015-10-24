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

public class Get_Option {

    public int min_value;
    public int max_value;

    public Get_Option(int max) {

        min_value = 0;
        max_value = max;

    }

    public int get_option() {

        String input;
        int option = 0; //refault is zero or return

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            input = br.readLine();
            option = Integer.parseInt(input);
            if (option >= max_value || option <= 0) {
                Exception e = new Exception();
                throw e;
            } else {
            }
        } catch (Exception e) {
            System.out.println("Invalid Option");
            option = get_option(); //possível problema de recursão
        }

        return option;
    }

}
