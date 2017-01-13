import javax.swing.*;
import java.awt.*;

/**
 * Created by dianyo on 2017/1/13.
 */
public class ClientGUI {
    private JFrame frame;
    private String page;
    private String firstPageOp = "login";
    private String account = "bingbingwei";
    private String password = "12345";
    private Boolean frameExist = true;
    private String roomName = "test1";
    private Boolean chatExist = true;
    private String newMsg = "test";
    public ClientGUI() {
        this.frame = new JFrame("CNLine");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void displayFirstPage() {
//        this.page = "first";
//        (new FirstPage()).addComponentsToPane(frame.getContentPane());
//
//        frame.pack();
//        frame.setVisible(true);
    }

    public void register() {
        //Register GUI
    }

    public void loginSuccess(String msg) {
        //login Success or Failed GUI
    }
    public void registerSuccess(String msg) {
        //register Success or Failed GUI
    }
    public void displayAllChat(List allRoom) {
        //display All Room (Room List)
    }
    public void displayChatRoom(String type, List msg) {
        //display the chat Room (Msg List)
    }
    public String getFirstPageOp () {
        return this.firstPageOp;
    }

    public String getAccount() {
        return this.account;
    }
    public String getPassword() {
        return this.password;
    }
    public String getRoomName() {
        return this.roomName;
    }
    public Boolean isExist() {
        return this.frameExist;
    }
    public Boolean isChatExist() {
        return this.chatExist;
    }
    public String getMsg(){
        return this.newMsg;
    }
}
