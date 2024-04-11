package application;

import java.util.Scanner;

public class TestMessenger {

    private String userName;
    private MessageListener listener;
    private boolean isConnected = true;
    private int listen_port = 5555;
    private int send_port = 5554;

    public static void main(String[] args) {
        MessageManager manager = new SocketMessageManager("localhost");

        TestMessenger testMessenger = new TestMessenger();
        testMessenger.start(manager);
    }

    private void start(MessageManager manager) {
        listener = new MyMessageListener();

        Scanner keyboard = new Scanner(System.in);

        while(isConnected){
            System.out.println("Please input text:");
            String input = keyboard.nextLine();

            switch (input) {
                case "connect":
                    
                    manager.connect(listener, listen_port);

                    System.out.println("Connected to server");
                    System.out.println("Please input user name");
                    userName = keyboard.nextLine();
                    break;
                case "disconnect":
                    manager.disconnect(listener);
                    isConnected = false;
                default:
                    manager.sendMessage(userName, input, send_port, listen_port);
                    System.out.println("Message send");
            }
        }

        keyboard.close();
    }

    private class MyMessageListener implements MessageListener{

        @Override
        public void messageReceived(String sender, String msg, int send_port, int listen_port) {
            System.out.println(sender + ": " + msg);
        }
        
    }
}
