import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dianyo on 2017/1/12.
 */
public class ClientSocket implements Runnable {
    private String instruction = "None";
    private Socket socket;
    private String account;
    private String password;
    private String nickname;
    private String loginMsg = "";
    private String registerMsg = "";
    private Gson gson = new Gson();
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String msgToServer;
    private userinfo user = new userinfo();
    private Boolean isRunning = true;
    private String roomName;
    private List allRoom;
    private List allMsg;
    private Boolean executing = false;
    private Message msgStructure = new Message();
    private String type = "123";
    private String chatMsgToServer;
    private List<String> memberToNewChatRoom;
    private Boolean executed = false;

    public ClientSocket() {
        try {
            this.socket = new Socket("10.5.5.36", 9999);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void sendLoginMsg(String account, String password) {
        this.instruction = "Login";
        this.executed = false;
        this.account = account;
        this.password = password;
    }
    public void sendRegisterMsg(String account, String password, String nickname) {
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.instruction = "Register";
        this.executed = false;
    }
    public String getLoginMsg() {
        System.out.println(this.loginMsg);
        return this.loginMsg;
    }
    public String getRegisterMsg() {
        System.out.println(this.registerMsg);
        return this.registerMsg;
    }
    public Boolean isExecuting() {
        return this.executing;
    }
    public void setExecuting() {this.executing = true;}
    public void sendMsg(String msg, String roomName) {
        this.instruction = "SendMsg";
        this.executed = false;
        this.roomName = roomName;
        this.chatMsgToServer = msg;
    }
    public void setAllChatRoom() {
        this.instruction = "AllRoom";
    }
    public List getAllChatRoom() {
        return this.allRoom;
    }
    public void setHistory(String roomName) {
        this.instruction = "AllMsg";
        this.executed = false;
        this.roomName = roomName;
    }
    public List getHistory(String roomName) {
        return this.allMsg;
    }
    public void setNewChatRoom(List<String> members, String roomName) {
        this.instruction = "AddNew";
        this.executed = false;
        this.roomName = roomName;
        this.memberToNewChatRoom = members;
    }
    public void close() {
        try {
            this.inputStream.close();
            this.outputStream.close();
            this.socket.close();
            this.isRunning = false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void login() {
        user.account = this.account;
        user.password = this.password;
        msgToServer = gson.toJson(user);
        try {
            outputStream.writeUTF("LOGIN");
            inputStream.readUTF();
            outputStream.writeUTF(msgToServer);
            this.loginMsg = inputStream.readUTF();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private  void register() {
        user.account = this.account;
        user.password = this.password;
        user.nickname = this.nickname;
        msgToServer = gson.toJson(user);
        try {
            outputStream.writeUTF("REGISTER");
            inputStream.readUTF();
            outputStream.writeUTF(msgToServer);
            registerMsg = inputStream.readUTF();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private  void sendMsgToServer() {
        Message message = new Message();
        message.account = account;
        message.msg = this.chatMsgToServer;
        String tmp = gson.toJson(message);
        System.out.println(tmp);
        try {
            outputStream.writeUTF("SENDMESSAGE");
            inputStream.readUTF();
            outputStream.writeUTF(tmp);
            inputStream.readUTF();
            outputStream.writeUTF(roomName);
            System.out.println("send Msg result = " + inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void getAllMsgFromServer() {
        try {
            outputStream.writeUTF("GETNEWMESSAGE");
            inputStream.readUTF();
            outputStream.writeUTF(account);
            inputStream.readUTF();
            outputStream.writeUTF(roomName);
            inputStream.readUTF();
            outputStream.writeUTF(type);
            String ret =inputStream.readUTF();
            if(type.equals("GetCount"))
                System.out.println(ret);
            else{
                this.allMsg = gson.fromJson(ret,new TypeToken<List<Message>>(){}.getType());
            }
            outputStream.writeUTF("UPDATECHATROOM");
            inputStream.readUTF();
            outputStream.writeUTF(account);
            inputStream.readUTF();
            outputStream.writeUTF(roomName);
            this.allMsg = gson.fromJson(inputStream.readUTF(), new TypeToken<List<Message>>(){}.getType());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private  void getAllRoomFromServer() {
        try {
            outputStream.writeUTF("GETROOMINFO");
            inputStream.readUTF();
            outputStream.writeUTF(account);
            allRoom = gson.fromJson(inputStream.readUTF(),new TypeToken<List<roominfo>>(){}.getType());
        } catch ( Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addNewChatRoom() {
        try {
            System.out.println("here");
            outputStream.writeUTF("OPENCHATROOM");
            inputStream.readUTF();
            outputStream.writeUTF(roomName);
            inputStream.readUTF();

            List<String> members = new ArrayList<>();
            System.out.println("here put self");
            members.add(this.account);
            for (int i = 0; i < memberToNewChatRoom.size() ; i++)
                members.add(memberToNewChatRoom.get(i));
            String msg = gson.toJson(members);
            outputStream.writeUTF("JOIN");
            inputStream.readUTF();
            outputStream.writeUTF(msg);
            inputStream.readUTF();
            outputStream.writeUTF(roomName);
            inputStream.readUTF();
        } catch (Exception e) { System.out.println(e.getMessage());}

    }
    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (instruction!= "None")
                System.out.println("instruction is " + instruction);
            switch (instruction) {
                case "Login":
                    this.executing = true;
                    System.out.println("login Socket");
                    login();
                    this.executed = true;
                    break;
                case "Register":
                    this.executing = true;
                    System.out.println("register Socket");
                    register();
                    this.executed = true;
                    break;
                case "SendMsg":
                    this.executing = true;
                    sendMsgToServer();
                    this.executed = true;
                    break;
                case "AllRoom":
                    this.executing = true;
                    getAllRoomFromServer();
                    this.executed = true;
                    break;
                case "AllMsg":
                    this.executing = true;
                    System.out.println("allMsg Socket");
                    getAllMsgFromServer();
                    this.executed = true;
                    break;
                case "AddNew":
                    this.executing = true;
                    System.out.println("addNew socket");
                    addNewChatRoom();
                    this.executed = true;
                    break;
                case "None":
                    break;
            }
            if (this.executed) {
                this.instruction = "None";
                this.executing = false;
                this.executed = false;
            }
        }

    }
}
