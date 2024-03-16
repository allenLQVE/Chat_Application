package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static application.SocketMessengerConstants.*;
// the server for one chat room
public class Server implements MessageListener{
    private ExecutorService serverExecutor; // excutor for the server

    public void startServer(){
        serverExecutor = Executors.newCachedThreadPool(); // create the new executor for the server

        try{
            // create a socket for waiting connection with maxiam amount 100
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT, 100);
            
            System.out.println("Server listening on port " + SERVER_PORT + " ...");

            // listen for connection
            while(true){
                // accept the new client connection
                Socket clientSocket = serverSocket.accept();

                // create a message receiver for the connected client
                serverExecutor.execute(new MessageReceiver(this, clientSocket));

                System.out.println("Connected by " + clientSocket.getInetAddress());
            }
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
        
    }

    // send a message to all the clients
    public void messageReceived(String sender, String msg){
        serverExecutor.execute(new MulticastSender((sender + MESSAGE_SEPARATOR + msg).getBytes()));
    }
}
