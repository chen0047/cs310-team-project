package cs310.team.project;

import java.util.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

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
        adjustedTS = (GregorianCalendar) originalTS.clone();
        LocalTime time = LocalTime.of(originalTS.HOUR, originalTS.MINUTE, 0);
        
        
        if(punchType == 1 && clockedIn == 0)
        {
            if(time.isBefore(s.getStart()))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftStartHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftStartMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
                trigger = "(Shift Start)";
            }
            else if(time.isAfter(s.getStart()) && (time.isBefore(s.getStart().plusMinutes(s.getGraceperiod())) || time.equals(s.getStart().plusMinutes(s.getGraceperiod()))))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftStartHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftStartMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
                trigger = "(Shift Start)";
            }
            else if(time.isAfter(s.getStart().plusMinutes(s.getGraceperiod())))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftStartHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftStartMinute() + s.getDock());
                adjustedTS.set(adjustedTS.SECOND, 0);
                trigger = "(Shift Dock)";
            }
            clockedIn = 1;
        }
        
        else if(punchType == 0 && clockedOut == 0)
        {
            if(time.isBefore(s.getLunchstart()))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftLunchStartHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStartMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
                trigger = "(Interval Round)";
            }
            else if(time.isAfter(s.getLunchstart()) && (time.isBefore(s.getLunchstart().plusMinutes(s.getGraceperiod())) || time.equals(s.getLunchstart().plusMinutes(s.getGraceperiod()))))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftLunchStartHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStartMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
            }
            else if(time.isAfter(s.getLunchstart().plusMinutes(s.getGraceperiod())))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftLunchStartHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStartMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
            }
            clockedOut = 1;
        }
        
        else if(punchType == 1 && clockedIn == 1)
        {
            if(time.isBefore(s.getLunchstop()))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftLunchStopHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStopMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
                trigger = "(Interval Round)";
            }
            else if(time.isAfter(s.getLunchstop()) && (time.isBefore(s.getLunchstop().plusMinutes(s.getGraceperiod())) || time.equals(s.getLunchstop().plusMinutes(s.getGraceperiod()))))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftLunchStopHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStopMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
            }
            else if(time.isAfter(s.getLunchstop().plusMinutes(s.getGraceperiod())))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftLunchStopHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStopMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
            }
            clockedIn = 0;
        }
        
        else if(punchType == 0 && clockedOut == 1)
        {
            if(time.isAfter(s.getStop()))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftStopHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftStopMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
            }
            else if(time.isBefore(s.getStop()) && (time.isAfter(s.getStop().minusMinutes(s.getGraceperiod())) || time.equals(s.getStop().minusMinutes(s.getGraceperiod()))))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftStopHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftStopMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
            }
            else if(time.isBefore(s.getStop().minusMinutes(s.getGraceperiod())))
            {
                adjustedTS.set(adjustedTS.HOUR, s.getShiftStopHour());
                adjustedTS.set(adjustedTS.MINUTE, s.getShiftStopMinute());
                adjustedTS.set(adjustedTS.SECOND, 0);
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
        return "#" + badge.getId() + Status + formattedTime + " " + trigger;
    }

}