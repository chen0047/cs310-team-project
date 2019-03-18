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
    private int eventtypeid;
    private int shiftId;
    private int punchType;
    private GregorianCalendar originalTS;

    private GregorianCalendar adjustedTS;
    private long Timestamp;

    private String trigger = "";


    public Punch(Badge badge, int terminalId,int punchId) {

    originalTS = new GregorianCalendar();
    this.terminalId = terminalId;
    this.badge = badge;
    this.punchId = punchId;
    
    }
    public Punch(Badge badge, int terminalId, int punchId, String BadgeId, long Timestamp, int punchType) {

    originalTS = new GregorianCalendar();
    this.badge = badge;
    this.terminalId = terminalId;
    this.punchId = punchId;
    this.BadgeId = BadgeId;
    this.Timestamp = Timestamp;
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
    
    public void adjust(Shift s)
    {
        adjustedTS = (GregorianCalendar) originalTS.clone();
        LocalTime time = LocalTime.of(originalTS.get(originalTS.HOUR_OF_DAY), originalTS.get(originalTS.MINUTE), 0);
        int unroundedMinutes = originalTS.get(originalTS.MINUTE);
        int mod = unroundedMinutes % 15;
        int day = originalTS.get(originalTS.DAY_OF_WEEK);
        
        if(day != originalTS.SATURDAY && day != originalTS.SUNDAY)
        {
            if(punchType == 1)
            {
                if(time.isBefore(s.getStart()) && time.isAfter(s.getStart().minusMinutes(s.getInterval())))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStartHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStartMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "(Shift Start)";
                }
                else if(time.isBefore(s.getLunchstop()) && time.isAfter(s.getLunchstart()))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftLunchStopHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStopMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "(Lunch Stop)";   
                }
                else if(time.isAfter(s.getStart()) && (time.isBefore(s.getStart().plusMinutes(s.getGraceperiod())) || time.equals(s.getStart().plusMinutes(s.getGraceperiod()))))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStartHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStartMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "(Shift Start)";
                }
                else if(time.isAfter(s.getStart().plusMinutes(s.getGraceperiod())) && (time.isBefore(s.getStart().plusMinutes(s.getDock())) || time.equals(s.getStart().plusMinutes(s.getDock()))))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStartHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStartMinute() + s.getDock());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "(Shift Dock)";
                }
                else
                {
                    if(mod !=0)
                    {  
                        adjustedTS.add(Calendar.MINUTE, 15 - unroundedMinutes);
                        adjustedTS.set(adjustedTS.SECOND, 0);
                        trigger = "(Interval Round)";
                    }
                    else
                    {
                        adjustedTS.set(adjustedTS.SECOND, 0);
                        trigger = "(None)"; 
                    }
                }
            }
            else if(punchType == 0)
            {
                if(time.isAfter(s.getStop()) && time.isBefore(s.getStop().plusMinutes(s.getInterval())))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStopHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStopMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "(Shift Stop)";
                }
                else if(time.isBefore(s.getLunchstop()) && time.isAfter(s.getLunchstart()))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftLunchStartHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStartMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "(Lunch Start)";  
                }
                else if(time.isBefore(s.getStop()) && (time.isAfter(s.getStop().minusMinutes(s.getGraceperiod())) || time.equals(s.getStop().minusMinutes(s.getGraceperiod()))))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStopHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStopMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "(Shift Stop)";
                }
                else if(time.isBefore(s.getStop().minusMinutes(s.getGraceperiod())) && (time.isAfter(s.getStop().minusMinutes(s.getDock())) || time.equals(s.getStop().minusMinutes(s.getDock()))))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStopHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStopMinute() - s.getDock());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "(Shift Dock)";
                }
                else
                {
                    if(mod !=0)
                    {  
                        adjustedTS.add(Calendar.MINUTE, 15 - unroundedMinutes);
                        adjustedTS.set(adjustedTS.SECOND, 0);
                        trigger = "(Interval Round)";
                    }
                    else
                    {
                        adjustedTS.set(adjustedTS.SECOND, 0);
                        trigger = "(None)"; 
                    }
                }
            }
        }
        else if(day == originalTS.SATURDAY || day == originalTS.SUNDAY)
        {
            adjustedTS.add(Calendar.MINUTE, mod < 8 ? -mod : (15-mod));
            adjustedTS.set(adjustedTS.SECOND, 0);
            trigger = "(Interval Round)";
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