import com.google.gson.Gson;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by dianyo on 2017/1/12.
 */
public class ClientSocket implements Runnable {
    private String instruction;
    private Socket socket;
    private String account;
    private String password;
    private String loginMsg;
    private String registerMsg;
    private Gson gson = new Gson();
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String msgToServer;
    private userinfo user = new userinfo();
    private Boolean isRunning = true;
    private String roomName;
    private List allRoom;
    private List allMsg;

    public ClientSocket() {
        try {
            this.socket = new Socket("127.0.0.1", 9999);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    public void sendLoginMsg(String account, String password) {
        this.instruction = "Login";
        this.account = account;
        this.password = password;
    }
    public void sendRegisterMsg(String account, String password) {
        this.account = account;
        this.password = password;
        this.instruction = "Register";
    }
    public String getLoginMsg() {
        if (this.loginMsg.equals("LOGIN SUCCESS")) return "OK";
        return loginMsg;
    }
    public String getRegisterMsg() {
        if (this.registerMsg.equals("SUCCESS")) return "OK";
        return registerMsg;
    }
    public void sendMsg(String msg, String roomName) {
        this.instruction = "Send Msg";
        this.roomName = roomName;
        this.msgToServer = msg;
    }
    public List getAllChatRoom() {
        this.instruction = "All Room";
        while (!instruction.equals("None")) {
            try { Thread.sleep(10);
            } catch (Exception e) { System.out.println(e.getMessage()); }
        }
        return this.allRoom;
    }
    public List getHistory(String roomName) {
        this.instruction = "All Msg";
        this.roomName = roomName;
        while (!instruction.equals("None")) {
            try { Thread.sleep(10);
            } catch (Exception e) { System.out.println(e.getMessage()); }
        }
        return  this.allMsg;
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
        msgToServer = gson.toJson(user);
        try {
            outputStream.writeUTF("REGISTER");
            inputStream.readUTF();
            outputStream.writeUTF(msgToServer);
            this.registerMsg = inputStream.readUTF();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private  void sendMsgToServer() {
        Message message = new Message();
        message.account = account;
        message.msg = this.msgToServer;
        msgToServer = gson.toJson(message);
        try {
            outputStream.writeUTF("SENDMESSAGE");
            inputStream.readUTF();
            outputStream.writeUTF(msgToServer);
            inputStream.readUTF();
            outputStream.writeUTF(roomName);
            System.out.println("send Msg result = " + inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void getAllMsgFromServer() {
    }

    private  void getAllRoomFromServer() {
        try {
            outputStream.writeUTF("GETROOMINFO");
            inputStream.readUTF();
            outputStream.writeUTF(account);
            System.out.println(inputStream.readUTF());
        } catch ( Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void run() {
        while (isRunning) {
            switch (instruction) {
                case "Login":
                    login();
                    break;
                case "Register":
                    register();
                    break;
                case "Send Msg":
                    sendMsgToServer();
                    break;
                case "All Room":
                    getAllRoomFromServer();
                    break;
                case "All Msg":

            }
            instruction = "None";
        }
    }
}
