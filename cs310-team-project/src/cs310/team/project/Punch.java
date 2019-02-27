package cs310.team.project;

import java.util.*;

public class Punch {
    private int shiftId = 0;
    private String punchId = 0;
    private Badge badge;
    private int terminalId;
    private GregorianCalendar originalTS;
    private GregorianCalendar adjustedTS = null;
    private int eventtypeid = 0;
    private int punchTypeId;

    public Punch(Badge badge, int terminalId,int punchTypeId) {

    originalTS = new GregorianCalendar();
    adjustedTS = null;
    this.terminalId = terminalId;
    this.badge = badge;
    this.punchTypeId = punchTypeId;

    }

    public String getBadge() {

        return badgeId;
    }

    public int getTerminalId() {

        return terminalId;
    }

    public int getShiftId() {

        return shiftId;
    }

    public GregorianCalendar getOriginalTimeStamp() {

        return originalTS;

    }
    public String printOriginalTimestamp() {

        String Status = "";

        if (eventtypeid == 1) {
            Status = " CLOCKED IN: ";

        } 

        else if (eventtypeid == 0) {
            Status = " CLOCKED OUT: ";

        } 

        else {
            Status = " TIMED OUT: ";
        }

        return "#" + badgeId + Status;
    }
 //Code to be implemented later is below...
/*
    public GregorianCalendar getAdjustedTimeStamp() {

        return adjusted;
    }

    public int geteventtypeid(){

        return eventtypeid;
    }

    public void setPunchId(String punchId) {

        this.punchId = punchId;
    }

    public String getPunchId() {
    
        return punchId;
    }
*/

}
