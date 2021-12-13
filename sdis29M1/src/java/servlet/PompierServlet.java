/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import bean.Pompier;
import com.mysql.cj.util.StringUtils;
import form.PompierForm;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author domin
 */
public class PompierServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PompierServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PompierServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        //Méthode accédée uniquement depuis la barre du menu
        int pageDemandee = 0;
        int page = 2;
        String jsp = "pompierJSP.jsp";

        boolean erreur = false;
        if (request.getParameter("pageDemandee") != null) {
            String pageEnString = request.getParameter("pageDemandee");
            boolean isNumeric = StringUtils.isStrictlyNumeric(pageEnString);
            if (isNumeric) {
                pageDemandee = Integer.parseInt(pageEnString);
                if (pageDemandee != 2 && pageDemandee != 3) { // erreur
                    erreur = true;
                }
            } else {
                erreur = true;
            }
        } else {
            erreur = true;
        }

        HttpSession maSession = request.getSession();
        Pompier lePompier = null;
        if (erreur == false) {
            lePompier = (Pompier) maSession.getAttribute("lePompierConnecte");
            if (pageDemandee == 3 && lePompier.getLeStatut().getCode() != 2) { // erreur
                erreur = true;
            }
        }

        if (erreur == false && pageDemandee == 3) { // Chef de centre
            ArrayList<Pompier> lesPompiers = (ArrayList<Pompier>) maSession.getAttribute("lesPompiers");
            //System.out.println("lesPompiers de la caserne " + lesPompiers);
            if (lesPompiers.size() > 0) {
                lePompier = lesPompiers.get(0);
                page = 3;
            }
        }
        if (erreur) {
            request.getRequestDispatcher("/authentification").forward(request, response); // redirection servlet
        } else {
            request.setAttribute("page", page);
            maSession.setAttribute("lePompier", lePompier);
            getServletContext().getRequestDispatcher("/WEB-INF/" + jsp).forward(request, response);            
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        /* Méthode accédée 
                depuis la validation du formulaire de choix du pompier à afficher (btAfficherPompier = "Afficher")
                depuis le bouton Ajouter du formulaire du choix du pompier (btAfficherPompier = "Ajouter")
                depuis le bouton de validation de mise à jour d'un pompier (btAfficherPompier = "ValiderMaj")
        */
        int erreur = 0;
        String btValue = null;
        ArrayList<String> valeursBt =  new ArrayList<String>() ;
        valeursBt.add("Afficher") ;
        valeursBt.add("Ajouter") ;
        valeursBt.add("ValiderMaj") ;
        
        // Test du bouton btAfficherPompier
        if (request.getParameter("btAfficherPompier")==null) {
            erreur = 1;
        } else {
            btValue = request.getParameter("btAfficherPompier");
            if (!valeursBt.contains(btValue)) {
                erreur = 2;
            }
        }
        if (erreur > 0) {
            request.getRequestDispatcher("/authentification").forward(request, response); // redirection servlet
            //response.sendRedirect("/sdis29M1/authentification");
            return;
        }
                
        HttpSession maSession = request.getSession();
        ArrayList<Pompier> lesPompiers = (ArrayList<Pompier>) maSession.getAttribute("lesPompiers");
        if (request.getParameter("btAfficherPompier").equals("Afficher")
                && Integer.parseInt(request.getParameter("ldrPompier")) < lesPompiers.size()) {
            int noPompier = Integer.parseInt(request.getParameter("ldrPompier"));
            //request.setAttribute("lePompier", lesPompiers.get(noPompier));
            maSession.setAttribute("lePompier", lesPompiers.get(noPompier));
            request.setAttribute("page", 3);
        }
        
        if (request.getParameter("btAfficherPompier").equals("Ajouter")) {
            maSession.removeAttribute("lePompier");
        }
                
        if (request.getParameter("btAfficherPompier").equals("ValiderMaj")) { 
            PompierForm pf = new PompierForm(request);
            boolean valid = pf.ctrlPompier();
//            if (maSession.getAttribute("lePompier")==null) {
//                System.out.println("Creation d'un nouveau pompier");
//            } else {
//                Pompier lePompier = (Pompier) maSession.getAttribute("lePompier");
//                Pompier lePompierConnecte = (Pompier) maSession.getAttribute("lePompierConnecte");
//                if (lePompier.getId() != lePompierConnecte.getId()) {
//                    System.out.println("Maj d'un pompier par le chef de centre");
//                } else {
//                    System.out.println("Maj du pompier connecté par lui-même");
//                }
//            }
            pf.majBDD();
        }    
        
        getServletContext().getRequestDispatcher("/WEB-INF/pompierJSP.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
