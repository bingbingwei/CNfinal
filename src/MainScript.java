public class MainScript {
    public static void main(String args[]){
        int port = Integer.valueOf(9999);
        MultiThreadingServer server = new MultiThreadingServer(port);
        System.out.println("Server Start");
        server.run();


        //server.stop();
    }
}