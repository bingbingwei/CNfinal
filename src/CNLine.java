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
        while (controller.getExecuting()) {
            try {
                Thread.sleep(30);
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
                if (!loginMsg.equals("LOGIN SUCCESS")){
                    JOptionPane.showMessageDialog(frame, loginMsg);
                } else {
                    //Get nickname for user
                    user_nickname = "Me";

                    //Get previous messages
                    // String[] history =
                    // listModel.addElement("dddd");
                    //Add to listmodel
                    userList.setModel(listModel);
                    CardLayout c = (CardLayout) panel1.getLayout();
                    c.last(frame.getContentPane());
                }
            }
        });
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIDataController.setAccount(loginaccount.getText());
                GUIDataController.setPassword(String.valueOf(passwordField1.getPassword()));
                GUIDataController.setNickname(signupNickname.getText());
                GUIDataController.setNotWaiting();
                waitControllerExecute(GUIDataController);
                GUIDataController.setWaiting();
                //Send user info to server
                String registerMsg = GUIDataController.getLoginMsg();
                if (!registerMsg.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(frame, registerMsg);
                } else {
                    CardLayout c = (CardLayout) panel1.getLayout();
                    c.first(frame.getContentPane());
                }
            }
        });
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    super.mouseClicked(e);
                    int index = userList.getSelectedIndex();
                    selected_user = listModel.getElementAt(index);
                    GUIDataController.setRoomName(selected_user);
                    GUIDataController.setOpenAChat();
                    waitControllerExecute(GUIDataController);
                    List<String> allMsg = GUIDataController.getAllMsg();
                    for (int i = 0; i < allMsg.size(); i++) {
                        messages.append(allMsg.get(i));
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
                    listModel.addElement(input);
                    List<String> members = new ArrayList<String>();
                    members.add(input);
                    GUIDataController.setAddNewChatRoom();
                    GUIDataController.setRoomName(input);
                    GUIDataController.setNewRoomMember(members);
                    GUIDataController.setNotWaiting();
                    waitControllerExecute(GUIDataController);
                    GUIDataController.setWaiting();
                }
            }
        });
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mesg = newmessage.getText();
                System.out.print(mesg);
                newmessage.setText("");
                if(mesg.endsWith("\n"))
                    messages.append(user_nickname+" : " + mesg);
                else
                    messages.append(user_nickname+" : " + mesg + "\n");
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
