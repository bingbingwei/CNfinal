/**
 * Created by dianyo on 2017/1/12.
 */
public class Client {
    private static void waitForExecute(ClientSocket clientSocket, ClientGUI clientGUI) {
        clientGUI.setExecuting();
        while (clientSocket.isExecuting()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        clientGUI.setNotExecuting();
    }
    private static void waitForGUI(ClientGUI clientGUI) {
        while (clientGUI.isWaiting()) {
            try {
                Thread.sleep(30);
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
        Thread t2 = new Thread(clientGUI);
        t1.start();
        t2.start();
        String operation;

        String loginMsg = "";
        while (true) {

            waitForGUI(clientGUI);
            operation = clientGUI.getFirstPageOp();
            if (operation.equals("login")) {
                clientSocket.sendLoginMsg(clientGUI.getAccount(), clientGUI.getPassword());
                waitForExecute(clientSocket, clientGUI);
                loginMsg = clientSocket.getLoginMsg();
                clientGUI.loginSuccess(loginMsg);
                System.out.println(operation + " " + loginMsg);
                if (loginMsg.equals("LOGIN SUCCESS")) break;
            } else {
                waitForGUI(clientGUI);
                //clientGUI.register();
                clientSocket.sendRegisterMsg(clientGUI.getAccount(), clientGUI.getPassword(), clientGUI.getNickname());
                waitForExecute(clientSocket, clientGUI);
                loginMsg = clientSocket.getRegisterMsg();
                clientGUI.registerSuccess(loginMsg);
                System.out.println(loginMsg);
                if (loginMsg.equals("SUCCESS")) break;
            }
        }

        while (clientGUI.isExist()) {
            clientSocket.setAllChatRoom();
            waitForExecute(clientSocket, clientGUI);
            clientGUI.displayAllChat(clientSocket.getAllChatRoom());

            if(clientGUI.waitAddNewChatRoom()) {
                System.out.println("in");
                clientSocket.setNewChatRoom(clientGUI.getNewRoomMember(), clientGUI.getRoomName());
                waitForExecute(clientSocket, clientGUI);
                clientGUI.displayAllChat(clientSocket.getAllChatRoom());
            }
            //open a chat room
            String roomName = "";
            if (clientGUI.getOpenAChat()) {
                roomName = clientGUI.getRoomName();
                clientSocket.setHistory(roomName);
                waitForExecute(clientSocket, clientGUI);
                clientGUI.displayChatRoom("history", clientSocket.getHistory(roomName));
            }

            //start chat
            while (clientGUI.isChatExist()) {
                String msg = clientGUI.getMsg();
                if (clientGUI.isSending()) {
                    clientSocket.sendMsg(msg, roomName);
                    waitForExecute(clientSocket, clientGUI);
                }
                clientSocket.setHistory(roomName);
                waitForExecute(clientSocket, clientGUI);
                clientGUI.displayChatRoom("new", clientSocket.getHistory(roomName));
                System.out.println("OK");
                break;
            }
            break;
        }

        clientSocket.close();

    }
}
