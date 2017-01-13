/**
 * Created by dianyo on 2017/1/12.
 */
public class Client {
    public static void main(String args[]) {
        //start GUI
        ClientGUI clientGUI = new ClientGUI();
        ClientSocket clientSocket = new ClientSocket();
        Thread t1 = new Thread(clientSocket);
        t1.start();
        String operation;

        String loginMsg = "";
        while (!loginMsg.equals("OK")) {
            clientGUI.displayFirstPage();
            operation = clientGUI.getFirstPageOp();
            if (operation.equals("login")) {
                clientSocket.sendLoginMsg(clientGUI.getAccount(), clientGUI.getPassword());
                loginMsg = clientSocket.getLoginMsg();
                clientGUI.loginSuccess(loginMsg);
            } else {
                clientGUI.register();
                clientSocket.sendRegisterMsg(clientGUI.getAccount(), clientGUI.getPassword());
                loginMsg = clientSocket.getRegisterMsg();
                clientGUI.registerSuccess(loginMsg);
            }
        }

        while (clientGUI.isExist()) {
            clientGUI.displayAllChat(clientSocket.getAllChatRoom());

            //open a chat room
            String roomName = clientGUI.getRoomName();
            clientGUI.displayChatRoom("history", clientSocket.getHistory(roomName));

            //start chat
            while (clientGUI.isChatExist()) {
                String msg = clientGUI.getMsg();
                if (!msg.equals(""))
                    clientSocket.sendMsg(msg, roomName);
                clientGUI.displayChatRoom("new", clientSocket.getHistory(roomName));
            }

        }

        clientSocket.close();

    }
}
