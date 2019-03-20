
package cs310.team.project;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Cs310TeamProject {

    public static void main(String[] args) {
        GregorianCalendar expected = new GregorianCalendar();
        GregorianCalendar actual = new GregorianCalendar();
        
        expected.setTimeInMillis(1537289973000L);
        actual.setTimeInMillis(1537297173000L);
        
        
        
        System.out.println(new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(expected.getTime()).toUpperCase());
        System.out.println(new SimpleDateFormat("EEE MM/dd/YYYY HH:mm:ss").format(actual.getTime()).toUpperCase());
        
    }
    
}
