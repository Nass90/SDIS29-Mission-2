/*
 * form/authentifForm.java
 * Controle des données saisie dans le formulaire d'authentification
 */
package form;

import bdd.ParamMySQL;
import bdd.PompierMySQL;
import bean.Parametre;
import bean.Pompier;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import util.CtrlSaisie;
import util.MD5;

/**
 *
 * @author domin
 */
public class AuthentifForm {
    String message;
    HttpServletRequest request;

    public AuthentifForm(HttpServletRequest request) {
        this.request = request;
    }
    
    public boolean ctrlAuthentif() {
        request.removeAttribute("message");
        // Vérification de la présence des zones obligatoires
        String[] champsObligatoires = {"ztLogin", "ztMDP"};
        if (!CtrlSaisie.CtrlChampsObligatoires(request, champsObligatoires)) {
            return false;
        }
        
        // Vérification absence de chevron
        if (!CtrlSaisie.CtrlChevron(request)) {
            return false;
        }
        return true;
    }
    
    public boolean ctrlBDD() {
        boolean trouve = false;
        String login = request.getParameter("ztLogin");
        String mdp = MD5.encode(request.getParameter("ztMDP"));
        PompierMySQL pompierMySQL = new PompierMySQL();
        Pompier lePompier = pompierMySQL.readAuthentif(login, mdp);
        if (lePompier!= null) {
            trouve = true;
            HttpSession maSession = request.getSession();
            maSession.setAttribute("lePompierConnecte", lePompier);
            ParamMySQL paramMySQL = new ParamMySQL();
            ArrayList <Parametre> lesGrades = paramMySQL.readType("grade");
            maSession.setAttribute("lesGrades", lesGrades);
            ArrayList <Parametre> lesPeriodes = paramMySQL.readType("tranche");
            maSession.setAttribute("lesPeriodes", lesPeriodes);
            ArrayList <Parametre> lesStatuts = paramMySQL.readType("statAgt");
            maSession.setAttribute("lesStatuts", lesStatuts);
            ArrayList <Parametre> lesTypes = paramMySQL.readType("typePer");
            maSession.setAttribute("lesTypes", lesTypes);
            if (lePompier.getLeStatut().getCode()==2) {
                // Recherche des pompiers de la caserne
                ArrayList <Pompier> lesPompiers = pompierMySQL.readLesPompiersCaserne(lePompier.getLaCaserne().getId());
                maSession.setAttribute("lesPompiers", lesPompiers);               
            }
        } else {
            request.setAttribute("message", "Login ou mot de passe erroné");
        }
        
        return trouve;
    }
}
