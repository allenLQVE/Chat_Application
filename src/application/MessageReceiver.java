package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import static application.SocketMessengerConstants.*;

public class MessageReceiver implements Runnable{
    private BufferedReader input;
    private MessageListener messageListener;
    private boolean isListening = true;

    public MessageReceiver(MessageListener listener, Socket clienSocket){
        // set listener to the new received message
        messageListener = listener;

        try {
            // reading from client will timeout in 5 sec
            clienSocket.setSoTimeout(5000);

            // create BufferedReader for reading incoming messages
            input = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String msg;

        // listen for incoming message
        while(isListening){
            // check if receive any message, timeout after 5s
            try {
                msg = input.readLine();
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e){
                e.printStackTrace();
                break;
            }

            // if message recieved
            if(msg != null){
                // tokenize the msg, seperate by the separator
                StringTokenizer tokenizer = new StringTokenizer(msg, MESSAGE_SEPARATOR);

                // if the msg contains sender, msg, and 2 ports
                if(tokenizer.countTokens() == 4){
                    // the first token is sender name, second is msg, follows by 2 port numbers
                    messageListener.messageReceived(tokenizer.nextToken(), tokenizer.nextToken(), Integer.valueOf(tokenizer.nextToken()), Integer.valueOf(tokenizer.nextToken()));
                } else {
                    // if the msg is equal to the disconnect string
                    if(msg.equalsIgnoreCase(MESSAGE_SEPARATOR + DISCONNECT_STRING)){
                        isListening = false;
                    }
                }
            }
        }

        // termination
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
