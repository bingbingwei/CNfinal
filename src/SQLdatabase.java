import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLdatabase {
    public Connection conn = null;
    public String user = "bingbingwei";
    public String pwd = "da20jeja1996";
    public Gson gson = new Gson();
    public SQLdatabase() throws ClassNotFoundException{
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Conntection Sucess MySQLToJava");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cnfinal",
                    user,pwd);
            System.out.println("Connecting MySQL");
        }catch(Exception e) {e.printStackTrace();}
    }
    public String EXECUTE(String query){
        try {
            Statement st = conn.createStatement();
            st.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return "EXECUTE "+query+" FAIL";
        }
        return "SUCCESS";
    }
    public String SELECT(String query,String target){
        try {
            Statement st = conn.createStatement();
            st.execute(query);
            ResultSet rs = st.getResultSet();
            rs.next();
            return rs.getString(target);
        } catch (SQLException e) {
            return "";
        }
    }
    public int SELECTINT(String query,int target){
        try {
            Statement st = conn.createStatement();
            st.execute(query);
            ResultSet rs = st.getResultSet();
            rs.next();
            return rs.getInt(target);
        } catch (SQLException e) {
            return -1;
        }
    }
    public String SELECTLIST(String query, String type){
        String ret = null;
        if(type.equals("NEWMESSAGES")){
            try {
                Statement st = conn.createStatement();
                st.execute(query);
                ResultSet rs = st.getResultSet();
                List<Message> list = new ArrayList<>();
                while(rs.next()){
                    Message single = new Message();
                    single.account = rs.getString("account");
                    single.nickname = rs.getString("nickname");
                    single.msg = rs.getString("msg");
                    single.timestamp = rs.getTimestamp("time");
                    list.add(single);
                }
                ret = gson.toJson(list);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("ROOMINFO")){
            try {
                Statement st = conn.createStatement();
                st.execute(query);
                ResultSet rs = st.getResultSet();
                List<roominfo> list = new ArrayList<>();
                while(rs.next()){
                    roominfo single = new roominfo();
                    single.roomname = rs.getString("roomname");
                    list.add(single);
                }
                ret = gson.toJson(list);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    public String LOGIN(String account, String password){
        String query = "SELECT * FROM chatroom.userinfo\n"+
                "WHERE account = \""+account+"\"";
        String account_cmp = SELECT(query,"account");
        String pwd_cmp = SELECT(query,"password");
        if(account_cmp.equals(""))
            return "NO ACCOUNT";
        else if(!pwd_cmp.equals(password))
            return "WRONG PASSWORD";
        else
            return "LOGIN SUCCESS";
    }
}