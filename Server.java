import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket; //listens for incoming connections

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{

            while(!serverSocket.isClosed()){ //while socket isnt closed wait for client to connect
                Socket socket =  serverSocket.accept(); //program halted until client connects
                System.out.println("A client has connected");
                UserHandler userHandler = new UserHandler(socket); //class to communicate w' user
                Thread thread = new Thread(userHandler);
                thread.start();
            }

        }catch (IOException e){

        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket!=null){ //prevents nullpointer exception
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{

        ServerSocket serverSocket = new ServerSocket(4567);
        Server server = new Server(serverSocket);
        server.startServer();

    }

}
