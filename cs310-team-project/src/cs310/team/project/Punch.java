package cs310.team.project;

import java.util.*;
import java.text.SimpleDateFormat;

public class Punch {

    public Punch(){}
    
    private Badge badge;
    private String BadgeId;
    private int terminalId;
    private int punchId;
    private int eventtypeid = 0;
    private int shiftId = 0;
    private int punchType = 0;
    private GregorianCalendar originalTS;
    private GregorianCalendar adjustedTS = null;
    private int clockedIn, clockedOut = 0;
    private String trigger = "";

    

    public Punch(Badge badge, int terminalId,int punchTypeId) {

    originalTS = new GregorianCalendar();
    this.terminalId = terminalId;
    this.badge = badge;
    this.punchId = punchTypeId;
    
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


    public void setOriginalTS(GregorianCalendar originalTS) {
        this.originalTS = originalTS;
    }

    public void setAdjustedTS(GregorianCalendar adjustedTS) {
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

    public GregorianCalendar getAdjustedTS() {
        return adjustedTS;
    }

    public GregorianCalendar getOriginalTS() {
        return originalTS;
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

    public GregorianCalendar getOriginalTimeStamp() {

        return originalTS;

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
    
    public void adjust(Shift s)
    {
        adjustedTS = originalTS;
        
        
        if(punchType == 1 && clockedIn == 0)
        {
            if(originalTS.before(s.getStart()) && originalTS.after(s.getStart().plusMinutes(-s.getInterval())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftStartHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftStartMinute());
                adjustedTS.set(originalTS.SECOND, 0);
                trigger = "(Interval Round)";
            }
            else if(originalTS.before(s.getStart()) && originalTS.after(s.getStart().plusMinutes(s.getGraceperiod())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftStartHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftStartMinute());
                adjustedTS.set(originalTS.SECOND, 0);
                trigger = "(Shift Start)";
            }
            else if(originalTS.after(s.getStart().plusMinutes(s.getGraceperiod())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftStartHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftStartMinute());
                adjustedTS.set(originalTS.SECOND, 0);
                trigger = "(Shift Dock)";
            }
            clockedIn = 1;
        }
        
        else if(punchType == 0 && clockedOut == 0)
        {
            if(originalTS.before(s.getLunchstart()) && originalTS.after(s.getLunchstart().plusMinutes(-s.getInterval())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftLunchStartHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftLunchStartMinute());
                adjustedTS.set(originalTS.SECOND, 0);
                trigger = "(Interval Round)";
            }
            else if(originalTS.before(s.getLunchstart()) && originalTS.after(s.getLunchstart().plusMinutes(s.getGraceperiod())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftLunchStartHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftLunchStartMinute());
                adjustedTS.set(originalTS.SECOND, 0);
            }
            else if(originalTS.after(s.getLunchstart().plusMinutes(s.getGraceperiod())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftLunchStartHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftLunchStartMinute());
                adjustedTS.set(originalTS.SECOND, 0);
            }
            clockedOut = 1;
        }
        
        else if(punchType == 1 && clockedIn == 1)
        {
            if(originalTS.before(s.getLunchstop()) && originalTS.after(s.getLunchstop().plusMinutes(-s.getInterval())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftLunchStopHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftLunchStopMinute());
                adjustedTS.set(originalTS.SECOND, 0);
                trigger = "(Interval Round)";
            }
            else if(originalTS.after(s.getLunchstop()) && originalTS.before(s.getLunchstop().plusMinutes(s.getGraceperiod())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftLunchStopHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftLunchStopMinute());
                adjustedTS.set(originalTS.SECOND, 0);
            }
            else if(originalTS.after(s.getLunchstop().plusMinutes(s.getGraceperiod())) && originalTS.before(s.getLunchstop().plusMinutes(s.getInterval())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftLunchStopHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftLunchStopMinute() + s.getDock());
                adjustedTS.set(originalTS.SECOND, 0);
            }
            clockedIn = 0;
        }
        
        else if(punchType == 0 && clockedOut == 1)
        {
            if(originalTS.before(s.getStop()) && originalTS.after(s.getStop().plusMinutes(s.getInterval())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftStopHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftStopMinute());
                adjustedTS.set(originalTS.SECOND, 0);
            }
            else if(originalTS.after(s.getStop()) && originalTS.before(s.getStop().plusMinutes(-s.getGraceperiod())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftStopHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftStopMinute());
                adjustedTS.set(originalTS.SECOND, 0);
            }
            else if(originalTS.after(s.getLunchstop().plusMinutes(s.getGraceperiod())) && originalTS.before(s.getLunchstop().plusMinutes(s.getInterval())))
            {
                adjustedTS.set(originalTS.HOUR, s.getShiftStopHour());
                adjustedTS.set(originalTS.MINUTE, s.getShiftStopMinute() - s.getDock());
                adjustedTS.set(originalTS.SECOND, 0);
            }
            clockedOut = 0;
        }
    }
    
    public String printAdjustedTimestamp() {

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
        String formattedTime = new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(adjustedTS.getTime()).toUpperCase();
        return "#" + badge.getId() + Status + formattedTime;
    }

}