package tas_fa18;

import java.sql.*;


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
      
            query = "SELECT * FROM badge b";
            pstSelect = conn.prepareStatement(query);
            System.err.println("Submitting Query ... ");

            hasresults = pstSelect.execute();

            System.err.println("Getting Results ...");
            
            while (hasresults || pstSelect.getUpdateCount() != -1){

                if (hasresults){

                   resultset = pstSelect.getResultSet();
                   
                   if (resultset.next()){
                        b.setID(resultset.getString("id"));
                        b.setDescription(resultset.getString("description"));
                   }
                  
            }
             
            hasresults = pstSelect.getMoreResults();

            System.out.println();
            conn.close();
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
      
            query = "SELECT * FROM punch p";
            pstSelect = conn.prepareStatement(query);
            System.err.println("Submitting Query ... ");

            hasresults = pstSelect.execute();

            System.err.println("Getting Results ...");
            
            while (hasresults || pstSelect.getUpdateCount() != -1){

                if (hasresults){

                   resultset = pstSelect.getResultSet();
                  
                   if (resultset.next()){
                        p.setID(resultset.getInt("id"));
                        p.setTerminalID(resultset.getString("terminalid"));
                        p.setBadgeID(resultset.getString("badgeid"));
                        p.setOriginalTimeStamp(resultset.getString("originaltimestamp"));
                        p.setPunchTypeID(resultset.getString("punchtypeid"));
                   }
                   
   
            }
             
            hasresults = pstSelect.getMoreResults();

            System.out.println();
            conn.close();
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
         
         Shift s = new Shift();
         
         try{
      
            query = "SELECT * FROM shift s";
            pstSelect = conn.prepareStatement(query);
            System.err.println("Submitting Query ... ");

            hasresults = pstSelect.execute();

            System.err.println("Getting Results ...");
            
            while (hasresults || pstSelect.getUpdateCount() != -1){

                if (hasresults){

                   resultset = pstSelect.getResultSet();
                  
                   if(resultset.next()){
                        s.setID(resultset.getInt("id"));
                        s.setDescription(resultset.getString("description"));
                        s.setStartTime(resultset.getString("start"));
                        s.setStopTime(resultset.getString("stop"));
                        s.setInterval(resultset.getString("interval"));
                        s.setGracePeriod(resultset.getString("graceperiod"));
                        s.setDock(resultset.getString("dock"));
                        s.setLunchStart(resultset.getString("lunchstart"));
                        s.setLunchStop(resultset.getString("lunchstop"));
                        s.setLunchDeduct(resultset.getString("lunchdeduct"));
                   
                   }
            }
                     
            
            hasresults = pstSelect.getMoreResults();


            System.out.println();
            conn.close();
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
            String query = "SELECT * FROM employee WHERE badgeid = " + id + "";
        
            pstSelect = conn.prepareStatement(query);
            System.err.println("Submitting Query ... ");

            hasresults = pstSelect.execute();

            System.err.println("Getting Results ...");
            
            while (hasresults || pstSelect.getUpdateCount() != -1){

                if (hasresults){

                   resultset = pstSelect.getResultSet();
                  
                   if(resultset.next()){
                       
                       shiftID = resultset.getInt("shiftid");
                       s.getShift(shiftID);
                   }
                   
                  
            }
                     
            
            hasresults = pstSelect.getMoreResults();


            System.out.println();
            conn.close();
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


