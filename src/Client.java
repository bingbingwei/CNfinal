/**
 * Created by dianyo on 2017/1/12.
 */
public class Client {
    private static void waitForExecute(ClientSocket clientSocket) {
        while (clientSocket.isExecuting()) {
            try {
                Thread.sleep(100);
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
                waitForExecute(clientSocket);
                loginMsg = clientSocket.getLoginMsg();
                clientGUI.loginSuccess(loginMsg);
                System.out.println(operation + " " + loginMsg);
                if (loginMsg.equals("LOGIN SUCCESS")) break;
            } else {
                clientGUI.register();
                clientSocket.sendRegisterMsg(clientGUI.getAccount(), clientGUI.getPassword(), clientGUI.getNickname());
                waitForExecute(clientSocket);
                loginMsg = clientSocket.getRegisterMsg();
                clientGUI.registerSuccess(loginMsg);
                System.out.println(loginMsg);
                if (loginMsg.equals("SUCCESS")) break;
            }
        }

        while (clientGUI.isExist()) {
            clientSocket.setAllChatRoom();
            waitForExecute(clientSocket);
            clientGUI.displayAllChat(clientSocket.getAllChatRoom());

            if(clientGUI.waitAddNewChatRoom()) {
                System.out.println("in");
                clientSocket.setNewChatRoom(clientGUI.getNewRoomMember(), clientGUI.getRoomName());
                waitForExecute(clientSocket);
                clientGUI.displayAllChat(clientSocket.getAllChatRoom());
            }
            //open a chat room
            String roomName = clientGUI.getRoomName();
            clientSocket.setHistory(roomName);
            waitForExecute(clientSocket);
            clientGUI.displayChatRoom("history", clientSocket.getHistory(roomName));

            //start chat
            while (clientGUI.isChatExist()) {
                String msg = clientGUI.getMsg();
                if (clientGUI.isSending()) {
                    clientSocket.sendMsg(msg, roomName);
                    waitForExecute(clientSocket);
                }
                clientSocket.setHistory(roomName);
                waitForExecute(clientSocket);
                clientGUI.displayChatRoom("new", clientSocket.getHistory(roomName));
                System.out.println("OK");
                break;
            }
            break;
        }

        clientSocket.close();

    }
}
