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
    private int currRoom;

    public static void main(String[] args) {
        // connect to mutiple rooms at once
        // ArrayList<MessageManager> managers = new ArrayList<MessageManager>();
        // MessageManager manager = new SocketMessageManager("localhost");
        // managers.add(manager);
        // manager = new SocketMessageManager("localhost");
        // managers.add(manager);

        // TestMessenger testMessenger = new TestMessenger();
        // testMessenger.start(managers);

        // connect to one room at a time
        MessageManager manager = new SocketMessageManager("localhost");

        TestMessenger testMessenger = new TestMessenger();
        testMessenger.start(manager);
    }

    /**
     * Create a process to handle actions from user.
     * Connect to all the rooms at once.
     * Have mutiple connections to the server.
     * 
     * @param ArrayList<MessageManager> a list of managers that handle message and connect/disconnect
     */
    private void start(ArrayList<MessageManager> managers) {
        listener = new MyMessageListener();

        Scanner keyboard = new Scanner(System.in);

        while(isConnected){
            System.out.println("Please input text:");
            String input = keyboard.nextLine();
            int room;

            switch (input) {
                case "connect":
                    System.out.println("Connect to room");
                    room = keyboard.nextInt();
                    keyboard.nextLine();
                    managers.get(room).connect(listener, listen_ports.get(room));
                    
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
                    room = keyboard.nextInt();
                    keyboard.nextLine();
                    managers.get(room).sendMessage(userName, input, send_ports.get(room), listen_ports.get(room));
            }
        }

        keyboard.close();
    }

    /**
     * Create a process to handle actions from user.
     * Connect only to the selected room.
     * Can only connect to one room at a time.
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
                    System.out.println("Connect to room");
                    currRoom = keyboard.nextInt();
                    keyboard.nextLine();
                    
                    manager.connect(listener, listen_ports.get(currRoom));
                    
                    System.out.println("Connected to room" + currRoom);
                    System.out.println("Please input user name");
                    userName = keyboard.nextLine();
                    break;
                case "disconnect":
                    manager.disconnect(listener);
                    isConnected = false;
                default:
                    manager.sendMessage(userName, input, send_ports.get(currRoom), listen_ports.get(currRoom));
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
