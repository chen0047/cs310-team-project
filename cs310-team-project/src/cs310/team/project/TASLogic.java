 package cs310.team.project;

import java.util.*;
import org.json.simple.*;

public class TASLogic 
{
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift){
        
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
        double absenteeism = ( 100.0 - (((double)hoursworked / (double)hoursscheduled) * 100.0));
        return absenteeism;
        
    }
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift s){
        
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
       
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
            punchData.put("totalminutes", String.valueOf(TASLogic.calculateTotalMinutes(punchlist, s)));
            punchData.put("absenteeism", String.valueOf(TASLogic.calculateAbsenteeism(punchlist, s)));
            
            jsonData.add(punchData);
            
        }
        String json = JSONValue.toJSONString(jsonData);
        return json;
    } 
}
