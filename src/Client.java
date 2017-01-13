/**
 * Created by dianyo on 2017/1/12.
 */
public class Client {
    private static void waitForExecute(ClientSocket clientSocket) {
        while (clientSocket.isExecuting()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String args[]) throws InterruptedException {
        //start GUI
        ClientGUI clientGUI = new ClientGUI();
        ClientSocket clientSocket = new ClientSocket();
        Thread t1 = new Thread(clientSocket);
        t1.start();
        String operation;

        String loginMsg = "";
        while (true) {
            clientGUI.displayFirstPage();
            operation = clientGUI.getFirstPageOp();
            if (operation.equals("login")) {
                clientSocket.sendLoginMsg(clientGUI.getAccount(), clientGUI.getPassword());
                Thread.sleep(100);
                waitForExecute(clientSocket);
                loginMsg = clientSocket.getLoginMsg();
                clientGUI.loginSuccess(loginMsg);
                System.out.println(operation + " " + loginMsg);
                if (loginMsg.equals("LOGIN SUCCESS")) break;
                System.exit(0);
            } else {
                clientGUI.register();
                clientSocket.sendRegisterMsg(clientGUI.getAccount(), clientGUI.getPassword());
                loginMsg = clientSocket.getRegisterMsg();
                clientGUI.registerSuccess(loginMsg);
            }
        }

        while (clientGUI.isExist()) {
            clientSocket.setAllChatRoom();
            waitForExecute(clientSocket);
            clientGUI.displayAllChat(clientSocket.getAllChatRoom());

            //open a chat room
            String roomName = clientGUI.getRoomName();
            clientSocket.setHistory(roomName);
            waitForExecute(clientSocket);
            clientGUI.displayChatRoom("history", clientSocket.getHistory(roomName));

            //start chat
            while (clientGUI.isChatExist()) {
                String msg = clientGUI.getMsg();
                if (!msg.equals(""))
                    clientSocket.sendMsg(msg, roomName);
                    waitForExecute(clientSocket);
                clientGUI.displayChatRoom("new", clientSocket.getHistory(roomName));
            }
        }

        clientSocket.close();

    }
}
