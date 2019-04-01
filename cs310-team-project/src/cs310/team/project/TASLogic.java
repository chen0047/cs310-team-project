package cs310.team.project;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.*;

public class TASLogic {
    
    public TASLogic(){
        
       
        
    }
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift){
        
        
        
        return 0;
    }
    
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist){
        

       ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
       
       
       for(int i = 0 ; i < dailypunchlist.size(); ++i){
           
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
    
    
}
