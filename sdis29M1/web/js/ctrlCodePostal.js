    // Recherche de la ou des villes correspondantes au code postal saisi (exemple 54490 --> 7 villes)
    ztCP.onchange = function () {
        //alert("ztCP a changé");
        msgCP.textContent = "";
        zlVille.length = 0;
        const XHTTP = new XMLHttpRequest();
        XHTTP.onload = function () {
            if (this.status != 200) {
                msgCP.textContent = "Code postal erroné";
            } else {
                var lesVilles = JSON.parse(this.responseText);
                    var info = "";
                    if (lesVilles.length > 1) {
                        info += "<optgroup label='Les villes du code postal' />";
                    }
                    for (var i = 0; i < lesVilles.length; i++) {
                        var uneVille = lesVilles[i];
                        info += "<option value='" + uneVille.libelleAcheminement + "'>";
                        info += uneVille.libelleAcheminement + '</option>';
                    }
                zlVille.innerHTML = info;
            }
        }
        var cp = ztCP.value;
        XHTTP.open("GET", "https://apicarto.ign.fr/api/codes-postaux/communes/" + cp);
        XHTTP.send();
    }

