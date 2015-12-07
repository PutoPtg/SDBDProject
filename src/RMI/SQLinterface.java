package RMI;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.sql.*;

public class SQLinterface {
    
    public SQLinterface(){
        
    }

//    public static void main(String[] args) {
//
//        //addUser("joao","123");
//        //login("joao","132");
//        consultarSaldo("joao");
//
//    }

    //integrado e testado mas pode ser melhorado
    public synchronized String login(String uname, String pword) {
        Sql sql = new Sql();

        String query1 = "SELECT nome, pass FROM utilizadores WHERE nome LIKE '" + uname + "'";
        //String query2 = "SELECT nome FROM utilizadores WHERE nome LIKE '" + uname + "'";
        ResultSet rs = sql.getFromDB(query1);
        try {
            if(!rs.next()){
                return "unknown_user";
            }
            if (rs.getString("pass").contains(pword)) {
                System.out.println("Login com sucesso(" + rs.getString(1) + ")");
                return "user_found";
            } else if (rs.getString("nome").contains(uname)) {
                System.out.println("Palavra passe Errada!(" + rs.getString(1) + ")");
                return "wrong_password";
            } else {
                System.out.println("utilizador inexistente");
                return "unknown_user";
            }

        } catch (Exception e) {
            System.out.println("Erro no login: " + e);
            return "Error in login: " + e;
        }
    }

    //integrado e testado
    public synchronized String addUser(String uname, String password) {

        Sql sql = new Sql();
        System.out.println("antes select");
        String query1 = "SELECT nome FROM utilizadores WHERE nome LIKE '" + uname + "'";

        String query2 = "INSERT INTO utilizadores (nome,pass,saldo) VALUES('" + uname + "','" + password + "',100)";

        ResultSet rs = sql.getFromDB(query1);
        try {
            if (rs.next()) {
                String txt = rs.getString(1);
                System.out.println(txt);
                System.out.println("Já existe");
                return "user_found";
            } else {
                sql.getFromDB(query2);
                System.out.println("Adicionado user com nome:" + uname + "");
                return "accepted_new_user";
            }
        } catch (Exception e) {
            System.out.println("Erro a adicionar user");
            return "Error in addUser: " + e;
        }

    }

    public synchronized String consultarSaldo(String uname) {
        Sql sql = new Sql();
        float saldo = 0;
        String query = "SELECT saldo FROM utilizadores WHERE nome LIKE '" + uname + "'";

        ResultSet rs = sql.getFromDB(query);
        try {
            if (rs.next()) {
                saldo = rs.getFloat(1);
                System.out.println("saldo do user " + uname + ":" + saldo + "");
                return Float.toString(saldo);
            }
            return "unknown_user";
        } catch (Exception ex) {
            // error executing SQL statement
            System.out.println("Erro a consultar saldo");
            return "Error in consultarSaldo: " + ex;
        }

    }

    public synchronized String criarProjeto(String nome, String username, String inicio, String fim, float pretendido, float saldo) {
        Sql sql = new Sql();
        String query = "Select max(id) from Projetos";
        int id = 0;
        ResultSet r = sql.getFromDB(query);
        try {
            if (r.next()) {
                id = r.getInt(1) + 1;
            }

        } catch (Exception ex) {
            // error executing SQL statement
            System.out.println("Erro a obter id de projeto");
        }
        try {
            sql.getFromDB("INSERT TO projetos (id,nome, admin, saldo, pretendido,inicio,final) VALUES (" + id + ",'" + nome + "','" + username + "',0,'" + pretendido + "',SYSDATE,to_date('" + inicio + "','dd-mm-yyyy'),SYSDATE,to_date('" + fim + "','dd-mm-yyyy')");
            System.out.println("Projeto com id:" + id + "criado");
        } catch (Exception ex) {
            System.out.println("Erro a criar projetos");
        }

        return "Done";
    }

    public synchronized String adicionarRecompensas(int idP, String nome, float valor) {
        Sql sql = new Sql();
        String query = "Select max(id) from Projetos";
        int id = 0;
        ResultSet r = sql.getFromDB(query);
        try {
            if (r.next()) {
                id = r.getInt(1) + 1;
            }

        } catch (Exception ex) {
            // error executing SQL statement
            System.out.println("Erro a obter id de Recompensa");
        }
        try {
            sql.getFromDB("INSERT TO Recompensas (idP,idR,entregue,nome) VALUES (" + idP + "," + id + ",0,'" + nome + "'");
            System.out.println("Recompensa com id:" + id + "criado");
        } catch (Exception ex) {
            System.out.println("Erro a criar Recompensa");
        }

        return "Done";
    }

    public synchronized String eliminarRecompensas(int id) {
        Sql sql = new Sql();
        String query = "delete from Recompensas where idR=" + id + "";
        int idPro = 0;
        try {
            sql.getFromDB(query);
            ResultSet r = sql.getFromDB("select idP from Recompensas where idR=" + id + "");
            try {
                if (r.next()) {
                    idPro = r.getInt(1);
                }

            } catch (Exception ex) {
                // error executing SQL statement
                System.out.println("Erro a obter id de projeto da Recompensa");
            }
            System.out.println("Recompensa com id:" + id + " do projeto com id" + idPro + " eliminada");
        } catch (Exception ex) {
            System.out.println("Erro a eliminar Recompensa");
        }
        return "Done";
    }

    public synchronized String mostrarRecompensas(int idp) {
        Sql sql = new Sql();
        String query = "SELECT idP, idR, entrege, nome FROM Recompensas WHERE idP = " + idp;
        ResultSet rs = sql.getFromDB(query);
        String txt = "";
        try {
            if (rs.next()) {
                do {
                    txt += "Nome: " + rs.getString(4) + "\nID do recompensa: " + rs.getString(2) + "\nID do Projecto: " + rs.getString(1) + "Entregue (1 para sim 0 para no)" + rs.getString(3);

                } while (rs.next());
            } else {
                txt = "Não tem recompensas.\n";
                System.out.println(txt);
            }
        } catch (Exception ex) {
            // error executing SQL statement
            System.out.println("Erro a aceder a recompensas");
        }
        return "done";
    }

    public synchronized String mostrarMensagens(String idPro) {
        Sql sql = new Sql();
        String query = "SELECT idP,idU,idM,mensagem FROM mensagem WHERE idP = " + idPro + " ORDER BY time ASC";
        ResultSet rs = sql.getFromDB(query);
        String txt = "Mensagens no projecto com o id -" + idPro + "- :\n";
        try {
            if (rs.next()) {
                do {
                    txt += "id utilizador: " + rs.getString(2) + "id mensagem: " + rs.getString(3) + " mensagem: " + rs.getString(4) + "\n";
                } while (rs.next());
            } else {
                txt = "o projecto " + idPro + " n�o tem mensagens.\n";
                System.out.println(txt);
            }
        } catch (Exception ex) {
            System.out.println("erro a aceder a mensagens");
        }
        return "done";
    }
}

class Sql {

    Statement stmt = null;
    Connection con = null;

    Sql() {

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bd", "bd");
            stmt = con.createStatement();

        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("DB: Cannot find driver class");
            System.exit(1);
        } catch (java.sql.SQLException e) {
            System.out.println("DB: Cannot get connection");
            System.exit(1);
        } catch (Exception ex) {
            System.out.println("DB: cannot create statement");
            System.exit(1);
        }

    }

    void setToDB(String s) {
        System.out.println("Executing " + s);
        try {
            //stmt.executeUpdate(s);
            stmt.execute(s);
        } catch (SQLException ex) {
            // error executing SQL statement
            System.out.println("Error: " + ex);
        }
    }
    //=======================================

    ResultSet getFromDB(String s) {
        System.out.println("Executing " + s);
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(s);
        } catch (SQLException ex) {
            // error executing SQL statement
            System.out.println("Error: " + ex);
        }
        return rs;
    }
    //=======================================

    void closeDatabase() {
        // close database
        try {
            con.close();
        } catch (Exception ex) {
        }
    }
}
