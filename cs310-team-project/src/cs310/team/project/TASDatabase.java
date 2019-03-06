package cs310.team.project;

import java.sql.*;
import java.util.GregorianCalendar;


 public class TASDatabase {
     
     private Connection conn;

     
     public TASDatabase (){
        try{
             String server = ("jdbc:mysql://localhost:3306/tas");
             String username = "team_B";
             String password = "cs310_B";
             
             System.out.println("Connecting to " + server + "...");
             
             Class.forName("com.mysql.jdbc.Driver").newInstance();
             
             conn = DriverManager.getConnection(server, username, password);
             
             if(conn.isValid(0)){
                 System.out.println("Connected Successfully!");
             
             }
        }catch (Exception e) { 
            System.err.println(e.toString());
        } 
         
     }
    
     public Badge getBadge(String badgeID){
     
         PreparedStatement pstSelect = null, pstUpdate = null;
         ResultSet resultset = null;
         
         String query;
         
         boolean hasresults;
         
         Badge b = new Badge();
         
         try{
      
            query = "SELECT * FROM badge b WHERE id=?";
            
            pstSelect = conn.prepareStatement(query);
            pstSelect.setString(1, badgeID);            
            
            System.err.println("Submitting Query ... ");

            hasresults = pstSelect.execute();

            System.err.println("Getting Results ...");
            
            while (hasresults || pstSelect.getUpdateCount() != -1){

                if (hasresults){

                   resultset = pstSelect.getResultSet();
                   
                   if (resultset.next()){
                        b.setId(resultset.getString("id"));
                        b.setDescription(resultset.getString("description"));
                   }
                  
            }
             
            hasresults = pstSelect.getMoreResults();

            System.out.println();
            //conn.close();
            }
                 
            }catch (Exception e){
             System.err.println(e.toString());
         }
         
         finally{
             if(resultset != null){ try {resultset.close(); resultset = null;} catch (Exception e){}}
             
             if(pstSelect != null){ try {pstSelect.close(); pstSelect = null;} catch (Exception e){}}
             
             if(pstUpdate != null) { try {pstUpdate.close(); pstUpdate = null;} catch (Exception e){}}
             
         }
         return b;  
     }
     
     public Punch getPunch(int punchID){ 
         
         PreparedStatement pstSelect = null, pstUpdate = null;
         ResultSet resultset = null;
         
         String query;
         
         boolean hasresults;
         
         Punch p = new Punch(); 
         
         try{
      
            query = "SELECT *, UNIX_TIMESTAMP(originaltimestamp)*1000 AS ts FROM punch p WHERE id=?";
            
            pstSelect = conn.prepareStatement(query);
            pstSelect.setInt(1, punchID);
            
            System.err.println("Submitting Query ... ");

            hasresults = pstSelect.execute();

            System.err.println("Getting Results ...");
            
            while (hasresults || pstSelect.getUpdateCount() != -1){

                if (hasresults){

                   resultset = pstSelect.getResultSet();
                   
                   if (resultset.next()){
                        p.setPunchId(resultset.getInt("id"));
                        p.setTerminalId(resultset.getInt("terminalid"));
                        p.setBadge(getBadge(resultset.getString("badgeid")) );
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.setTimeInMillis( resultset.getLong("ts") );
                        p.setOriginalTS(gc);
                        p.setPunchType(resultset.getInt("punchtypeid"));
                   }
                   
   
            }
             
            hasresults = pstSelect.getMoreResults();

            System.out.println();
            //conn.close();
            }
                 
            }catch (Exception e){
             System.err.println(e.toString());
         }
         
         finally{
             if(resultset != null){ try {resultset.close(); resultset = null;} catch (Exception e){}}
             
             if(pstSelect != null){ try {pstSelect.close(); pstSelect = null;} catch (Exception e){}}
             
             if(pstUpdate != null) { try {pstUpdate.close(); pstUpdate = null;} catch (Exception e){}}
             
         }
         
         return p;
     }
     
     public Shift getShift(int shiftID){
         
         PreparedStatement pstSelect = null, pstUpdate = null;
         ResultSet resultset = null;
         
         String query;
         
         boolean hasresults;
         
         Shift s = null;
         
         try{
      
            query = "SELECT *, HOUR(`start`) AS starthour, MINUTE(`start`) AS " 
                    + "startminute, HOUR(`stop`) AS stophour, MINUTE(`stop`) AS "
                    + "stopminute, HOUR (`lunchstart`) AS lunchstarthour, MINUTE"
                    + "(`lunchstart`) AS lunchstartminute, HOUR(`lunchstop`) AS "
                    + "lunchstophour, MINUTE (`lunchstop`) AS lunchstopminute "
                    + "FROM shift s WHERE id = ?";
            
            pstSelect = conn.prepareStatement(query);
            pstSelect.setInt(1, shiftID);
            
            System.err.println("Submitting Query ... ");

            hasresults = pstSelect.execute();

            System.err.println("Getting Results ...");
            
            while (hasresults || pstSelect.getUpdateCount() != -1){

                if (hasresults){

                   resultset = pstSelect.getResultSet();
                   s = new Shift();
                  
                   if(resultset.next()){
                        s.setId(resultset.getInt("id"));
                        s.setDescription(resultset.getString("description"));
                        s.setShiftStart(resultset.getInt("starthour"), resultset.getInt("startminute"));
                        s.setShiftStop(resultset.getInt("stophour"), resultset.getInt("stopminute"));
                        s.setInterval(resultset.getInt("interval"));
                        s.setGraceperiod(resultset.getInt("graceperiod"));
                        s.setDock(resultset.getInt("dock"));
                        s.setShiftLunchStart(resultset.getInt("lunchstarthour"), resultset.getInt("lunchstartminute"));
                        s.setShiftLunchStop(resultset.getInt("lunchstophour"), resultset.getInt("lunchstopminute"));
                        s.setLunchdeduct(resultset.getInt("lunchdeduct"));
                   
                   }
            }
                     
            
            hasresults = pstSelect.getMoreResults();


            System.out.println();
            //conn.close();
            }
                 
            }catch (Exception e){
             System.err.println(e.toString());
         }
         
         finally{
             if(resultset != null){ try {resultset.close(); resultset = null;} catch (Exception e){}}
             
             if(pstSelect != null){ try {pstSelect.close(); pstSelect = null;} catch (Exception e){}}
             
             if(pstUpdate != null) { try {pstUpdate.close(); pstUpdate = null;} catch (Exception e){}}
             
         }
         return s;
     }
    public Shift getShift(Badge badge){
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        
        boolean hasresults;
        String id = badge.getId();
        int shiftID = -1; //****
        Shift s = null;
        
        try{
            String query = "SELECT * FROM employee WHERE badgeid=?";
        
            pstSelect = conn.prepareStatement(query);
            pstSelect.setString(1, badge.getId());
            
            System.err.println("Submitting Query ... ");

            hasresults = pstSelect.execute();

            System.err.println("Getting Results ...");
            
            while (hasresults || pstSelect.getUpdateCount() != -1){

                if (hasresults){

                   resultset = pstSelect.getResultSet();
                  
                   if(resultset.next()){
                       
                       shiftID = resultset.getInt("shiftid");
                       s = getShift(shiftID);
                   }
                   
                  
            }
                     
            
            hasresults = pstSelect.getMoreResults();


            System.out.println();
            //conn.close();
            }
                 
            }catch (Exception e){
             System.err.println(e.toString());
         }
         
         finally{
             if(resultset != null){ try {resultset.close(); resultset = null;} catch (Exception e){}}
             
             if(pstSelect != null){ try {pstSelect.close(); pstSelect = null;} catch (Exception e){}}
             
             if(pstUpdate != null) { try {pstUpdate.close(); pstUpdate = null;} catch (Exception e){}}
             
         }
         return s;
        }
}


