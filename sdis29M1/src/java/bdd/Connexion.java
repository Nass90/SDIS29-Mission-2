package bdd;
/*
Connexion.java
Classe permettant d'établir une connexion avec une base de données mySQL
*/
import java.sql.Connection;
import java.sql.DriverManager;

public class Connexion {
    private static Connection connect;      // Variable de connexion
  
  /**
   * Constructeur
   * @param serveur     nom du serveur, localhost si local
   * @param bdd         nom de la base de données
   * @param nomUtil     nom utilisateur
   * @param mdp         mot de passe lié à l'utilisateur
   */   
  private Connexion(String serveur, String bdd, String nomUtil, String mdp) {     
    try {
      // 1. Chargement du driver
      //Class.forName("com.mysql.jdbc.Driver");
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("Driver accessible");
 
      // 2. Initialisation des paramètres de connexion
      String host   = serveur;                              // Serveur de bd
      String dbname = bdd;                                  // Nom bd
      String url = "jdbc:mysql://" + host + "/" + dbname;   // url de connexion
      url += "?autoReconnect=true";  // Ajout 26/09/2021
      System.out.println("url : "+url);
      String user = nomUtil;                                // nom du user
      System.out.println("nomUtil : "+nomUtil);
      String passwd = mdp;                                  // mot de passe
      System.out.println("mdp : "+mdp);
      
      // 3. Connexion
      connect = (Connection) DriverManager.getConnection(url, user, passwd);
      System.out.println("Connexion réussie !");      
           
    } catch (Exception e) {        
      e.printStackTrace();
    }     
  }

    /**
     * Retourne la connection établie (Création d'une connection si elle n'existe pas)
     * @param serveur  nom du serveur, localhost si local
     * @param bdd nom de la base de données
     * @param nomUtil nom utilisateur
     * @param mdp mot de passe lié à l'utilisateur
     * @return connection établie
     */  
    public static Connection getConnect(String serveur, String bdd, String nomUtil, String mdp) {
        //System.out.println("getConnect");
        if (connect == null) {
            new Connexion(serveur, bdd, nomUtil, mdp);
        }
        return connect;
    }    
}
