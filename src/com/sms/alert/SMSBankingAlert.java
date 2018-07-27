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
import org.lhs.dbcon.DateManipulation;
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
                    + "where status=false and statuscode is null";
            //
            pstmt = con.prepareStatement(querySMSDetails);
            rs = pstmt.executeQuery();

            //
            String _val = null;
            if (rs.next()) {

                String value = rs.getString("body");
                _val = value.replace(" ", "%20");
                _val = _val.replace(",", "%2C");
                _val = _val.replace(":", "%3A");
                _val = _val.replace(";", "%3B");
                _val = _val.replace("'", "%27");
                _val = _val.replace("(", "%28");
                _val = _val.replace(")", "%29");
                _val = _val.replace("#", "%23");
                messageModel.setBody(_val);
                messageModel.setPnum(rs.getString("phonenumbers"));
                messageModel.setDateSent(rs.getString("datesent"));
                messageModel.setStatus(true);
                messageModel.setId(rs.getInt("id"));
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

    public void updateSmsTable(String statusCode, String description, int id) {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dbConnections.mySqlDBconnection();

            String updateSmsTable = "update smstable set status=?,statuscode=?,statusdescription=?,datemessagesent=?,datesenttime=? where id=?";
            pstmt = con.prepareStatement(updateSmsTable);
            pstmt.setBoolean(1, true);
            pstmt.setString(2, statusCode);
            pstmt.setString(3, description);
            pstmt.setString(4, DateManipulation.dateAlone());
            pstmt.setString(5, DateManipulation.dateAndTime());
            pstmt.setInt(6, id);
            pstmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
                String val = null;
                String sender = "GOTIT";
                URL url = new URL("http://www.bulksmslive.com/tools/geturl/Sms.php?username=goldtive@gmail.com&password=GoldTivere94&sender=" + sender + "&message=" + messageModel.getBody() + "&flash=1&sendtime=" + messageModel.getDateSent() + "&listname=friends&recipients=" + messageModel.getPnum());
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
                String responseCod = response.toString();

                if (responseCod.equalsIgnoreCase("-1")) {
                    val = "Incorrect / badly formed URL data";
                } else if (responseCod.equalsIgnoreCase("-2")) {
                    val = "Incorrect username and/or password";
                } else if (responseCod.equalsIgnoreCase("-3")) {
                    val = "Not enough credit units in user account";
                } else if (responseCod.equalsIgnoreCase("-4")) {
                    val = "Invalid sender name";
                } else if (responseCod.equalsIgnoreCase("-5")) {
                    val = "No valid recipient ";
                } else if (responseCod.equalsIgnoreCase("-6")) {
                    val = "Invalid message length/No message content";
                } else if (responseCod.equalsIgnoreCase("-10")) {
                    val = "Unknown/Unspecified error";
                } else if (responseCod.equalsIgnoreCase("100")) {
                    val = "Send successful";
                }

                updateSmsTable(response.toString(), val, messageModel.getId());
                System.out.println("ID: " + messageModel.getId() + " sent. Message: " + messageModel.getBody() + " Code" + responseCod);
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
