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

        while(isListening){
            try {
                msg = input.readLine();
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e){
                e.printStackTrace();
                break;
            }

            if(msg != null){
                // tokenize the msg, the first part is sender name and second part is msg
                StringTokenizer tokenizer = new StringTokenizer(msg, MESSAGE_SEPARATOR);

                // if the msg contains sender and msg
                if(tokenizer.countTokens() == 2){
                    // the first token is sender name, second is msg
                    messageListener.messageReceived(tokenizer.nextToken(), tokenizer.nextToken());
                } else {
                    // if the msg is equal to the disconnect string
                    if(msg.equalsIgnoreCase(MESSAGE_SEPARATOR + DISCONNECT_STRING)){
                        isListening = false;
                    }
                }
            }
        }

        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
