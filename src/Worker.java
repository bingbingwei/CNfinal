import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.Timestamp;
import java.util.List;

public class Worker implements Runnable {
    private Socket socket = null;
    private String threadID;
    private DataInputStream inputstream;
    private DataOutputStream outputstream;
    private SQLdatabase database;
    private Gson gson = new Gson();

    public Worker(Socket socket){
        this.socket = socket;
    }
    public void run(){
        //do the works
        threadID = Thread.currentThread().getName();
        String input;

        try {
            database = new SQLdatabase();
            inputstream = new DataInputStream(socket.getInputStream());
            outputstream = new DataOutputStream(socket.getOutputStream());
            while(!(input=inputstream.readUTF()).equals("LOGOUT")){
                userinfo info;
                String result,account;
                System.out.println(input);
                switch(input){
                    case "REGISTER":
                        outputstream.writeUTF("ok");
                        info = gson.fromJson(inputstream.readUTF(),userinfo.class);
                        String query = "INSERT INTO userinfo (account,password,nickname)\n"+
                                "VALUES ('"+info.account+"','"+info.password+"','"+info.nickname+"')";
                        outputstream.writeUTF(database.EXECUTE(query));
                        break;
                    case "LOGIN":
                        outputstream.writeUTF("ok");
                        info = gson.fromJson(inputstream.readUTF(),userinfo.class);
                        outputstream.writeUTF(database.LOGIN(info.account,info.password));
                        break;
                    case "OPENCHATROOM":
                        outputstream.writeUTF("ok");
                        result = OpenChatRoom(inputstream.readUTF());
                        outputstream.writeUTF(result);
                        break;
                    case "JOIN":
                        outputstream.writeUTF("ok");
                        List<String> member = gson.fromJson(inputstream.readUTF(),new TypeToken<List<String>>(){}.getType());
                        outputstream.writeUTF("Which room?");
                        result = Join(inputstream.readUTF(),member);
                        outputstream.writeUTF(result);
                        break;
                    case "SENDMESSAGE":
                        outputstream.writeUTF("ok");
                        Message message = gson.fromJson(inputstream.readUTF(),Message.class);
                        outputstream.writeUTF("to where?");
                        result = SendMessage(message,inputstream.readUTF());
                        outputstream.writeUTF(result);
                        break;
                    case "UPDATECHATROOM":
                        outputstream.writeUTF("ok");
                        account = inputstream.readUTF();
                        outputstream.writeUTF("to where?");
                        result = UpdateChatRoom(account,inputstream.readUTF());
                        outputstream.writeUTF(result);
                        break;
                    case "GETNEWMESSAGE":
                        outputstream.writeUTF("ok");
                        account = inputstream.readUTF();
                        outputstream.writeUTF("which?");
                        String roomname = inputstream.readUTF();
                        outputstream.writeUTF("got it");
                        String type;
                        if(inputstream.readUTF().equals("GetCount"))
                            type = "INT";
                        else
                            type = "String";
                        String ret = GetNewMessage(account,roomname,type);
                        outputstream.writeUTF(ret);
                        break;
                }
            }
        } catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        System.out.println("Thread Close");
        Thread.currentThread().interrupt();
        return;
    }
    public String OpenChatRoom(String ChatRoomName){
        String query = "CREATE TABLE chatroom." +ChatRoomName+
                "  (id INT NOT NULL AUTO_INCREMENT,\n" +
                "  account VARCHAR(45) NOT NULL,\n" +
                "  nickname VARCHAR(45) NOT NULL,\n" +
                "  msg VARCHAR(45) NOT NULL,\n" +
                "  time TIMESTAMP(6) NOT NULL,\n" +
                "  PRIMARY KEY (id));\n";
        return database.EXECUTE(query);
    }
    public String Join(String ChatRoomName,List<String> member){
        String query =null;
        boolean flag = false;
        String ret = "";
        for(int i=0;i<member.size();i++){
            String nickname = member.get(i);
            query = "SELECT * FROM cnfinal.userinfo\n" +
                    "WHERE nickname = \""+nickname+"\"";
            String account = database.SELECT(query,"account");
            if(account.equals("")){
                if(!flag){
                    flag=true;
                    ret = nickname;}
                else
                    ret+= (", "+nickname);
                continue;
            }
            query = "INSERT INTO user2room (account,roomname)\n" +
                    "VALUES ('"+account+"','"+ChatRoomName+"')";
            database.EXECUTE(query);
        }
        if(flag)
            return ret+" failed to join "+ChatRoomName;
        else
            return "SUCCESS";
    }
    public String SendMessage(Message message,String roomname){
        java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        String query = "SELECT nickname FROM userinfo\n" +
                "WHERE account = \""+message.account+"\"";
        String nickname = database.SELECT(query,"nickname");
        query = "INSERT INTO chatroom."+roomname+"(account, nickname, msg, time)\n" +
                "VALUES ('"+message.account+"','"+nickname+"','"+message.msg+"','"+ timestamp+"')";
        return database.EXECUTE(query);
    }
    public String UpdateChatRoom(String account, String roomname){
        java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        String query = "UPDATE cnfinal.user2room SET time = \""+timestamp+"\"\n" +
                "WHERE account = \""+account+"\" AND roomname = \""+roomname+"\"";
        database.EXECUTE(query);
        String getMaxId = "SELECT MAX(id) FROM chatroom."+roomname;
        query = "UPDATE cnfinal.user2room SET lastenter = "+database.SELECTINT(getMaxId,1)+"\n" +
                "WHERE account = \""+account+"\" AND roomname = \""+roomname+"\"";
        return database.EXECUTE(query);
    }
    public String GetNewMessage(String account,String roomname,String type){
        String query ="SELECT * FROM chatroom."+roomname+"\n" +
                "WHERE id>"+database.SELECT("SELECT lastenter FROM user2room WHERE account = \""
                +account+"\" AND roomname =\""+roomname+"\"","lastenter");
        String strmsgs = database.SELECTLIST(query,"NEWMESSAGES");
        List<Message> msgs = gson.fromJson(strmsgs,new TypeToken<List<Message>>(){}.getType());
        if(type.equals("INT"))
            return Integer.toString(msgs.size());
        else
            return strmsgs;
    }
}
