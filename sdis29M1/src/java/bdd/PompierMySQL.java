/*
 * bdd/PompierMysql.java
 */
package bdd;

import bean.Caserne;
import bean.Parametre;
import bean.Pompier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dominique_2
 */
public class PompierMySQL {

    private final Connection theConnection = Connexion.getConnect("localhost",
            "sdis29",
            "adminBDsdis",
            "mdpBDsdis");
    // Tableau des attributs mis à jour
    // 1ère dimension : noms des attributs en bdd
    // 2ème dimension : noms des champs transmis
    // 3ème dimension : type de données, i pour Integet, s pour String
    private String[][] lesAttributs = {
        {"id", "idCaserne", "nom", "prenom", "statut", "typePers", "mail", "login", "mdp", "adresse", "cp", "ville", "bip", "grade", "commentaire"},
        {"idP", "idC", "ztNom", "ztPrenom", "ldrStatut", "ldrType", "ztMail", "ztLogin", "ztMdp", "ztAdresse", "ztCP", "zlVille", "ztBip", "ldrGrade", "ztObs"},
        {"i", "i", "s", "s", "i", "i", "s", "s", "s", "s", "s", "s", "s", "i", "s"}
    };

    /**
     * Recherche du pompier ayant le login et le mdp passé en paramètres
     *
     * @param login
     * @param mdp
     * @return Pompier correspondant ou null si non trouvé
     */
    public Pompier readAuthentif(String login, String mdp) {
        Pompier lePompier = null;
        try {
            PreparedStatement prepStmt;
            String sql = "SELECT * FROM pompier WHERE login=? AND mdp=? ";

            prepStmt = theConnection.prepareStatement(sql);
            prepStmt.setString(1, login);
            prepStmt.setString(2, mdp);
            ResultSet result = prepStmt.executeQuery();
            if (result.next()) {
                CaserneMySQL laCaserneMySQL = new CaserneMySQL();
                Caserne laCaserne = laCaserneMySQL.read(result.getInt(2));
                lePompier = constituerLePompier(result, laCaserne);
            }
            prepStmt.close();
        } catch (SQLException ex) {
            System.out.println("SQLExeption : " + ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("Code erreur : " + ex.getErrorCode());
        }
        return lePompier;
    }

    /**
     * Recherche des pompiers volontaires de la caserne
     *
     * @param idCaserne
     * @return collection des pompiers concernés
     */
    public ArrayList<Pompier> readLesPompiersCaserne(int idCaserne) {
        ArrayList<Pompier> lesPompiers = new ArrayList();
        try {
            PreparedStatement prepStmt;
            String sql = "SELECT * FROM pompier WHERE idCaserne=? AND Statut=1 AND typePers=2 ORDER BY nom, prenom";

            prepStmt = theConnection.prepareStatement(sql);
            prepStmt.setInt(1, idCaserne);
            ResultSet result = prepStmt.executeQuery();
            CaserneMySQL laCaserneMySQL = new CaserneMySQL();
            Caserne laCaserne = laCaserneMySQL.read(idCaserne);
            while (result.next()) {
                Pompier unPompier = constituerLePompier(result, laCaserne);
                lesPompiers.add(unPompier);
            }
            prepStmt.close();
        } catch (SQLException ex) {
            System.out.println("SQLExeption : " + ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("Code erreur : " + ex.getErrorCode());
        }
        return lesPompiers;
    }

    public Pompier constituerLePompier(ResultSet result, Caserne laCaserne) {
        Pompier lePompier = null;
        ParamMySQL leParamMySQL = new ParamMySQL();
        try {
            Parametre leStatut = leParamMySQL.read("statAgt", result.getInt(5));
            Parametre leType = leParamMySQL.read("typePer", result.getInt(6));
            Parametre leGrade = leParamMySQL.read("grade", result.getInt(15));
            lePompier = new Pompier(laCaserne, result.getInt(1),
                    result.getString(3), result.getString(4),
                    leStatut, leType,
                    result.getString(7), result.getString(8),
                    result.getString(9), result.getString(10),
                    result.getString(11), result.getString(12),
                    result.getString(13), leGrade, result.getString(16));
            //System.out.println("lePompier " + lePompier);
        } catch (SQLException ex) {
            Logger.getLogger(PompierMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lePompier;
    }

    /**
     * Recherche du dernier no de pompier utilisé pour une caserne
     *
     * @param idC : caserne concernée
     * @return dernier no de pompier utilisé dansla caserne
     */
    public int getIdMax(int idC) {
        int max = 0;
        try {
            PreparedStatement prepStmt;
            String sql = "SELECT max(pId) FROM pompier WHERE pCis = ?";
            prepStmt = theConnection.prepareStatement(sql);
            prepStmt.setInt(1, idC);
            ResultSet result = prepStmt.executeQuery();
            if (result.first()) {
                max = result.getInt(1);
            }
            prepStmt.close();
        } catch (SQLException ex) {
            System.out.println("SQLExeption : " + ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("Code erreur : " + ex.getErrorCode());
        }
        return max;
    }

    public Pompier update(HashMap<String, Object> lesParametres, int idP) {
//        Date dateJ = new Date();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String dateDuJour = dateFormat.format(dateJ);
        Pompier lePompierMaj = null;
        // Constitution de la requête en fonction des données transmises 
        String valeurs [] = new String[lesAttributs[1].length];
        String sql = "UPDATE pompier SET ";
        int nbAttributsMaj = 0;
        String virgule = "";
        for (HashMap.Entry unParam : lesParametres.entrySet()) {
            // Recherche attribut correspondant au champ trnasmis
            int i = 0;
            while (i < lesAttributs[1].length && !(lesAttributs[1][i].equals(unParam.getKey()))) {                
                i++;
            }
            if (i < lesAttributs[0].length) {
                sql += virgule + lesAttributs[0][i] + "= ?";
                valeurs[nbAttributsMaj] = "("+lesAttributs[2][i]+")" + unParam.getValue();
                nbAttributsMaj++;
                virgule = ", ";
            }
        }
        sql += ", dateModif = ? WHERE id = ?;";
        // Alimentation des valeurs de la requête
        PreparedStatement prepStmt;
        try {
            prepStmt = theConnection.prepareStatement(sql);
            for (int i=0; i<nbAttributsMaj; i++) {
                if (valeurs[i].substring(0, 3).equals("(s)")) {
                    prepStmt.setString(i+1, valeurs[i].substring(3));
                } else {
                    prepStmt.setInt(i+1, Integer.parseInt(valeurs[i].substring(3)));
                }               
            }
            prepStmt.setTimestamp(nbAttributsMaj+1, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            prepStmt.setInt(nbAttributsMaj+2, idP);
            System.out.println("sql "+prepStmt);
            
            int nbLigne = prepStmt.executeUpdate();

            //Recherche du no du pompier créé
            if (nbLigne > 0) {
                System.out.println("nbLigne " + nbLigne);
                lePompierMaj = read(idP);
                System.out.println("lePompierMaj : "+ lePompierMaj);    
            }
            prepStmt.close();
        } catch (SQLException ex) {
            System.out.println("SQLExeption : " + ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("Code erreur : " + ex.getErrorCode());
        }
        
        
        
        
        return lePompierMaj;
//        String sql = "UPDATE pompier "
//                + "SET pNom = ?, pPrenom = ?, pMail = ?, "
//                + "pLogin = ?, pMdp = ?, pAdresse = ?, pCp = ?, pVille = ?, "
//                + "pBip = ?, pGrade = ?, pCommentaire = ?, pDateModif = ? "
//                + "WHERE pCis=? AND pId=?; ";
//        return requeteMaj(sql, lesParametres);
    }

    public Pompier create(HashMap<String, Object> lesParametres) {
        Pompier lePompierCree = null;
        // Constitution de la requête en fonction des données transmises 
        String valeurs [] = new String[lesAttributs[1].length];
        String sql = "INSERT INTO pompier (";
        int nbAttributsMaj = 0;
        String virgule = "";
        for (HashMap.Entry unParam : lesParametres.entrySet()) {
            // Recherche attribut correspondant au champ trnasmis
            int i = 0;
            while (i < lesAttributs[1].length && !(lesAttributs[1][i].equals(unParam.getKey()))) {                
                i++;
            }
            if (i < lesAttributs[0].length) {
                sql += virgule + lesAttributs[0][i];
                valeurs[nbAttributsMaj] = "("+lesAttributs[2][i]+")" + unParam.getValue();
                nbAttributsMaj++;
                virgule = ", ";
            }
        }
        sql += ", dateEnreg) values (?";
        for (int i=0; i<nbAttributsMaj; i++ ) {
            sql+= ", ?";
        }
        sql += ");";
        
        // Alimentation des valeurs de la requête
        PreparedStatement prepStmt;
        try {
            prepStmt = theConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i=0; i<nbAttributsMaj; i++) {
                if (valeurs[i].substring(0, 3).equals("(s)")) {
                    prepStmt.setString(i+1, valeurs[i].substring(3));
                } else {
                    prepStmt.setInt(i+1, Integer.parseInt(valeurs[i].substring(3)));
                }               
            }
            prepStmt.setTimestamp(nbAttributsMaj+1, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            System.out.println("sql "+prepStmt);
            
            int nbLigne = prepStmt.executeUpdate();

            //Recherche du no du pompier créé
            if (nbLigne > 0) {
                ResultSet result = prepStmt.getGeneratedKeys();
                if (result.first()) {
                    int id = result.getInt(1);
                    lePompierCree = read(id);
                    System.out.println("lePompierCree : "+ lePompierCree);                    
                }
            }
            prepStmt.close();
        } catch (SQLException ex) {
            System.out.println("SQLExeption : " + ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("Code erreur : " + ex.getErrorCode());
        }
         
        return lePompierCree;
    }

    private Pompier requeteMaj(String sql, HashMap<String, Object> lesParametres) {
        int nbLigne = 0;
        try {
            PreparedStatement prepStmt;
            prepStmt = theConnection.prepareStatement(sql);
            prepStmt.setString(1, (String) lesParametres.get("ztNom"));
            prepStmt.setString(2, (String) lesParametres.get("ztPrenom"));
            prepStmt.setString(3, (String) lesParametres.get("ztMail"));
            prepStmt.setString(4, (String) lesParametres.get("ztLogin"));
            prepStmt.setString(5, (String) lesParametres.get("ztMdp"));
            prepStmt.setString(6, (String) lesParametres.get("ztAdresse"));
            prepStmt.setString(7, (String) lesParametres.get("ztCP"));
            prepStmt.setString(8, (String) lesParametres.get("ztVille"));
            prepStmt.setString(9, (String) lesParametres.get("ztBip"));
            prepStmt.setInt(10, (Integer) lesParametres.get("ldrGrade"));
            prepStmt.setString(11, (String) lesParametres.get("taObs"));
            //prepStmt.setDate(12, java.sql.Date.valueOf(java.time.LocalDate.now()));
            prepStmt.setTimestamp(12, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            prepStmt.setInt(13, (Integer) lesParametres.get("idC"));
            prepStmt.setInt(14, (Integer) lesParametres.get("zhIdP"));
            if (sql.substring(0, 6).equals("INSERT")) {
                prepStmt.setInt(15, (Integer) lesParametres.get("idStatut"));
                prepStmt.setInt(16, (Integer) lesParametres.get("idType"));
            }

            System.out.println(prepStmt);
            nbLigne = prepStmt.executeUpdate();
            prepStmt.close();
        } catch (SQLException ex) {
            System.out.println("SQLExeption : " + ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("Code erreur : " + ex.getErrorCode());
        }
        Pompier lePompierMaj = null;
        if (nbLigne > 0) {
            //lePompierMaj = read((Integer) lesParametres.get("idC"), (Integer) lesParametres.get("zhIdP"));
        }
        return lePompierMaj;
    }

    public Pompier read(int idP) {
        Pompier unPompier = null;
        try {
            PreparedStatement prepStmt;
            String sql = "SELECT * FROM pompier WHERE id=? ";

            prepStmt = theConnection.prepareStatement(sql);
            prepStmt.setInt(1, idP);        
            ResultSet result = prepStmt.executeQuery();
            if (result.next()) {
                CaserneMySQL laCaserneMySQL = new CaserneMySQL();
                Caserne laCaserne = laCaserneMySQL.read(result.getInt(2));
                unPompier = constituerLePompier(result, laCaserne);
            }
            prepStmt.close();
        } catch (SQLException ex) {
            System.out.println("SQLExeption : " + ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("Code erreur : " + ex.getErrorCode());
        }
        return unPompier;
    }
}
