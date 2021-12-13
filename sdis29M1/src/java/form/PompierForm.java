/*
 * form/PompierForm.java
 * Contrôle des données saisies dans le formulaire pompier et maj bdd
 */
package form;

import bdd.ParamMySQL;
import bdd.PompierMySQL;
import bean.Parametre;
import bean.Pompier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import util.CtrlSaisie;
import util.MD5;

/**
 *
 * @author domin
 */
public class PompierForm {

    String message;
    HttpServletRequest request;

    public PompierForm(HttpServletRequest request) {
        this.request = request;
    }

    public boolean ctrlPompier() {
        request.removeAttribute("message");
        // Vérification de la présence des zones obligatoires
        String[] champsObligatoires = {"ztNom", "ztPrenom", "ztAdresse", "ztCP",
            "zlVille", "ztMail", "ztLogin", "ztMdp", "ztBip", "ldrGrade", "ldrStatut", "ldrType"};
        if (!CtrlSaisie.CtrlChampsObligatoires(request, champsObligatoires)) {
            return false;
        }

        // Contrôle caractères indésirables
        if (!CtrlSaisie.CtrlChevron(request)) {
            return false;
        }

        // Contrôle des zones grade, statut et typerPers
        HttpSession maSession = request.getSession();
        Pompier lePompierConnecte = (Pompier) maSession.getAttribute("lePompierConnecte");
        boolean chef = lePompierConnecte.getLeStatut().getCode() == 2;
        ParamMySQL pm = new ParamMySQL();
        if (chef) { // contrôle uniquement pour les chefs de centre
            int statut, type, grade = -1;

            try {
                statut = Integer.parseInt(request.getParameter("ldrStatut"));
                type = Integer.parseInt(request.getParameter("ldrType"));
                grade = Integer.parseInt(request.getParameter("ldrGrade"));
            } catch (Exception e) {
                System.out.println("Statut, type ou grade transmis non numérique - " + e.getMessage());
                request.setAttribute("message", "Erreur au niveau de la saisie du statut, du grade ou du type");
                return false;
            }
            Parametre leStatut = pm.read("statAgt", statut);
            Parametre leType = pm.read("typePer", type);
            Parametre leGrade = pm.read("grade", grade);
            if (leStatut == null || leType == null || leGrade == null) {
                System.out.println("Statut, type ou grade transmis inexistant dans la table paramètre - "
                        + "Statut:" + statut + " - Type:" + type + " - Grade:" + grade);
                request.setAttribute("message", "Erreur au niveau de la saisie du statut, du grade ou du type");
                return false;
            }
        }
        return true;
    }

    // Mise à jour de la fiche du pompier
    public Pompier majBDD() {
        HttpSession maSession = request.getSession();
        Pompier lePompierConnecte = (Pompier) maSession.getAttribute("lePompierConnecte");
        boolean chef = lePompierConnecte.getLeStatut().getCode() == 2;
        Pompier lepompierAffiche = (Pompier) maSession.getAttribute("lePompier");
        
        // Recherche du type de mise à jour à effectuer
        int typMaj = -1; 
        if (chef && maSession.getAttribute("lePompier") == null) {
            System.out.println("Creation d'un nouveau pompier");
            typMaj = 1;
        } else {
            if (chef && lepompierAffiche.getId() != lePompierConnecte.getId()) {
                System.out.println("Maj d'un pompier par le chef de centre");
                typMaj = 2;
            } else {
                System.out.println("Maj du pompier connecté par lui-même");
                typMaj = 3;
            }
        }

        ArrayList<String> nonModifParPompier = new ArrayList<>();
        nonModifParPompier.add("ldrGrade");
        nonModifParPompier.add("ldrStatut");
        nonModifParPompier.add("ldrType");
        nonModifParPompier.add("ztBip");

        // Zones saisies
        HashMap<String, Object> lesParametres = new HashMap();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            // Exclusion des zones non modifiables pour les pompiers
            if (typMaj == 1 || typMaj == 2 || !nonModifParPompier.contains(name)) {
                Object value = request.getParameter(name);
                lesParametres.put(name, value);
            }
        }
        
        // Zones implicites
        if (typMaj == 1) {
            int idC = lePompierConnecte.getLaCaserne().getId();
            lesParametres.put("idC", idC);    // Caserne
        }
//        if (typMaj == 2 || typMaj == 3) {
//            int idP = lepompierAffiche.getId();
//            lesParametres.put("idP", idP);    // id du pompier
//        }   
          
        // Cas particulier du mot de passe, actuellement non modifiable
        String ztMdp = MD5.encode((String) lesParametres.get("ztLogin"));
        lesParametres.put("ztMdp", ztMdp); // Mot de passe défini à partir du login         
               
        /* Pb pour les zones numériques : 
                elles doivent être mémorisées dans le dictionnaire en entier, pour
                une lecture par la suite */
        
        Pompier lePompierMaj = null;
        PompierMySQL pompierMySQL = new PompierMySQL();
        if (typMaj == 1) {
             lePompierMaj = pompierMySQL.create(lesParametres);
         } else {
            lePompierMaj = pompierMySQL.update(lesParametres, lepompierAffiche.getId());
        }
         if (lePompierMaj != null) {
            request.setAttribute("message", "Le pompier no " + lePompierMaj.getId() + " a été mis à jour");
         } else {
            request.setAttribute("message", "Erreur lors de la mise à jour du pompier");
         }
       
        // Mise à jour de la collection les Pompiers, si le pompier mis à jour est volontaire
        if (chef && lePompierMaj != null) {
//            ArrayList<Pompier> lesPompiers = (ArrayList<Pompier>) maSession.getAttribute("lesPompiers");
//            if (lesPompiers.contains(lePompierMaj)) {
//                lesPompiers.remove(lePompierMaj);
//            }
//            if (lePompierConnecte.getId() != lePompierMaj.getId()) {
//                lesPompiers.add(lePompierMaj);
//            }
            ArrayList<Pompier> lesPompiers = pompierMySQL.readLesPompiersCaserne(lePompierConnecte.getLaCaserne().getId());
            
            maSession.setAttribute("lesPompiers", lesPompiers);  
            maSession.setAttribute("lePompier", lesPompiers.get(0));
                      
        }
        if (typMaj==3) {
            maSession.setAttribute("lePompierConnecte", lePompierMaj); 
            maSession.setAttribute("lePompier", lePompierMaj);
        }  
        return lePompierMaj;
    }
}
