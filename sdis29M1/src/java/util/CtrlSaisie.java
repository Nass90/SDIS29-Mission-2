/*
 * util/CtrlSaisie.java
 */
package util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author domin
 */
public abstract class CtrlSaisie {
    /**
     * Vérifie la présence des champs obligatoires dans les paramètres transmis
     * @param request
     * @param champsObligatoires : tableau de chaine de caractères, avec le nom des champs obligatoires
     * @return booléen
     */
    public static boolean CtrlChampsObligatoires(HttpServletRequest request, String[] champsObligatoires) {
        ArrayList <String> champsPresents = new ArrayList<>(request.getParameterMap().keySet());
        int i = 0;
        int nbChampsO = champsObligatoires.length;
        boolean erreur = false;
        while (erreur == false && i < nbChampsO && champsPresents.contains(champsObligatoires[i])){
            String value=request.getParameter(champsObligatoires[i]);  
            if (value.trim().length() > 0) {
               i++; 
            } else {
                erreur = true;
            }            
        }
        if (i < nbChampsO) {
            request.setAttribute("message", "Veuillez renseigner toutes les zones obligatoires ("+i+')');
        } 
        return (i>=nbChampsO);
    }
    
    /**
     * Contrôle si un champ de saisie contient un chevron
     * @param request
     * @return 0 sinon, -1 si oui
     */
    public static boolean CtrlChevron(HttpServletRequest request) {
        // Contrôle car "<" dans les zones de texte
        Enumeration lesNoms = request.getParameterNames(); 
        int erreur = 0; 
        //ArrayList <String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
        while (erreur == 0 && lesNoms.hasMoreElements()) {
            Object paramObjet=lesNoms.nextElement();
            String param=(String) paramObjet;
            String value=request.getParameter(param);            
            if (value.contains("<") || value.contains("<")) {
                String message = "Veuillez recommencer votre saisie, une anomalie sur une zone de saisie a été détectée ";
                request.setAttribute("message", message);
                erreur=1;                    
            }            
        }     
        return (erreur > 0) ? false : true;
        
    } 
    
}
