import java.sql.Connection;
import java.sql.DriverManager;


public class Connect {
    private static Connection con;
        static{
            try{
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Driver Ok");
                con=DriverManager.getConnection("jdbc:mysql://localhost:3308/java_project","root","");
                System.out.println("Connected");
            }catch(Exception e){
                e.printStackTrace();
            }
            
    }
   
    public static Connection getConnection() {
        return con;
    }

    
}
