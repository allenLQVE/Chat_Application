package application;

import static application.SocketMessengerConstants.MESSAGE_SEPARATOR;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;

public class MessageSender implements Runnable{

    private Socket clientSocket;
    private String msg;

    public MessageSender(Socket socket, String sender, String msg, int send_port, int listen_port){
        clientSocket = socket;
        this.msg = sender + MESSAGE_SEPARATOR + msg + MESSAGE_SEPARATOR + send_port + MESSAGE_SEPARATOR + listen_port;
    }

    @Override
    public void run() {
        try {
            @SuppressWarnings("resource")
            Formatter output = new Formatter(clientSocket.getOutputStream());
            output.format("%s", msg + "\n"); // send the msg
            output.flush(); // clean the buffer
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
