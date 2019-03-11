package cs310.team.project;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.*;


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


    public int insertPunch(Punch p){ 
         PreparedStatement pstSelect = null, pstUpdate = null;
         ResultSet resultset = null;
         
         String query;
         int updateCount = 0;
         int id = 0;
         
         int newTerminalId = p.getTerminalId();
         int punchType = p.getPunchType();
         String newBadgeId = p.getBadgeId();
         long timeStamp = p.getOriginalTimeStamp();
         
         GregorianCalendar originalTS = new GregorianCalendar();
         originalTS.setTimeInMillis(timeStamp);
         
         String formattedTS = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(originalTS.getTime()).toUpperCase();
        try{
            
               System.out.println("Connected Successfully!");

               query = "INSERT INTO punch (terminalid, badgeid, originaltimestamp,"
                       + " punchtypeid) VALUES (?, ?, ?, ?)";
               
               pstUpdate = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
               
               pstUpdate.setInt(1,newTerminalId);
               pstUpdate.setString(2,newBadgeId );
               pstUpdate.setString(3,formattedTS);
               pstUpdate.setInt(4, punchType);
               
               updateCount = pstUpdate.executeUpdate();
               
               if(updateCount > 0){
                   
                   resultset = pstUpdate.getGeneratedKeys();
                   
                   if (resultset.next()){
                       
                       System.out.print("Update Successful!!");
                       id = resultset.getInt(1);
                   }
                   
               }
               
               
        } 
        catch (Exception e){
            System.err.println(e.toString());
        }
        
        finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
            if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
            
        }
        
        
        return id;
 
    }

    public ArrayList getDailyPunchList(Badge b, long ts){
        
        
        
        
         return null;

    }
     

 }
