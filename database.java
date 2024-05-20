
package nds.pkg2;

import java.sql.Connection;
import java.sql.DriverManager;

public class database {
    public static Connection connectDB(){
        try{
           Class.forName("com.mysql.jdbc.Driver");
           Connection connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/nds","root","rimsha@2005");
           return connect;
        }
        catch(Exception e){
            e.printStackTrace();}
            return null;
            
        
    }
}
