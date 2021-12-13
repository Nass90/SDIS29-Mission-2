/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author sio
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class  MD5 {
    
    public static String encode(String uneChaine){
        MessageDigest md = null;        
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(ex);
        }
        md.update(uneChaine.getBytes());
        byte[] digest = md.digest();
        //myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
