/*
 * bean/Pompier.java
 */
package bean;
 
import java.util.Objects;

/**
 *
 * @author Dominique_2
 */
public class Pompier {
    private Caserne laCaserne;
    private int id;
    private String nom;
    private String prenom;
    private Parametre leStatut;
    private Parametre leType;
    private String mail;
    private String login;
    private String mdp;
    private String adresse;
    private String cp;
    private String ville;
    private String bip;
    private Parametre leGrade;
    private String commentaire;

    public Caserne getLaCaserne() {
        return laCaserne;
    }

    public void setLaCaserne(Caserne laCaserne) {
        this.laCaserne = laCaserne;
    }

    public Pompier(Caserne laCaserne, int id, String nom, String prenom, Parametre leStatut, Parametre leType, String mail, String login, String mdp, String adresse, String cp, String ville, String bip, Parametre leGrade, String commentaire) {
//        this.laCaserne = laCaserne;
//        this.pId = pId;
//        this.pNom = pNom;
//        this.pPrenom = pPrenom;
        this(laCaserne, id, nom, prenom);
        this.leStatut = leStatut;
        this.leType = leType;
        this.mail = mail;
        this.login = login;
        this.mdp = mdp;
        this.adresse = adresse;
        this.cp = cp;
        this.ville = ville;
        this.bip = bip;
        this.leGrade = leGrade;
        this.commentaire = commentaire;
    }

    public Pompier(Caserne laCaserne, int id, String nom, String prenom) {
        this.laCaserne = laCaserne;
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Parametre getLeStatut() {
        return leStatut;
    }

    public void setLeStatut(Parametre leStatut) {
        this.leStatut = leStatut;
    }

    public Parametre getLeType() {
        return leType;
    }

    public void setLeType(Parametre leType) {
        this.leType = leType;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getBip() {
        return bip;
    }

    public void setBip(String bip) {
        this.bip = bip;
    }

    public Parametre getLeGrade() {
        return leGrade;
    }

    public void setLeGrade(Parametre leGrade) {
        this.leGrade = leGrade;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.laCaserne);
        hash = 67 * hash + this.id;
        hash = 67 * hash + Objects.hashCode(this.nom);
        hash = 67 * hash + Objects.hashCode(this.prenom);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pompier other = (Pompier) obj;
        if (this.id != other.id) {
            return false;
        }
        
        if (!Objects.equals(this.laCaserne, other.laCaserne)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pompier{" + "laCaserne=" + laCaserne + ", id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", leStatut=" + leStatut + ", leType=" + leType + ", mail=" + mail + ", login=" + login + ", mdp=" + mdp + ", adresse=" + adresse + ", cp=" + cp + ", ville=" + ville + ", bip=" + bip + ", grade=" + leGrade + ", commentaire=" + commentaire + '}';
    }

    

    
    
    

    
    
    
    
    
}
