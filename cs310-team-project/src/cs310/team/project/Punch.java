package cs310.team.project;

import java.util.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class Punch {
    
    public static final int CLOCK_IN = 1;
    public static final int CLOCK_OUT = 0;
    public static final int TIME_OUT = 2;

    public Punch(){
        originalTS = new GregorianCalendar();
        adjustedTS = new GregorianCalendar();
        adjustedTS.setTimeInMillis( originalTS.getTimeInMillis() );
    }
    
    private Badge badge;    
    private int terminalId;
    private int punchId;
    //private int eventtypeid;
    private int shiftId;
    private int punchType;
    private GregorianCalendar originalTS;

    private GregorianCalendar adjustedTS;
    private long Timestamp;

    private String trigger = "";
    private int day;
    
    public Punch(Badge badge, int terminalId,int punchId) {

    originalTS = new GregorianCalendar();
    adjustedTS = new GregorianCalendar();
    adjustedTS.setTimeInMillis( originalTS.getTimeInMillis() );
    this.terminalId = terminalId;
    this.badge = badge;
    this.punchId = punchId;
    
    }
    public Punch(Badge badge, int terminalId, int punchId, String BadgeId, long Timestamp, int punchType) {

    originalTS = new GregorianCalendar();
    adjustedTS = new GregorianCalendar();
    adjustedTS.setTimeInMillis( originalTS.getTimeInMillis() );
    this.badge = badge;
    this.terminalId = terminalId;
    this.punchId = punchId;
    
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
    
    

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }


    public void setOriginalTS(GregorianCalendar originalTS) {
        this.originalTS.setTimeInMillis(originalTS.getTimeInMillis());
    }

    public void setAdjustedTS(GregorianCalendar adjustedTS) {
        this.adjustedTS.setTimeInMillis(adjustedTS.getTimeInMillis());
    }

    public int getPunchId() {
        return punchId;
    }
    
    public String getBadgeId() {
        return badge.getId();
    }
    
    public int getPunchType() {
        return punchType;
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
    
    public String getTrigger(){
    
        return this.trigger;
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
    
    public void adjust(Shift s) {
        
        boolean adjusted = false;
        
        int adjustedminute;
        
        SimpleDateFormat format = new SimpleDateFormat("EEE MM/dd/yyyy HH:mm:ss");
        
        int day = originalTS.get(Calendar.DAY_OF_WEEK);
        
        int shiftinterval = s.getInterval(); // day
        
        System.out.println("Original: " + format.format(originalTS.getTime()));
        
        
        /* INITIALIZE ADJUSTED TIMESTAMP TO ORIGINAL TIMESTAMP */
        
        adjustedTS.setTimeInMillis(originalTS.getTimeInMillis());
        
        /* GENERATE TIMESTAMP OBJECTS */
        
        /* Original Punch */
        
        long originalpunch = originalTS.getTimeInMillis();
        long adjustedpunch = originalpunch;
        
        int originalminute = originalTS.get(Calendar.MINUTE);

        /* Shift Start */
        
        GregorianCalendar shiftstarttimestamp = new GregorianCalendar();
        shiftstarttimestamp.setTimeInMillis(originalTS.getTimeInMillis());
        shiftstarttimestamp.set(Calendar.HOUR_OF_DAY, s.getStart().getHour());// day
        shiftstarttimestamp.set(Calendar.MINUTE, s.getStart().getMinute());// day
        shiftstarttimestamp.set(Calendar.SECOND, 0);
        
        long shiftstart = shiftstarttimestamp.getTimeInMillis();
        
        /* Shift Stop */
        
        GregorianCalendar shiftstoptimestamp = new GregorianCalendar();
        shiftstoptimestamp.setTimeInMillis(originalTS.getTimeInMillis());
        shiftstoptimestamp.set(Calendar.HOUR_OF_DAY, s.getStop().getHour()); // day
        shiftstoptimestamp.set(Calendar.MINUTE, s.getStop().getMinute());// day
        shiftstoptimestamp.set(Calendar.SECOND, 0);
        
        long shiftstop = shiftstoptimestamp.getTimeInMillis();
        
        /* Lunch Start */
        
        GregorianCalendar lunchstarttimestamp = new GregorianCalendar();
        lunchstarttimestamp.setTimeInMillis(originalTS.getTimeInMillis());
        lunchstarttimestamp.set(Calendar.HOUR_OF_DAY, s.getLunchstarthour(day));
        lunchstarttimestamp.set(Calendar.MINUTE, s.getLunchstartminute(day));
        lunchstarttimestamp.set(Calendar.SECOND, 0);
        
        long lunchstart = lunchstarttimestamp.getTimeInMillis();
        System.out.println( "Lunch Start: " + format.format(lunchstarttimestamp.getTime()) );
        
        /* Lunch Stop */
        
        GregorianCalendar lunchstoptimestamp = new GregorianCalendar();
        lunchstoptimestamp.setTimeInMillis(originalTS.getTimeInMillis());
        lunchstoptimestamp.set(Calendar.HOUR_OF_DAY, s.getLunchstophour(day));
        lunchstoptimestamp.set(Calendar.MINUTE, s.getLunchstopminute(day));
        lunchstoptimestamp.set(Calendar.SECOND, 0);
        
        long lunchstop = lunchstoptimestamp.getTimeInMillis();
        System.out.println( "Lunch Stop: " + format.format(lunchstoptimestamp.getTime()) );
        
        /* Interval Start */
        
        GregorianCalendar intervalstarttimestamp = new GregorianCalendar();
        intervalstarttimestamp.setTimeInMillis(shiftstarttimestamp.getTimeInMillis());
        intervalstarttimestamp.add(Calendar.MINUTE, -(s.getInterval()));
        
        long intervalstart = intervalstarttimestamp.getTimeInMillis();
        
        /* Grace Start */
        
        GregorianCalendar gracestarttimestamp = new GregorianCalendar();
        gracestarttimestamp.setTimeInMillis(shiftstarttimestamp.getTimeInMillis());
        gracestarttimestamp.add(Calendar.MINUTE, s.getGraceperiod());
        
        long gracestart = gracestarttimestamp.getTimeInMillis();
        
        /* Dock Start */
        
        GregorianCalendar dockstarttimestamp = new GregorianCalendar();
        dockstarttimestamp.setTimeInMillis(shiftstarttimestamp.getTimeInMillis());
        dockstarttimestamp.add(Calendar.MINUTE, s.getDock());
        
        long dockstart = dockstarttimestamp.getTimeInMillis();
        
        /* Interval Stop */
        
        GregorianCalendar intervalstoptimestamp = new GregorianCalendar();
        intervalstoptimestamp.setTimeInMillis(shiftstoptimestamp.getTimeInMillis());
        intervalstoptimestamp.add(Calendar.MINUTE, s.getInterval());
        
        long intervalstop = intervalstoptimestamp.getTimeInMillis();
        
        /* Grace Stop */
        
        GregorianCalendar gracestoptimestamp = new GregorianCalendar();
        gracestoptimestamp.setTimeInMillis(shiftstoptimestamp.getTimeInMillis());
        gracestoptimestamp.add(Calendar.MINUTE, -(s.getGraceperiod()));
        
        long gracestop = gracestoptimestamp.getTimeInMillis();
        
        /* Dock Stop */
        
        GregorianCalendar dockstoptimestamp = new GregorianCalendar();
        dockstoptimestamp.setTimeInMillis(shiftstoptimestamp.getTimeInMillis());
        dockstoptimestamp.add(Calendar.MINUTE, -(s.getDock()));
        
        long dockstop = dockstoptimestamp.getTimeInMillis();
        
        /* PERFORM CLOCK IN ADJUSTMENTS */
        
        if ( (originalTS.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) && (originalTS.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) ) {
            
            /* Clock In Punches */
        
            if (punchType == CLOCK_IN) {

                /* Interval Start */

                if ((originalpunch >= intervalstart) && (originalpunch <= shiftstart)) {
                    adjustedpunch = shiftstart;
                    trigger = "Shift Start";
                    adjusted = true;
                }

                /* Grace Start */

                else if ((originalpunch > shiftstart) && (originalpunch <= gracestart)) {
                    adjustedpunch = shiftstart;
                    trigger = "Shift Start";
                    adjusted = true;
                }

                /* Dock Start */

                else if ((originalpunch > gracestart) && (originalpunch <= dockstart)) {
                    adjustedpunch = dockstart;
                    trigger = "Shift Dock";
                    adjusted = true;
                }

                /* Lunch Stop */

                else if ((originalpunch >= lunchstart) && (originalpunch <= lunchstop)) {
                    adjustedpunch = lunchstop;
                    trigger = "Lunch Stop";
                    adjusted = true;
                }
                
            }
            
            /* Clock Out Punches */

            else if (punchType == CLOCK_OUT) {

                /* Lunch Start */

                if ((originalpunch >= lunchstart) && (originalpunch <= lunchstop)) {
                    adjustedpunch = lunchstart;
                    trigger = "Lunch Start";
                    adjusted = true;
                }

                /* Dock Stop */

                else if ((originalpunch >= dockstop) && (originalpunch < gracestop)) {
                    adjustedpunch = dockstop;
                    trigger = "Shift Dock";
                    adjusted = true;
                }

                /* Grace Stop */

                else if ((originalpunch >= gracestop) && (originalpunch < shiftstop)) {
                    adjustedpunch = shiftstop;
                    trigger = "Shift Stop";
                    adjusted = true;
                }

                /* Interval Stop */

                else if ((originalpunch >= shiftstop) && (originalpunch <= intervalstop)) {
                    adjustedpunch = shiftstop;
                    trigger = "Shift Stop";
                    adjusted = true;
                }
                
            }
            
        }
        
        /* If none of the other rules apply ... */
        
        if (!adjusted) {
            
            /* ... perform an interval round if needed ... */

            if ( originalminute % shiftinterval != 0 ) {

                if ((originalminute % shiftinterval) < (shiftinterval / 2)) {
                    adjustedminute = (Math.round(originalminute / shiftinterval) * shiftinterval);
                }

                else {
                    adjustedminute = (Math.round(originalminute / shiftinterval) * shiftinterval) + shiftinterval;
                }
                
                adjustedTS.add(Calendar.MINUTE, (adjustedminute - originalminute));
                adjustedTS.set(Calendar.SECOND, 0);
                
                adjustedpunch = adjustedTS.getTimeInMillis();
                trigger = "Interval Round";

            }
            
            /* ... or else, leave the punch alone */

            else {
                adjustedTS.set(Calendar.SECOND, 0);
                adjustedpunch = adjustedTS.getTimeInMillis();
                trigger = "None";
            }

        }
        
        /* Apply Punch Adjustment */
        
        adjustedTS.setTimeInMillis(adjustedpunch);
        
    }
    
    public void _adjust(Shift s)
    {
        adjustedTS = (GregorianCalendar) originalTS.clone();
        LocalTime time = LocalTime.of(originalTS.get(originalTS.HOUR_OF_DAY), originalTS.get(originalTS.MINUTE), 0);
        int unroundedMinutes = originalTS.get(originalTS.MINUTE);
        int interval = s.getInterval();
        int mod = unroundedMinutes % interval;
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
                    trigger = "Shift Start";
                }
                else if(time.isBefore(s.getLunchstop()) && time.isAfter(s.getLunchstart()))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftLunchStopHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStopMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "Lunch Stop";   
                }
                else if(time.isAfter(s.getStart()) && (time.isBefore(s.getStart().plusMinutes(s.getGraceperiod())) || time.equals(s.getStart().plusMinutes(s.getGraceperiod()))))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStartHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStartMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "Shift Start";
                }
                else if(time.isAfter(s.getStart().plusMinutes(s.getGraceperiod())) && (time.isBefore(s.getStart().plusMinutes(s.getDock())) || time.equals(s.getStart().plusMinutes(s.getDock()))))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStartHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStartMinute() + s.getDock());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "Shift Dock";
                }
                else
                {
                    if(mod !=0)
                    {  
                        if(originalTS.get(originalTS.SECOND) > 50)
                        {
                            mod += 1;
                            adjustedTS.add(Calendar.MINUTE, mod < 8 ? -mod : (interval-(mod-1)));
                        }
                        else
                            adjustedTS.add(Calendar.MINUTE, mod < 8 ? -mod : (interval-mod));
                        adjustedTS.set(adjustedTS.SECOND, 0);
                        trigger = "Interval Round";
                    }
                    else
                    {
                        adjustedTS.set(adjustedTS.SECOND, 0);
                        trigger = "None"; 
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
                    trigger = "Shift Stop";
                }
                else if(time.isBefore(s.getLunchstop()) && time.isAfter(s.getLunchstart()))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftLunchStartHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftLunchStartMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "Lunch Start";  
                }
                else if(time.isBefore(s.getStop()) && (time.isAfter(s.getStop().minusMinutes(s.getGraceperiod())) || time.equals(s.getStop().minusMinutes(s.getGraceperiod()))))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStopHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStopMinute());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "Shift Stop";
                }
                else if(time.isBefore(s.getStop().minusMinutes(s.getGraceperiod())) && (time.isAfter(s.getStop().minusMinutes(s.getDock())) || time.equals(s.getStop().minusMinutes(s.getDock()))))
                {
                    adjustedTS.set(adjustedTS.HOUR_OF_DAY, s.getShiftStopHour());
                    adjustedTS.set(adjustedTS.MINUTE, s.getShiftStopMinute() - s.getDock());
                    adjustedTS.set(adjustedTS.SECOND, 0);
                    trigger = "Shift Dock";
                }
                else
                {
                    if(mod !=0)
                    {  
                        if(originalTS.get(originalTS.SECOND) > 50)
                        {
                            mod += 1;
                            adjustedTS.add(Calendar.MINUTE, mod < 8 ? -mod : (interval-(mod-1)));
                        }
                        else
                            adjustedTS.add(Calendar.MINUTE, mod < 8 ? -mod : (interval-mod));
                        adjustedTS.set(adjustedTS.SECOND, 0);
                        trigger = "Interval Round";
                    }
                    else
                    {
                        adjustedTS.set(adjustedTS.SECOND, 0);
                        trigger = "None"; 
                    }
                }
            }
        }
        else if(day == originalTS.SATURDAY || day == originalTS.SUNDAY)
        {
            adjustedTS.add(Calendar.MINUTE, mod < 8 ? -mod : (15-mod));
            adjustedTS.set(adjustedTS.SECOND, 0);
            trigger = "Interval Round";
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
        return "#" + badge.getId() + Status + formattedTime + " " + "("+ trigger + ")";
    }


    public int getDayOfWeek() {
        return originalTS.get(Calendar.DAY_OF_WEEK);    
    }
}
