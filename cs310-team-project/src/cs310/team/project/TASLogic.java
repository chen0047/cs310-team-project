 package cs310.team.project;

import java.util.*;
import org.json.simple.*;

public class TASLogic 
{
    
    public static int _calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift)
    {
        int lunch_deduction = 360;
        int minutes  = -1;

        int minute_conversion = 60000;
        int lunch_time = 30;
        
        if(dailypunchlist.size() == 0)
        {
            minutes = 0;
        }
        
        else if (dailypunchlist.size() == 1)
        {
            minutes = 0;
        }
        else if(dailypunchlist.size() == 2)
         {
            long in_time = dailypunchlist.get(0).getAdjustedTS();
            long out_time = dailypunchlist.get(1).getAdjustedTS();
            
            if(((out_time - in_time) / minute_conversion) > lunch_deduction)
            {
                minutes = (int)(((out_time - in_time) / minute_conversion) - lunch_time);
            }
            
            else
            {
                minutes = (int)((out_time - in_time) / minute_conversion);
            }
            
            
        }
        
        else if (dailypunchlist.size() == 3)
        {
            minutes = 0;
        }
        
        else if (dailypunchlist.size() == 4)
        {
            long time_in = dailypunchlist.get(0).getAdjustedTS();
            long lunch_start = dailypunchlist.get(1).getAdjustedTS();
            long lunch_stop = dailypunchlist.get(2).getAdjustedTS();
            long out_time = dailypunchlist.get(3).getAdjustedTS();
            
            long lunch_break = lunch_stop - lunch_start;
            
            if(((out_time - time_in) / minute_conversion) > lunch_deduction)
            {
                minutes = (int)(((out_time - time_in) - lunch_break) / minute_conversion);
            }
            
            else
            {
                 minutes = (int)((out_time - time_in) / minute_conversion);  
            }
        }
        
        else 
        {
            minutes = -1;
        }
        
        return minutes;
}
    
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
    
    public static double _calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift) {
       double totalAccuredTime = 0;
       double totalShiftTime = 0;
       double percent = 0.0;
       
       System.out.println("ArrayList size: " + punchlist.size());
      
       ArrayList<ArrayList<Punch>> dailyPunchLists = new ArrayList<>(); 
       
      
      for(int i = 1; i < 8; i++){
           ArrayList<Punch> dayOfPunches = new ArrayList<>();
           
           for(int j = 0 ; j < punchlist.size(); j++)
           {
               if(punchlist.get(j).getDayOfWeek() == i)
                   dayOfPunches.add(punchlist.get(j));
           }
           if(!dayOfPunches.isEmpty())
                dailyPunchLists.add(dayOfPunches);
       }
       
       for(int i = 0; i < dailyPunchLists.size(); i++){ 
          totalAccuredTime += TASLogic.calculateTotalMinutes(dailyPunchLists.get(i), shift);
          totalShiftTime = shift.getShiftLength()*7;//* shift.getNumOfDaysInShift();
          percent = ( 1 - ((totalAccuredTime/totalShiftTime))) * 100;
       }
       System.out.println("Time actually worked: " + totalAccuredTime);
       System.out.println("Shift time in minutes: " + totalShiftTime);
       System.out.println("ShiftLengthTotal: " + shift.getShiftLength());
        //Calc absenteeism as percentage
       System.out.println("Percent: " + percent);
       
       
       return percent;
    }   
    
    public static double calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift) {
        
        int hoursscheduled = shift.getTotalScheduledHours();
        
        int hoursworked = calculateTotalMinutes(punchlist, shift);
        
        System.out.println( "calculateAbsenteeism(): hoursscheduled: " + hoursscheduled + ", hoursworked: " + hoursworked );
        
        double absenteeism = ( 100.0 - (((double)hoursworked / (double)hoursscheduled) * 100.0) );
        
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
