 package cs310.team.project;

import java.text.SimpleDateFormat;
import java.util.*;
import org.json.simple.*;

public class TASLogic 
{
    /* Calculate the total number of hours that were accumulated by the employee */

    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {

        /* Initialize local variables */

        long beginBlock = 0, endBlock = 0;
        boolean withinBlock = false, adjustedForLunch = false, outBeforeLunch = false, inAfterLunch = false, ignorebit = false;

        int subtotalMinutes = 0, totalMinutes = 0, minutes = 0, punchtypeid;

        Punch punch;
        
        String badgeid, currentDate = "", newDate;

        if (dailypunchlist.size() > 0) {

            /* Iterate Through Event List and Compute Hours */

            for (int i = 0; i < (dailypunchlist.size()); i++) {
                
                punch = (Punch)(dailypunchlist.get(i));
                punchtypeid = punch.getPunchType();

                GregorianCalendar ats = new GregorianCalendar();
                ats.setTimeInMillis(punch.getAdjustedTS());
                
                int day = ats.get(Calendar.DAY_OF_WEEK);
                
                badgeid = punch.getBadgeId();
                //shift = getShift(getBadge(badgeid));

                /* Create lunch break start/stop "landmarks" */

                GregorianCalendar lunchStart = new GregorianCalendar();
                lunchStart.setTimeInMillis(punch.getOriginalTimeStamp());

                lunchStart.set( Calendar.HOUR_OF_DAY, shift.getLunchstart().getHour() ); // day
                lunchStart.set( Calendar.MINUTE, shift.getLunchstart().getMinute()); // day
                lunchStart.set( Calendar.SECOND, 0 );

                GregorianCalendar lunchStop = new GregorianCalendar();
                lunchStop.setTimeInMillis(punch.getOriginalTS());

                lunchStop.set( Calendar.HOUR_OF_DAY, shift.getLunchstop().getHour() ); // day
                lunchStop.set( Calendar.MINUTE, shift.getLunchstop().getMinute() ); // day
                lunchStop.set( Calendar.SECOND, 0 );

                /* Get lunch deduction and threshold values from Shift rules */

                int lunchDeductionThreshold = shift.getLunchdeduct(); // day
                int lunchDeductionAmount = shift.getLunchDuration(); // day

                //System.out.println("Lunch Deduction: " + lunchDeductionAmount);


                newDate = ((new SimpleDateFormat("MM-dd-yyyy")).format(ats.getTime()));



                /* Clock-In Events (use first Clock-In event in the block and ignore others that occur within the block) */

                if ( (punchtypeid == Punch.CLOCK_IN) && (withinBlock == false) ) {

                    /* Set beginning of time block */

                    beginBlock = punch.getAdjustedTS();
                    withinBlock = true;


                    /* Clock-In After Lunch Break */

                    if ( beginBlock > lunchStop.getTimeInMillis() ) {

                            inAfterLunch = true;

                    }

                    /* Clock-In Before Lunch Break */

                    else if ( beginBlock < lunchStart.getTimeInMillis() ) {

                            outBeforeLunch = false;

                    }

                    /* Clock-In During Lunch Break */

                    else if ( punch.getTrigger().equals("Lunch Stop") ) {

                            adjustedForLunch = true;

                    }

                }

                /* Clock-Out Events (use first Clock-Out event in the block and ignore others that occur outside the block) */

                else if ( (punchtypeid == Punch.CLOCK_OUT) && (withinBlock == true) ) {

                    /* Set end of time block */

                    endBlock = punch.getAdjustedTS();
                    withinBlock = false;

                    /* Calculate Total Accrued Minutes */

                    minutes = (int)( (endBlock - beginBlock) / (long)60000 );

                    /* If Clock-Out occured before Clock-In, set accrued minutes to zero */

                    if ( minutes < 0 ) {

                            minutes = 0;

                    }

                    /* Add Minutes to Running Total */

                    subtotalMinutes += minutes;

                    /* Clock-Out Before Lunch Break */

                    if ( endBlock < lunchStart.getTimeInMillis() ) {

                            outBeforeLunch = true;

                    }

                    /* Clock-Out During Lunch Break */

                    else if ( punch.getTrigger().equals("Lunch Start") ) {

                            adjustedForLunch = true;

                    }

                }

                /* Time-Out Event (close block but do not add any minutes to the total; this is an error condition that must be corrected by a user) */

                else if ( (punchtypeid == Punch.TIME_OUT) && (withinBlock == true) ) {

                    withinBlock = false;

                }
                
                if ( !(currentDate.equals(newDate)) ) {

                    /* Check to see if person was clocked out before lunch and clocked in after lunch; does not apply on weekends */

                    if ( (adjustedForLunch == false) && ( outBeforeLunch && inAfterLunch ) ) {

                            adjustedForLunch = true;

                    }

                    /* If lunch has not already been deducted or if this is a weekend, and if the person has exceeded their threshold, deduct lunch break minutes */

                    if ( (subtotalMinutes > lunchDeductionThreshold) && ( adjustedForLunch == false ) ) {

                            subtotalMinutes -= lunchDeductionAmount;

                    }

                    /* Add Running Total to Weekly Total */

                    totalMinutes += subtotalMinutes;
                    
                    subtotalMinutes = 0;
                    adjustedForLunch = false;

                }

            }

        }
        
        return totalMinutes;

    }
    
    public static int _calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift){
        
      boolean insidePair = false;
      boolean lunchDeducted = false;
      int totalMinute = 0;
      long startTime = 0;
     
      for(int i = 0; i < dailypunchlist.size(); i++){
          
          Punch p = dailypunchlist.get(i);
          
          if (p.getPunchType() == Punch.CLOCK_IN) {
              
              if (!insidePair) {
                  startTime = p.getAdjustedTS();
                  insidePair = true;
              }
              if (p.getTrigger().equals("Lunch Stop"))
                  lunchDeducted = true;
              
          }
          
          else if (p.getPunchType() == Punch.CLOCK_OUT) {
              
              if (insidePair) {
                  totalMinute += (int)((p.getAdjustedTS() - startTime) / 60000);
                  insidePair = false;
              }
              if (p.getTrigger().equals("Lunch Start"))
                  lunchDeducted = true;
              
          }
          
          else if (p.getPunchType() == Punch.TIME_OUT) {
              
              if (insidePair){
                  insidePair = false;
              }
          }
          
             
      }
      
      if (!lunchDeducted) {
        if (totalMinute > shift.getLunchdeduct()) {
            System.out.println("Lunch Duration: " + shift.getLunchDuration());
            totalMinute -= shift.getLunchDuration();
        }
      }
      
      System.out.println("totalMin: " + totalMinute);
              
        
        return totalMinute;
    }
    

    
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist)
    {
        

       ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
       
       
       for(int i = 0 ; i < dailypunchlist.size(); ++i)
       {
           
            Punch punch = dailypunchlist.get(i);
           
            HashMap<String, String> punchData = new HashMap<>();
            
            punchData.put("punchdata", String.valueOf(punch.getTrigger()));
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginalTS()));
            punchData.put("badgeid",String.valueOf(punch.getBadgeId()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.getAdjustedTS()));
            punchData.put("punchtypeid", String.valueOf(punch.getPunchType()));
            punchData.put("terminalid", String.valueOf(punch.getTerminalId()));
            punchData.put("id", String.valueOf(punch.getPunchId()));
            
            
            jsonData.add(punchData);
       }
       
        String json = JSONValue.toJSONString(jsonData);
        return json;
        
    }
    
    
    public static double calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift) {
        
        int hoursscheduled = shift.getTotalScheduledHours();
        int hoursworked = calculateTotalMinutes(punchlist, shift);
        System.out.println( "calculateAbsenteeism(): hoursscheduled: " + hoursscheduled + ", hoursworked: " + hoursworked );       
        //double absenteeism = ( 100.0 - (((double)hoursworked / (double)hoursscheduled) * 100.0));
        //double absenteeism = ((((double)hoursscheduled/ (double)hoursworked) * 100.0) - 100);
        
        double absenteeism = ( 100 - (((double)hoursworked / (double)hoursscheduled) * 100.0) );
        
        return absenteeism;
        
    }
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift s){
        
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        
        HashMap<String, String> total = new HashMap<>();
        
        for (int i = 0; i < punchlist.size(); i++){
        
            Punch punch = punchlist.get(i);
          
            HashMap<String, String> punchData = new HashMap<>();
            
            punchData.put("punchdata", String.valueOf(punch.getTrigger()));
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginalTS()));
            punchData.put("badgeid",String.valueOf(punch.getBadgeId()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.getAdjustedTS()));
            punchData.put("punchtypeid", String.valueOf(punch.getPunchType()));
            punchData.put("terminalid", String.valueOf(punch.getTerminalId()));
            punchData.put("id", String.valueOf(punch.getPunchId()));
            
            jsonData.add(punchData);
            
        }
        
        total.put("totalminutes", String.valueOf(TASLogic.calculateTotalMinutes(punchlist, s)));
        total.put("absenteeism", String.format("%.2f", TASLogic.calculateAbsenteeism(punchlist, s)) + "%");
        
        jsonData.add(total);
        
        String json = JSONValue.toJSONString(jsonData);
        return json;
    } 
}
