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
    
    public static double calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift){
        
        
        
        return 0;
    } 
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift s){
        
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
       
        for(int i = 0; i < punchlist.size(); ++i){
            
            Punch punch = punchlist.get(i);
            
            HashMap<String, String> totalminutes = new HashMap<>();
            HashMap<String, String> absenteeism = new HashMap<>();
            
            
        
        
        
        }
        String json = JSONValue.toJSONString(jsonData);
        
        return json;
        
        
    }
    
    
}
