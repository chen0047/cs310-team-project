package cs310.team.project;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Absenteeism {
    
    private String badgeId;
    private long payTS;
    private double percentage;
    
    
    public Absenteeism(String badgeId, long payTS, double percentage){
        this.payTS = payTS;
        this.percentage = percentage;       
        this.badgeId = badgeId;

    }

    public String getBadgeId() {        //needed helps
        return badgeId;
    }

    public long getpayTS() {
        return payTS;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public void setPayTS(long payTS) {
        this.payTS = payTS;
    }

    
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        GregorianCalendar payTsLong = new GregorianCalendar(); 
        payTsLong.setTimeInMillis(payTS);
        String formattedTime = new SimpleDateFormat("MM-dd-YYYY").format(payTsLong.getTime()).toUpperCase();
        return "#" + badgeId + " (Pay Period Starting " + formattedTime + ") :" + percentage + '%';
    }
    
    
    
}
