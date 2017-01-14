/**
 * Created by dianyo on 2017/1/12.
 */
public class Client {
    private static void waitForExecute(ClientSocket clientSocket, ClientGUI clientGUI) {
        clientSocket.setExecuting();
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
        clientGUI.setWaiting();
        while (clientGUI.isWaiting()) {
            try {
                System.out.println("waiting GUI");
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
        Thread t2 = new Thread(clientGUI);
        t1.start();
        t2.start();
        String operation;

        String loginMsg = "";

        waitForGUI(clientGUI);
        operation = clientGUI.getFirstPageOp();
        if (operation.equals("login")) {
            clientSocket.sendLoginMsg(clientGUI.getAccount(), clientGUI.getPassword());
            waitForExecute(clientSocket, clientGUI);
            System.out.println("finish");
            loginMsg = clientSocket.getLoginMsg();
            System.out.println(loginMsg + "Main");
            clientGUI.loginSuccess(loginMsg);
            System.out.println(operation + " " + loginMsg);
//            if (loginMsg.equals("LOGIN SUCCESS"));
        } else {
            waitForGUI(clientGUI);
            System.out.println("after waiting");
            //clientGUI.register();
            clientSocket.sendRegisterMsg(clientGUI.getAccount(), clientGUI.getPassword(), clientGUI.getNickname());
            waitForExecute(clientSocket, clientGUI);
            loginMsg = clientSocket.getRegisterMsg();
            clientGUI.registerSuccess(loginMsg);
            clientGUI.setNotExecuting();
            System.out.println("afterExecute");
            System.out.println(loginMsg);
//            if (loginMsg.equals("SUCCESS"));
        }

        System.out.println("afterLogin");

        while (clientGUI.isExist()) {
            waitForGUI(clientGUI);
            if(clientGUI.getNeedAllRoom()) {
                clientSocket.setAllChatRoom();
                waitForExecute(clientSocket, clientGUI);
                System.out.println("after All room Main");
                clientGUI.displayAllChat(clientSocket.getAllChatRoom());
            }

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
                clientGUI.setChatExist();
            }

            //start chat
            while (clientGUI.isChatExist()) {

                clientSocket.setHistory(roomName);
                waitForExecute(clientSocket, clientGUI);
                clientGUI.displayChatRoom("new", clientSocket.getHistory(roomName));
                System.out.println("OK");

                waitForGUI(clientGUI);
                if (clientGUI.isSending()) {
                    String msg = clientGUI.getMsg();
                    clientSocket.sendMsg(msg, roomName);
                    waitForExecute(clientSocket, clientGUI);
                }

                break;
            }
        }

        clientSocket.close();

    }
}
