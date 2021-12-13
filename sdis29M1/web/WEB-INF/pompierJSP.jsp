<%-- 
    pompierJSP.jsp
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file= "jspf/debutJSP.jspf" %>
<body>
    
    <%@include file= "jspf/header.jspf" %>
    <div class="container">
        <section id="contenuAffiche" class="row">
            <c:set var="legend" value="Mes données" scope="page" />
            <c:if test="${sessionScope.lePompierConnecte.getLeStatut().getCode() eq 2}" var="testChef" scope="page">
                <c:if test="${sessionScope.lePompierConnecte.getId() ne lePompier.getId()}" var="testAffich" scope="page">
                    <c:set var="legend" scope="page">
                            Données du pompier ${lePompier.getNom()} ${lePompier.getPrenom()}
                    </c:set>
                    <c:if test="${sessionScope.lePompier==null}" var="testCreation" scope="page">
                        <c:set var="legend" scope="page">
                            Données du pompier à créer
                        </c:set>
                    </c:if>         
                    <fieldset class="row mb-10 text-center">  
                        <%@include file= "jspf/formChoixPompier.jspf" %>  
                    </fieldset> 
                </c:if>                             
            </c:if>

            <fieldset class="row mt-1 mb-10">  
                <legend>                    
                    ${legend}              
                </legend>               

                <%@include file= "jspf/formulairePompier.jspf" %>
            </fieldset> 
        </section>

        <%@include file= "jspf/footer.jspf" %>
    </div> <!-- /div class container -->  

    <!-- Bootstrap Bundle with Popper   -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>

    <!-- Code postaux -->
    <script src="js/ctrlCodePostal.js"></script>
</body>
</html>




