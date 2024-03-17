package application;

import java.util.Scanner;

public class TestMessenger {

    private String userName;
    private MessageListener listener;
    private boolean isConnected = true;

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
                    manager.connect(listener);

                    System.out.println("Please input user name");
                    userName = keyboard.nextLine();
                    break;
                case "disconnect":
                    manager.disconnect(listener);
                    isConnected = false;
                default:
                    manager.sendMessage(userName, input);
            }
        }

        keyboard.close();
    }

    private class MyMessageListener implements MessageListener{

        @Override
        public void messageReceived(String sender, String msg) {
            System.out.println(sender + ": " + msg);
        }
        
    }
}
