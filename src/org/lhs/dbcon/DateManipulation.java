/**
 *
 * @author: micheal abobade
 * @email: pagims2003@yahoo.com
 * @mobile: 234-8065-711-043
 * @date: 2016-08-17
 */
package org.lhs.dbcon;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class DateManipulation {

    private int year;
    private int month;

    public static String dateAndTime() {

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;

    }

    public static String time() {

        String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
        return timeStamp;

    }

    public static String dateAlone() {

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        return timeStamp;

    }

    public boolean computeAge(String dateofbirthX) {
        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date _today = new Date();
            Date tdate = new Date(format.format(_today));

            //
            String fmr_date_of_birth = dateofbirthX.replace("-", "/");
            Date dob = new Date(fmr_date_of_birth);

            //compute my age...
            LocalDate birthdate = LocalDate.of(dob.getYear() + 1900, dob.getMonth() + 1, dob.getDay());
            LocalDate current_date = LocalDate.of(tdate.getYear() + 1900, tdate.getMonth() + 1, tdate.getDay());

            //LocalDate now = new LocalDate();
            Period period = Period.between(birthdate, current_date);
            setYear(period.getYears());
            setMonth(period.getMonths());

            return true;

        } catch (Exception ex) {

            return false;
        }

    }//end computeAge

    public static Date defineDate(String dateofbirthX) {
        try {

            
            Date dob = new Date(dateofbirthX);
            return dob;

        } catch (Exception ex) {

            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

}
