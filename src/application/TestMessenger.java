package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TestMessenger {

    private String userName;
    private MessageListener listener;
    private boolean isConnected = true;
    private ArrayList<Integer> listen_ports = new ArrayList<Integer>(Arrays.asList(5555,5556));
    private ArrayList<Integer> send_ports = new ArrayList<Integer>(Arrays.asList(5554,5553));
    private int listen_port = 5555;
    private int send_port = 5554;

    public static void main(String[] args) {
        // MessageManager manager = new SocketMessageManager("localhost");

        ArrayList<MessageManager> managers = new ArrayList<MessageManager>();
        MessageManager manager = new SocketMessageManager("localhost");
        managers.add(manager);
        manager = new SocketMessageManager("localhost");
        managers.add(manager);

        TestMessenger testMessenger = new TestMessenger();
        // testMessenger.start(manager);
        testMessenger.start(managers);
    }

    /**
     * Create a process to handle actions from user
     * 
     * @param ArrayList<MessageManager> a list of managers that handle message and connect/disconnect
     */
    private void start(ArrayList<MessageManager> managers) {
        listener = new MyMessageListener();

        Scanner keyboard = new Scanner(System.in);

        while(isConnected){
            System.out.println("Please input text:");
            String input = keyboard.nextLine();

            switch (input) {
                case "connect":
                    
                    for (int i = 0; i < managers.size(); i++) {
                        managers.get(i).connect(listener, listen_ports.get(i));
                    }

                    System.out.println("Connected to server");
                    System.out.println("Please input user name");
                    userName = keyboard.nextLine();
                    break;
                case "disconnect":
                    for (int i = 0; i < managers.size(); i++) {
                        managers.get(i).disconnect(listener);;
                    }
                    isConnected = false;
                default:
                    System.out.println("Sending message to room");
                    int room = keyboard.nextInt();
                    managers.get(room).sendMessage(userName, input, send_ports.get(room), listen_ports.get(room));
            }
        }

        keyboard.close();
    }

    /**
     * Create a process to handle actions from user
     * 
     * @param MessageManager manager that handle message and connect/disconnect
     */
    @SuppressWarnings("unused")
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
