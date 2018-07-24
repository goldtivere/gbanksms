/*
 * SMSBankingAlert.java
 *
 * Created on May 6, 2009, 11:46 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.sms.alert;

/**
 *
 * @author Micheal
 */
import java.io.*;
import java.math.BigDecimal;

import java.net.*;
import java.util.*;
import java.sql.*;

import java.sql.Connection;

import org.lhs.dbcon.DbConnectionX;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;

/**
 * This program connects to a URL and displays the response header data and the
 * first 10 lines of the requested data. Supply the URL and an optional username
 * and password (for HTTP basic authentication) on the command line.
 */
public class SMSBankingAlert implements Runnable {

    int i = 0;
    int a = 0;

    //values to pass...
    private String msgToPass;

    private String messangerOfTruth;

    //do database computations....
    //table to use...(sms_alert_logs,
    public MessageModel doTransaction() throws Exception {

        Connection con = null;
        DbConnectionX dbCon = new DbConnectionX();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sms_url;

        MessageModel messageModel = new MessageModel();

        try {

            con = dbCon.mySqlDBconnection();

            String querySMSDetails = "select * from smstable "
                    + "where status=false and statuscode=null";
            //
            pstmt = con.prepareStatement(querySMSDetails);
            rs = pstmt.executeQuery();

            //
            
            if (rs.next()) {

                String value=rs.getString("body");
                String _val = value.replace(" ", "%20");
                messageModel.setBody(_val);
                messageModel.setPnum(rs.getString("phonenumbers"));
                messageModel.setDateSent(rs.getString("datesent"));
                messageModel.setStatus(true);
                return messageModel;

            } else {

                messageModel.setStatus(false);
                messageModel.setStatus_msg("no record");

                return messageModel;

            }

        } catch (Exception e) {

            System.out.print("Exception from doTransaction method.....");

            messageModel.setStatus(false);
            messageModel.setStatus_msg("Error:" + e.getMessage());

            return messageModel;

        } finally {

            if (!(con == null)) {
                con.close();
            }

            if (!(pstmt == null)) {
                pstmt.close();
            }

            if (!(rs == null)) {
                rs.close();
            }

        }

    }//end doTransaction...

    public void run() {

        int i = 0;

        outer:

        while (true) {

            try {

                MessageModel messageModel = doTransaction();
                if (messageModel.getStatus() == false) {
                    System.out.println(messageModel.getStatus_msg());//error

                    continue outer;
                } else if (messageModel.getStatus() == false) {
                    System.out.println(messageModel.getStatus_msg());//no record
                    continue outer;
                }
                String message = "HiRobor";
                String sender = "GOT IT";
                URL url = new URL("http://www.bulksmslive.com/tools/geturl/Sms.php?username=goldtive@gmail.com&password=GoldTivere94&sender=" + sender + "&message="+ message +"&flash=0&sendtime=" + messageModel.getDateSent() + "&listname=friends&recipients=" + messageModel.getPnum());
                //http://www.bulksmslive.com/tools/geturl/Sms.php?username=abc&password=xyz&sender="+sender+"&message="+message+"&flash=0&sendtime=2009-10- 18%2006:30&listname=friends&recipients="+recipient; 
                //URL gims_url = new URL("http://smshub.lubredsms.com/hub/xmlsmsapi/send?user=loliks&pass=GJP8wRTs&sender=nairabox&message=Acct%3A5073177777%20Amt%3ANGN1%2C200.00%20CR%20Desc%3ATesting%20alert%20Avail%20Bal%3ANGN%3A1%2C342%2C158.36&mobile=08065711040&flash=0");
                final String USER_AGENT = "Mozilla/5.0";

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", USER_AGENT);
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
               // System.out.println(messageModel.getBody() + " dude");
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println(response.toString() + " okayyyy");

                // in.close(); unremark
                //System.out.println("God is my Strength:" + i++  );
                //  System.out.println("The URL:" + gims_url);
                //doTransaction();
                Thread t = new Thread();
                t.sleep(20000);

            } catch (Exception e) {

                e.printStackTrace();
                System.out.println("IOException error.....");

            }

        }//end of while...

    }//end of run method...

    //this gets the account balance for both customer and sms_insterest_account
    public static void main(String[] args) {

        try {

            SMSBankingAlert smsBankingAlert = new SMSBankingAlert();

            //SMSBankingAlert smsBankingAlert = new SMSBankingAlert("Gims");
            Thread t = new Thread(smsBankingAlert);
            t.start();

        } catch (Exception e) {

            e.printStackTrace();

        }

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
