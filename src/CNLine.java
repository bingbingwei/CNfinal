import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
//import java.security.MessageDigest;

public class CNLine {
    private JPanel panel1;
    private JTextField loginaccount;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton registerButton;
    private JButton backButton;
    private JButton enterButton;
    private JTextField signupAccount;
    private JPasswordField passwordField2;
    private JPanel Chatroom;
    private JPanel Login;
    private JPanel MainInterface;
    private JPanel Register;
    private JLabel accountLabel;
    private JLabel passwordLabel;
    private JLabel passwordAgainLabel;
    private JButton signupButton;
    private JList userList;
    private JTextArea messages;
    private JButton addButton;
    private JTextPane newmessage;
    private JTextField signupNickname;
    private ClientGUI GUIDataController;

    private static DefaultListModel<String> listModel = new DefaultListModel<String>();
    private String selected_user, user_nickname;
    private static JFrame frame = new JFrame("CNLine");
    private void waitControllerExecute(ClientGUI controller) {
        controller.setExecuting();
        while (controller.getExecuting()) {
            try {
                System.out.println("waiting execute");
                Thread.sleep(100);
            } catch (Exception e) { System.out.println(e.getMessage()); }
        }
    }
    private void setActions() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout c = (CardLayout)panel1.getLayout();
                c.next(frame.getContentPane());
                c.next(frame.getContentPane());
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIDataController.setAccount(loginaccount.getText());
                GUIDataController.setPassword(String.valueOf(passwordField1.getPassword()));
                GUIDataController.setFirstPageOp("login");
                GUIDataController.setNotWaiting();
                waitControllerExecute(GUIDataController);
                GUIDataController.setWaiting();
                String loginMsg = GUIDataController.getLoginMsg();
                System.out.println(loginMsg + "GUI");
                if (!loginMsg.equals("LOGIN SUCCESS")){
                    JOptionPane.showMessageDialog(frame, loginMsg);
                } else {
                    //Get nickname for user
                    user_nickname = GUIDataController.getNickname();

                    //Get previous messages
                    // String[] history =
                    // listModel.addElement("dddd");
                    //Add to listmodel
                    GUIDataController.setAllRoom();
                    GUIDataController.setNotWaiting();
                    waitControllerExecute(GUIDataController);
                    GUIDataController.setWaiting();
                    List<roominfo> rooms;
                    rooms = GUIDataController.getAllRoom();
                    if (rooms != null) {
                        for (int i = 0; i < rooms.size(); i++) {
                            listModel.addElement(rooms.get(i).roomname);
                        }
                    }
                    userList.setModel(listModel);
                    CardLayout c = (CardLayout) panel1.getLayout();
                    c.last(frame.getContentPane());
                }
            }
        });
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIDataController.setAccount(signupAccount.getText());
                GUIDataController.setPassword(String.valueOf(passwordField2.getPassword()));
                GUIDataController.setNickname(signupNickname.getText());
                GUIDataController.setNotWaiting();
                waitControllerExecute(GUIDataController);
                //Send user info to server
                String registerMsg = GUIDataController.getLoginMsg();
                if (!registerMsg.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(frame, registerMsg);
                } else {
                    CardLayout c = (CardLayout) panel1.getLayout();
                    c.last(frame.getContentPane());
                }
            }
        });
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    super.mouseClicked(e);
                    System.out.println("click2");
                    GUIDataController.setChatExist();
                    int index = userList.getSelectedIndex();
                    selected_user = listModel.getElementAt(index);
                    GUIDataController.setRoomName(selected_user);
                    GUIDataController.setOpenAChat();
                    GUIDataController.setNotWaiting();
                    waitControllerExecute(GUIDataController);
                    GUIDataController.setWaiting();
                    System.out.println("getAllMsg");
                    List<Message> allMsg = GUIDataController.getAllMsg();
                    if (allMsg != null) {
                        for (int i = 0; i < allMsg.size(); i++) {
                            messages.append(allMsg.get(i).nickname + " : " + allMsg.get(i).msg +"\n");
                        }
                    }

                    CardLayout c = (CardLayout)panel1.getLayout();
                    c.previous(frame.getContentPane());
                    c.previous(frame.getContentPane());
                }
        }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIDataController.setChatClose();
                messages.setText("");
                CardLayout c = (CardLayout)panel1.getLayout();
                c.last(frame.getContentPane());
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Type user's name: ");
                if(input == null){
                    JOptionPane.showMessageDialog(frame ,"Cancell.");
                }else {
                    JOptionPane.showMessageDialog(frame, "Start the conversation with " + input + ".");
                    List<String> members = new ArrayList<String>();
                    members.add(input);
                    GUIDataController.setNewRoomMember(members);
                }
                String input2 = JOptionPane.showInputDialog("Type your room name: ");
                if (input2 == null) {
                    JOptionPane.showMessageDialog(frame, "Cancell");
                }  else {
                    listModel.addElement(input2);
                    JOptionPane.showMessageDialog(frame, "Name is " + input2 + ".");
                    GUIDataController.setRoomName(input2);
                }
                GUIDataController.setAddNewChatRoom();
                GUIDataController.setNotWaiting();
                waitControllerExecute(GUIDataController);
                GUIDataController.setWaiting();
            }
        });
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIDataController.setSending();
                String mesg = newmessage.getText();
                System.out.print(mesg);
                GUIDataController.setSendingMsg(mesg);
                GUIDataController.setNotWaiting();
                waitControllerExecute(GUIDataController);
                GUIDataController.setWaiting();

                newmessage.setText("");
                messages.setText("");
                GUIDataController.setNotWaiting();
                waitControllerExecute(GUIDataController);
                GUIDataController.setWaiting();
                System.out.println("getAllMsg");
                List<Message> allMsg = GUIDataController.getAllMsg();
                if (allMsg != null) {
                    for (int i = 0; i < allMsg.size(); i++) {
                        messages.append(allMsg.get(i).nickname + " : " + allMsg.get(i).msg +"\n");
                    }
                }

            }
        });
    }

    public CNLine(ClientGUI GUIDataController) {
        this.GUIDataController = GUIDataController;
        setActions();
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        GUIDataController.setWaiting();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        userList = new JList<String>(listModel);
    }
}
