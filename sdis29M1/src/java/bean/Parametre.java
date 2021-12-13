/*
 * bean/Parametre.java
 */
package bean;

/**
 *
 * @author Dominique_2
 */
public class Parametre {
    private String typParam;
    private int code;
    private String valeur;

    
    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the valeur
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * @param valeur the valeur to set
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Parametre(String typParam, int code, String valeur) {
        this.typParam = typParam;
        this.code = code;
        this.valeur = valeur;
    }

    /**
     * @return the typParam
     */
    public String getTypParam() {
        return typParam;
    }

    /**
     * @param typParam the typParam to set
     */
    public void setTypParam(String typParam) {
        this.typParam = typParam;
    }

    @Override
    public String toString() {
        return "Parametre{" + "typParam=" + typParam + ", code=" + code + ", valeur=" + valeur + '}';
    }
    
    
    
}
