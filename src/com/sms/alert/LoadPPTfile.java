/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sms.alert;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
 
 

/**
 *
 * @author: micheal abobade
 * @email: pagims2003@yahoo.com
 * @mobile: 234-8065-711-043
 * @date: 2016-07-31
 */
public class LoadPPTfile implements Serializable{

    private Properties pptFile;
    private String messangerOfTruth; 

    public boolean isLoadPPtFile()throws Exception {

        
        //
        InputStream inp = null;
        String resource_file = "\\resources\\config.properties";
        Properties dPPT = null;

        try {

            dPPT = new Properties();
            inp = getClass().getClassLoader().getResourceAsStream(resource_file);
            dPPT.load(inp);

            setPptFile(dPPT);
            
             
            return true;

        } catch (Exception ex) {

            setPptFile(null);
            ex.printStackTrace();
            setMessangerOfTruth(ex.getMessage());
             
            return false;

        }finally{
            
            if(!(inp== null)){
                inp.close();
            }
        }

    }//end isLoadPPtFile()

    /**
     * @return the pptFile
     */
    public Properties getPptFile() {
        return pptFile;
    }

    /**
     * @param pptFile the pptFile to set
     */
    public void setPptFile(Properties pptFile) {
        this.pptFile = pptFile;
    }

    /**
     * @return the messangerOfTruth
     */
    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    /**
     * @param messangerOfTruth the messangerOfTruth to set
     */
    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

}
