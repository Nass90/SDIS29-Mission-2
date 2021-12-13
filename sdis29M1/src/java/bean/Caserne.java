/*
 * bean/Caserne.java
 */
package bean;

import java.util.Objects;

/**
 *
 * @author Dominique_2
 */
public class Caserne {
    private int id;
    private String nom;
    private String adresse;
    private String tel;

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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    

    public Caserne(int cId, String cNom, String cAdresse, String cTel) {
        this.id = cId;
        this.nom = cNom;
        this.adresse = cAdresse;
        this.tel = cTel;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.id;
        hash = 17 * hash + Objects.hashCode(this.nom);
        hash = 17 * hash + Objects.hashCode(this.adresse);
        hash = 17 * hash + Objects.hashCode(this.tel);
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
        final Caserne other = (Caserne) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.adresse, other.adresse)) {
            return false;
        }
        if (!Objects.equals(this.tel, other.tel)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Caserne{" + "id=" + id + ", nom=" + nom + ", adresse=" + adresse + ", tel=" + tel + '}';
    }
    
}
