import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class User {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;

    public User(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){

        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner input = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = input.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void messageListener(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGc;
                while (socket.isConnected()) {
                    try {
                        messageFromGc = bufferedReader.readLine();
                        System.out.println(messageFromGc);

                    }catch (IOException e){
                        closeAll(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("What is your username: ");
        String username = input.nextLine();

        Socket socket = new Socket("localhost", 4567);
        User user = new User(socket, username);
        user.messageListener();
        user.sendMessage();
    }
}
