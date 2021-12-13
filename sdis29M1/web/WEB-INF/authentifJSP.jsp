<%-- 
    Document   : authentifJSP
    Created on : 23 oct. 2021, 10:02:18
    Author     : domin
--%>
<%@include file= "jspf/debutJSP.jspf" %>
    <body>
        <c:set var="page" value="1" scope="request" />
        <%@include file= "jspf/header.jspf" %>
        <div class="container">
            
            <section id="contenuAffiche">
                <fieldset id="authentif">
                    <legend>
                        Authentification
                    </legend>
                    <form id="fAuthentif" method="POST" action="authentification">
                        <div class="mb-3 mt-3">
                            <label for="ztLogin"> Login * </label>
                            <input type="text" placeholder="login" id="ztLogin" 
                                   autocomplete="username" name="ztLogin" class="form-control" required />
                        </div>
                        <div class="mb-3">
                            <label for="ztMDP"> Mot de passe  * </label>
                            <input type="password" placeholder="mdp" id="ztMDP" 
                                   name="ztMDP" class="form-control" required />
                        </div>
                        <div class="text-center">
                            <button type="submit" value="Valider" class="btn btn-danger"><i class="bi bi-check2"></i> Valider</button>                                    
                        </div>
                    </form> 
                    ${message}
                </fieldset>
            </section> <!-- /section id contenuAffiche -->
            <%@include file= "jspf/footer.jspf" %>
        </div> <!-- /div class container -->  

        <!-- Bootstrap Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>    
    </body>
</html>
