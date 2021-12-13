/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import bean.Pompier;
import form.AuthentifForm;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 *
 * @author domin
 */
public class AuthentifServlet extends HttpServlet {

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
            out.println("<title>Servlet AuthentifServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AuthentifServlet at " + request.getContextPath() + "</h1>");
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
        // Suppression des variables de session
        System.out.println("doGet AuthentifServlet");
        HttpSession maSession = request.getSession();
        maSession.removeAttribute("lePompierConnecte");
        maSession.removeAttribute("lesPompiers");
        maSession.removeAttribute("lePompier");
        getServletContext().getRequestDispatcher("/WEB-INF/authentifJSP.jsp").forward(request, response);
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
        Pompier lePompier = null;
        int page = 2;
        AuthentifForm af = new AuthentifForm(request);
        if (af.ctrlAuthentif() && af.ctrlBDD()) {   // Contrôle des zones saisies et contrôle en base de données
            HttpSession maSession = request.getSession();
            lePompier = (Pompier) maSession.getAttribute("lePompierConnecte");
            if (lePompier.getLeStatut().getCode()==2) { // Chef de centre
                ArrayList <Pompier> lesPompiers = (ArrayList <Pompier>) maSession.getAttribute("lesPompiers");
                if (lesPompiers.size() > 0) {
                    lePompier = lesPompiers.get(0);
                    page = 3;
                }
            }  
            request.setAttribute("page", page);
            //request.setAttribute("lePompier", lePompier);
            maSession.setAttribute("lePompier", lePompier);
            getServletContext().getRequestDispatcher("/WEB-INF/pompierJSP.jsp").forward(request, response);            
        } else { // Une erreur a été détectée
            getServletContext().getRequestDispatcher("/WEB-INF/authentifJSP.jsp").forward(request, response);
        }
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
