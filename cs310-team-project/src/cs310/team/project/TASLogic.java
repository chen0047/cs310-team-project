package cs310.team.project;

import java.util.*;
import org.json.simple.*;

public class TASLogic 
{
    
    public TASLogic()
    {

    }
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift)
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
    
    public static double calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift)
    {
       double total_accrued_time = 0;      
       System.out.println("ArrayList size: " + punchlist.size());
       double total_shift_time = 0;
       double percent = 0.0;
       ArrayList<ArrayList<Punch>> dailyPunchLists = new ArrayList<>(); 
        for(int i = 0; i < dailyPunchLists.size(); i++)
        {
          total_accrued_time += TASLogic.calculateTotalMinutes(dailyPunchLists.get(i), shift);
          total_shift_time = shift.getTotalMinutes() * shift.getNumOfDaysInShift();        
        }
      
       for(int i = 1; i < 8; i++)
       {
           ArrayList<Punch> dayOfPunches = new ArrayList<>();
           for(int j = 0 ; j < punchlist.size(); j++)
           {
               if(punchlist.get(j).getDayOfWeek() == i)
                   dayOfPunches.add(punchlist.get(j));
           }
           if(!dayOfPunches.isEmpty())
                dailyPunchLists.add(dayOfPunches);
       }
       
       System.out.println("Time worked: " + total_accrued_time);
       System.out.println("Shift time: " + total_shift_time);
       percent = 100 - ((total_accrued_time/total_shift_time)* 100);
       System.out.println("Percentage: " + percent);
  
       return percent;
    }   
}
