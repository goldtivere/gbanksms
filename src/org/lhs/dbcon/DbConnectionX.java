/**
 * @author Abobade Oludayo Michael
 * @email:pagims2003@yahoo.com , michael.abobade@lolikshouse.com
 * @version 1.0
 * @mobile 08065711043, 08077792196
 * @Date 2017-01-28
 */
package org.lhs.dbcon;

 
import com.sms.alert.LoadPPTfile;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
 

public class DbConnectionX implements Serializable {

    LoadPPTfile loadPPTfile = new LoadPPTfile();
    private String messangerOfTruth;
    private boolean testconnection;
  
    
    public Connection mySqlDBconnection() {

        
        
        try {

            if (!(loadPPTfile.isLoadPPtFile())) {
                setMessangerOfTruth("Cannot load configuration file...");
                setTestconnection(false);
                return null;
            }

            Properties ppt = loadPPTfile.getPptFile();
            String url = ppt.getProperty("mysql_url");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);

            setTestconnection(true);
            
            return con;

        } catch (Exception e) {

             
            setTestconnection(false);
            setMessangerOfTruth("Error from DbConnection.class " +  e.getMessage());
            return null;

        }

    }//end myConnection()

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

    /**
     * @return the testconnection
     */
    public boolean isTestconnection() {
        return testconnection;
    }

    /**
     * @param testconnection the testconnection to set
     */
    public void setTestconnection(boolean testconnection) {
        this.testconnection = testconnection;
    }

}
