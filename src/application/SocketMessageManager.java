package application;

import static application.SocketMessengerConstants.DISCONNECT_STRING;
import static application.SocketMessengerConstants.SERVER_PORT;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SocketMessageManager implements MessageManager{

    private Socket clienSocket;  // socket for outgoing message
    private String serverAddress;
    private PacketReceiver receiver; // for receiving muticaset messages
    private boolean isConnected = false;
    private ExecutorService serverExcutor; // excutor for server

    public SocketMessageManager(String serverAddress){
        this.serverAddress = serverAddress;
        serverExcutor = Executors.newCachedThreadPool();
    }

    @Override
    public void connect(MessageListener listener, int listen_port) {
        // return if already connected
        if(isConnected){
            return;
        }

        try {
            clienSocket = new Socket(InetAddress.getByName(serverAddress), SERVER_PORT);

            // create runnable for receiveing messages
            receiver = new PacketReceiver(listener, listen_port);
            serverExcutor.execute(receiver);
            isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect(MessageListener listener) {
        // return if not connected yet
        if(!isConnected){
            return;
        }

        try {
            // notify server for disconnection
            Runnable disconnecter = new MessageSender(clienSocket, "", DISCONNECT_STRING, 0, 0);

            @SuppressWarnings("rawtypes")
            Future disconnecting = serverExcutor.submit(disconnecter);
            disconnecting.get(); // wait for the result of sending disconnecter

            receiver.stopListening();
            clienSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        isConnected = false;
    }

    @Override
    public void sendMessage(String from, String msg, int multicast_send_port, int multicast_listen_port) {
        // return if not connected
        if(!isConnected){
            return;
        }

        serverExcutor.execute(new MessageSender(clienSocket, from, msg, multicast_send_port, multicast_listen_port));
    }
    
}
