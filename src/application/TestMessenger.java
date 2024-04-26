package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TestMessenger {

    // private String userName;
    private MessageListener listener;
    private boolean isConnected = true;

    // get user from login info
    // TODO: remove testing value and get user from login function
    private User user;

    // for connceting all rooms
    private ArrayList<Integer> listen_ports = new ArrayList<Integer>(Arrays.asList(5555,5556));
    private ArrayList<Integer> send_ports = new ArrayList<Integer>(Arrays.asList(5554,5553));
    
    // for connecting single room
    private int listen_port = 5555;
    private int send_port = 5554;
    private int currRoom;

    public static void main(String[] args) {
        TestMessenger testMessenger = new TestMessenger();
        testMessenger.login();
        testMessenger.connectToServer();
        
    }

    private void connectToServer() {
        // connect to mutiple rooms at once
        ArrayList<MessageManager> managers = new ArrayList<MessageManager>();
        for (int i = 0; i < user.getRooms().size(); i ++) {
            MessageManager manager = new SocketMessageManager("localhost");
            managers.add(manager);
        }

        TestMessenger testMessenger = new TestMessenger();
        testMessenger.start(managers);

        // connect to one room at a time
        // MessageManager manager = new SocketMessageManager("localhost");

        // TestMessenger testMessenger = new TestMessenger();
        // testMessenger.start(manager);
    }

    public void login(){
        // TODO: login a user

        // user for test
        user = new User("Allen", "test");
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

            switch (input) {
                case "connect":
                    for (int i = 0; i < user.getRooms().size(); i ++) {
                        managers.get(i).connect(listener, user.getRooms().get(i).getPort());
                    }
                    
                    System.out.println("Connected to server");
                    // System.out.println("Please input user name");
                    // userName = keyboard.nextLine();
                    break;
                case "disconnect":
                    for (int i = 0; i < managers.size(); i++) {
                        managers.get(i).disconnect(listener);;
                    }
                    isConnected = false;
                default:
                    // TODO: the room sending the message should be retrive from the place user sent msg
                    System.out.println("Sending message to room");
                    int room = keyboard.nextInt();
                    keyboard.nextLine();
                    managers.get(room).sendMessage(user.getName(), input, send_ports.get(room), listen_ports.get(room));
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
                    // System.out.println("Please input user name");
                    // userName = keyboard.nextLine();
                    break;
                case "disconnect":
                    manager.disconnect(listener);
                    isConnected = false;
                default:
                    manager.sendMessage(user.getName(), input, send_ports.get(currRoom), listen_ports.get(currRoom));
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
