import com.sun.rowset.CachedRowSetImpl;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import javax.sql.RowSet;
import java.sql.*;


/**
 * Created by Tony Jiang on 2016/11/16.
 */
public class DbConnection {
    private String dbUrl = "";
    private String dbUsername = "";
    private String dbPassword = "";




    public DbConnection(String dbUrl,
                        String dbUsername, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;

    }

    public Result query(String sql_statement) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        PreparedStatement pstmt = connection.prepareStatement(sql_statement);
        ResultSet rs = pstmt.executeQuery();
        Result result = ResultSupport.toResult(rs);
        connection.close();
        return result;
    }
    public int update(String sql_statement) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        PreparedStatement pstmt = connection.prepareStatement(sql_statement);
        int ret = pstmt.executeUpdate();
        connection.close();
        return ret;
    }

    public void test() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            String sql = "select * from user";
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                String uname = rs.getString("username");
                String upsw = rs.getString("password");
                System.out.println(uname + "\t" + upsw);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException!!!");
        } catch (SQLException e) {
            System.out.println("SQLException!!!");
        }
    }
}
