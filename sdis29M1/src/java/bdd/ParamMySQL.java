/*
 * bdd/ParamMySQL.java
 */
package bdd;



import bean.Parametre;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Dominique_2
 */
public class ParamMySQL {
  
    private final Connection laConnection  = Connexion.getConnect("Localhost", 
                                                                "sdis29", 
                                                                "adminBDsdis", 
                                                                "mdpBDsdis");    
    /**
     * Recherche du paramètre correspondant au type et à l'indice passé en paramètres
     * @param typeParam
     * @param ind
     * @return Parametre concerné ou null si non trouvé
     */
    public Parametre read(String typeParam, int ind) {
        Parametre leParam = null;
        try {
            PreparedStatement prepStmt = null;
            String sql = "SELECT * FROM parametre WHERE idType=? AND indice=?";
            prepStmt = laConnection.prepareStatement(sql);
            prepStmt.setString(1, typeParam);
            prepStmt.setInt(2, ind);
            ResultSet result = prepStmt.executeQuery();
            if (result.next()) {
                leParam = new Parametre(result.getString(1), result.getInt(2), 
                                        result.getString(3));
            }
            prepStmt.close();
        } catch (SQLException ex) {
                System.out.println("SQLExeption : " + ex.getMessage());
                System.out.println("SQLState : " + ex.getSQLState());
                System.out.println("Code erreur : " + ex.getErrorCode());
        }        
        return leParam;
    }
    
    /**
     * Recherche de tous les paramètres du type passé en paramètre
     * @param unType
     * @return collection de Parametre correspondant au type
     */
    public ArrayList<Parametre> readType(String unType) {
        ArrayList<Parametre> lesParam = new ArrayList();
        try {
            PreparedStatement prepStmt = null;
            String sql = "SELECT * FROM parametre WHERE idType=? ";
            prepStmt = laConnection.prepareStatement(sql);
            prepStmt.setString(1, unType);
            ResultSet result = prepStmt.executeQuery();
            Parametre leParam;
            while (result.next()) {
                leParam = new Parametre(result.getString(1), result.getInt(2), 
                                        result.getString(3));
                lesParam.add(leParam);
            }
            prepStmt.close();
        } catch (SQLException ex) {
                System.out.println("SQLExeption : " + ex.getMessage());
                System.out.println("SQLState : " + ex.getSQLState());
                System.out.println("Code erreur : " + ex.getErrorCode());
        }  
        return lesParam;
    }
}
