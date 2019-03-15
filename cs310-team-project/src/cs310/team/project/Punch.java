package cs310.team.project;

import java.util.*;
import java.text.SimpleDateFormat;

public class Punch {

    public Punch(){}
    
    private Badge badge;
    private String BadgeId;
    private int terminalId;
    private int punchId;
    private int eventtypeid;
    private int shiftId;
    private int punchType;
    private long originalTS;
    private long adjustedTS;
    

    public Punch(Badge badge, int terminalId,int punchId) {

    Gregoriancalendar Timestamp = new GregorianCalendar();
    this.terminalId = terminalId;
    this.badge = badge;
    this.punchId = punchId;
    
    }
    public Punch(Badge badge, int terminalId, int punchId, String BadgeId, long originalTS, int punchType) {

    Gregoriancalendar Timestamp = new GregorianCalendar();
    this.badge = badge;
    this.terminalId = terminalId;
    this.punchId = punchId;
    this.BadgeId = BadgeId;
    this.originalTS = originalTS;
    this.punchType = punchType;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public void setPunchId(int punchId) {
        this.punchId = punchId;
    }

    public void setPunchType(int punchType) {
        this.punchType = punchType;
    }
    
    public void setEventtypeid(int eventtypeid) {
        this.eventtypeid = eventtypeid;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }


    public void setOriginalTS(long originalTS) {
        this.originalTS = originalTS;
    }

    public void setAdjustedTS(long adjustedTS) {
        this.adjustedTS = adjustedTS;
    }

    public int getPunchId() {
        return punchId;
    }
    
    public String getBadgeId() {
        return BadgeId;
    }
    
    public int getPunchType() {
        return punchType;
    }
    public int getEventtypeid() {
        return eventtypeid;
    }

    public long getAdjustedTS() {
        return adjustedTS.getTimeInMillis();
    }

    public long getOriginalTS() {
        return originalTS.getTimeInMillis();
    }

    public Badge getBadge() {

        return badge;
    }

    public int getTerminalId() {

        return terminalId;
    }

    public int getShiftId() {

        return shiftId;
    }

    public long getOriginalTimeStamp() {

        return originalTS.getTimeInMillis();

    }
    public String printOriginalTimestamp() {

        String Status = "";

        if (punchType == 1) {
            Status = " CLOCKED IN: ";

        } 

        else if (punchType == 0) {
            Status = " CLOCKED OUT: ";

        } 

        else {
            Status = " TIMED OUT: ";
        }
        String formattedTime = new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(originalTS.getTime()).toUpperCase();
        return "#" + badge.getId() + Status + formattedTime;
    }
}