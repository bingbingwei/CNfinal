import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestClient {
    public static Gson gson = new Gson();
    public static Socket socket = null;
    public static DataInputStream inputStream = null;
    public static DataOutputStream outputStream = null;
    public static void REG(String account, String password, String nickname){
        userinfo user = new userinfo();
        user.account = account;
        user.password = password;
        user.nickname = nickname;
        String msg = gson.toJson(user);
        try {
            outputStream.writeUTF("REGISTER");
            inputStream.readUTF();
            outputStream.writeUTF(msg);
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void LOG(String account,String pwd){
        userinfo user = new userinfo();
        user.account = account;
        user.password =pwd;
        String msg = gson.toJson(user);
        try {
            outputStream.writeUTF("LOGIN");
            inputStream.readUTF();
            outputStream.writeUTF(msg);
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void OpenChatRoom(String RoomName){
        try {
            outputStream.writeUTF("OPENCHATROOM");
            inputStream.readUTF();
            outputStream.writeUTF(RoomName);
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void JoinChatRoom(String RoomName){
        Scanner stdin = new Scanner(System.in);
        String member = null;
        List<String> members = new ArrayList<>();
        while(!(member = stdin.nextLine()).equals("END")){
            members.add(member);
        }
        String msg = gson.toJson(members);
        try {
            outputStream.writeUTF("JOIN");
            inputStream.readUTF();
            outputStream.writeUTF(msg);
            inputStream.readUTF();
            outputStream.writeUTF(RoomName);
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SendMessage(String account,String msg,String RoomName){
        Message message = new Message();
        message.account = account;
        message.msg = msg;
        String mess = gson.toJson(message);
        try {
            outputStream.writeUTF("SENDMESSAGE");
            inputStream.readUTF();
            outputStream.writeUTF(mess);
            inputStream.readUTF();
            outputStream.writeUTF(RoomName);
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void UpdateChatRoom(String account, String roomname){
        try {
            outputStream.writeUTF("UPDATECHATROOM");
            inputStream.readUTF();
            outputStream.writeUTF(account);
            inputStream.readUTF();
            outputStream.writeUTF(roomname);
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void GetNewMessage(String account,String roomname,String type){
        try {
            outputStream.writeUTF("GETNEWMESSAGE");
            inputStream.readUTF();
            outputStream.writeUTF(account);
            inputStream.readUTF();
            outputStream.writeUTF(roomname);
            inputStream.readUTF();
            outputStream.writeUTF(type);
            String ret =inputStream.readUTF();
            if(type.equals("GetCount"))
                System.out.println(ret);
            else{
                List<Message> list = gson.fromJson(ret,new TypeToken<List<Message>>(){}.getType());
                for(int i=0;i<list.size();i++)
                    System.out.println(list.get(i).nickname+":"+list.get(i).msg+"     "+list.get(i).timestamp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void GetRoomInfo(String account){
        try {
            outputStream.writeUTF("GETROOMINFO");
            inputStream.readUTF();
            outputStream.writeUTF(account);
            System.out.println(inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        try {
            socket = new Socket("127.0.0.1",9999);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            //REG("cute","12345","Annie");
            //LOG("abcde","12345");
            //OpenChatRoom("測試3");
            //JoinChatRoom("測試3");
            //SendMessage("dad","I love you~~","測試1");
            //GetNewMessage("bingbingwei","測試1","GetMessage");
            //GetRoomInfo("bingbingwei");
            //UpdateChatRoom("bingbingwei","測試1");
            outputStream.writeUTF("LOGOUT");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
}
