package codes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector{

    private Connection conn;
    public DatabaseConnector(){
        this.conn = null;
        this.connectDb();
    }
    private void connectDb(){
        String url = "jdbc:mariadb://localhost:3306/lotto";
        try{
            this.conn = DriverManager.getConnection(url,"root","");
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    public void closeConnection(){
        try{
            if(conn != null){
                this.conn.close();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public Connection getConnection(){
        return this.conn;
    }
}