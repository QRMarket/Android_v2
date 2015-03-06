/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qr_market.resources;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Kemal Sami KARACA
 */
public class ResourceProperty{
    
    
    private String propertyName;                // kullanılacak propery dosyasının ismi
    private static ResourceBundle rsBundle;     
    
    // **********************
    // CONSTRUCTOR
    // **********************
    public ResourceProperty(){     
        this.propertyName = "com.resources.dbProperties";
        rsBundle = ResourceBundle.getBundle("com.resources.dbProperties");        
    }
    
    public ResourceProperty(String propertyName){    
        this.propertyName = propertyName;
        rsBundle = ResourceBundle.getBundle(propertyName);        
    }

    
    // **********************
    // ENCAPSULATION METHODS
    // **********************
    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @param propertyName the propertyName to set
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    
    // **********************
    // GET METHODS
    // **********************     
    public String getPropertyValue(String key){
        return rsBundle.getString(key);
    }
    
    public List<String> getPropertyKeys(){
        Enumeration<String> rsKeys = rsBundle.getKeys();
        List<String> keyList = new ArrayList();
        while (rsKeys.hasMoreElements()) {            
            keyList.add((String)rsKeys.nextElement());                        
        }
        
        return keyList;
    }
    
            
    // **********************
    // GET SOME OF PROPERTIES
    // **********************
    public String getMongoDBName(){
        return rsBundle.getString("mongoDBName");
    }
    
    public String getMongoDBCollection(){
        return rsBundle.getString("mongoDBCollection");
    }

    
        
    
    
    
}
