import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadingServer {
    private boolean Threadalive = true;
    private int port = 9999;
    private ServerSocket socket;

    public MultiThreadingServer(int port){
        this.port = port;
    }
    public void run(){
        openServerSocket();
        while(ThreadAlive()){
            Socket clientsocket = null;
            try {clientsocket = this.socket.accept();}
            catch (IOException e) {e.printStackTrace();}
            new Thread(new Worker(clientsocket)).start();
        }
        System.out.println("Server stopped.");
    }
    private void openServerSocket(){
        try {this.socket = new ServerSocket(this.port);}
        catch (IOException e) {e.printStackTrace();}
    }
    private boolean ThreadAlive(){return this.Threadalive;}
    public void stop(){
        this.Threadalive = false;
        try {this.socket.close();}
        catch (IOException e) {e.printStackTrace();}
    }
}
