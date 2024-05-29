import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class UserHandler implements Runnable{
    public static ArrayList<UserHandler> userHandlerArrayList = new ArrayList<>();  //keep track of users
    //loop thru list and send msg to all users
    private Socket socket; //start connection between client and server
    private BufferedReader bufferedReader; //read msgs sent by client
    private BufferedWriter bufferedWriter; //show msgs sent by client
    private String clientUser;

    public UserHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //read msgs
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //send msgs
            this.clientUser =  bufferedReader.readLine();
            userHandlerArrayList.add(this);

            showMessage("Server: " + clientUser + " has joined the chat");

        }catch (IOException e){
           closeAll(socket, bufferedReader, bufferedWriter);
        }
    }
    @Override
    public void run() {
        String ClientMsg;
        while(socket.isConnected()){
            try{
                ClientMsg = bufferedReader.readLine(); //this needs to be run on a seperate thread so it doesnt pause the whole program
                showMessage(ClientMsg);
            }catch (IOException e){
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void showMessage(String messageToSend){
        for(UserHandler userHandler: userHandlerArrayList){ 
            try{
                if(!userHandler.clientUser.equals(clientUser)){
                    userHandler.bufferedWriter.write(messageToSend);
                    userHandler.bufferedWriter.newLine();
                    userHandler.bufferedWriter.flush();

                }
            }catch (IOException e){
                closeAll(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeUserHandler(){
        userHandlerArrayList.remove(this);
        showMessage("Server: " + clientUser + " has left");
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeUserHandler();
        try {
            if(bufferedReader!=null){
                bufferedReader.close();
            }

            if(bufferedWriter!=null){
                bufferedWriter.close();
            }

            if (socket!=null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
