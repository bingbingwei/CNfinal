import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dianyo on 2017/1/13.
 */
public class ClientGUI implements Runnable{
    private JFrame frame;
    private String page;
    private String firstPageOp = "login";
    private String account = "Dianyo2";
    private String password = "12345";
    private String nickname = "Dianyo2";
    private Boolean frameExist = true;
    private String roomName = "測試1";
    private Boolean chatExist = true;
    private String newMsg = "test";
    private Boolean sending = true;
    private Boolean addNewChatRoom;
    private List<String> membersToNewChatRoom = new ArrayList<String>();
    private Boolean executing = false;
    private Boolean waiting = true;
    private String loginMsg = "";
    private Boolean openAChat = false;
    private List<String> allMsg;
    private List<String> allRoom;

    @Override
    public void run() {
        CNLine clientGUI = new CNLine(this);
    }


    public void register() {
        //Register GUI
    }

    public void loginSuccess(String msg) {
        this.loginMsg = msg;
    }
    public String getLoginMsg() {return  this.loginMsg;}
    public void registerSuccess(String msg) {
        //register Success or Failed GUI
        this.loginMsg = msg;
    }
    public void displayAllChat(List allRoom) {
        //display All Room (Room List)
        this.allRoom = allRoom;
    }
    public void displayChatRoom(String type, List msg) {
        //display the chat Room (Msg List)
        this.allMsg = msg;
    }
    public List<String> getAllMsg() {return  this.allMsg;}
    public String getFirstPageOp () {
        return this.firstPageOp;
    }
    public void setFirstPageOp(String op) {this.firstPageOp = op;}

    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) { this.account = account;}

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {this.password = password;}

    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) { this.nickname = nickname;}

    public String getRoomName() {
        return this.roomName;
    }
    public void setRoomName(String roomName) {this.roomName = roomName;}

    public Boolean isExist() {
        return this.frameExist;
    }
    public Boolean isChatExist() {
        return this.chatExist;
    }
    public String getMsg(){
        return this.newMsg;
    }

    public Boolean isSending() {
        return this.sending;
    }
    public void setSending() {this.sending = true;}
    public void setNotSending() {this.sending = false;}

    public Boolean waitAddNewChatRoom() {
        return this.addNewChatRoom;
    }
    public void setAddNewChatRoom() { this.addNewChatRoom = true;}
    public List<String> getNewRoomMember() {
        return this.membersToNewChatRoom;
    }
    public void setNewRoomMember(List<String> members) { this.membersToNewChatRoom = members;}

    public void setExecuting() { this.executing = true;}
    public void setNotExecuting() {this.executing = false;}
    public Boolean getExecuting() {return this.executing;}

    public Boolean isWaiting() {return this.waiting;}
    public void setWaiting() {this.waiting = true;}
    public void setNotWaiting() {this.waiting = false;}

    public Boolean getOpenAChat() {return this.openAChat;}
    public void setOpenAChat() {this.openAChat = true;}
    public void setNotOpenAChat() {this.openAChat = false;}
}
