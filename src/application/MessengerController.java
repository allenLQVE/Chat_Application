package application;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class MessengerController {

    // chat panel components
    @FXML
    private BorderPane chatPane;

    // login panel components
    @FXML
    private BorderPane loginPane;
    @FXML
    private Button loginBtn;
    @FXML
    private Button newUserBtn;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private TextField userNameInput;

    // main panel components
    @FXML
    private BorderPane mainPane;
    @FXML
    private Label nameLabel;
    @FXML
    private ListView<Button> roomList;

    private String userName;
    private String pwd;
    private User user;
    private MessageListener listener;
    private Dictionary<String, MessageManager> managers;

    /**
     * Login user then connect the user to the server
     * 
     * @param event
     */
    @FXML
    void login(ActionEvent event) {
        userName = userNameInput.getText();
        pwd = passwordInput.getText();

        // TODO: login user, find user by using user name from DB then check if pwd is correct
        // if login success, login user and connect to the server
        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            rooms.add(new Room("room " + i, i + 1000));
        }
        
        user = new User("test", "test");
        user.setRooms(rooms);
        
        this.connect();
        this.showMainPane();

        // if login failed, ask user to re-enter the password
        passwordInput.setText("");
        Alert alert = new Alert(AlertType.ERROR, "Incorrect Password!");
        alert.show();
        
    }

    private void connect() {
        // connect to mutiple rooms at once
        managers = new Hashtable<String, MessageManager>();
        for (int i = 0; i < user.getRooms().size(); i ++) {
            MessageManager manager = new SocketMessageManager("localhost");
            manager.connect(listener, user.getRooms().get(i).getPort());
            managers.put(user.getRooms().get(i).getName(), manager);
        }

        listener = new MyMessageListener();
    }

    private class MyMessageListener implements MessageListener{
        @Override
        public void messageReceived(String sender, String msg, int send_port, int listen_port) {
            // TODO: add new message to the room
            System.out.println(sender + ": " + msg);
        }
        
    }

    /**
     * Display the main menu
     * 
     * @param user
     */
    private void showMainPane() {
        loginPane.setVisible(false);
        mainPane.setVisible(true);

        nameLabel.setText("Current User: " + user.getName());
        
        // add the rooms to the list view
        List<Button> roomBtns = new ArrayList<Button>();
        for (Room room : user.getRooms()) {
            Button roomBtn = new Button(room.getName());
            roomBtn.setStyle("-fx-border:none; -fx-background-color:transparent;");         
            roomBtn.setOnMouseClicked(e -> openChatRoom(room));
            roomBtns.add(roomBtn);
        }

        roomList.setItems(FXCollections.observableArrayList(roomBtns));
    }

    /**
     * Display the view of a chat room
     * 
     * @param room
     */
    private void openChatRoom(Room room) {
        // TODO: load message from the room and allow user to return, send new messeage, and show who is in the room
        mainPane.setVisible(false);
        chatPane.setVisible(true);
    }

    /**
     * Add new chating room to the list
     * 
     * @param event
     */
    @FXML
    void addRoom(ActionEvent event) {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Please enter the port for the room.");
            dialog.setTitle("Add Room");
            Optional<String> option = dialog.showAndWait();

            int port = Integer.parseInt(option.get());

            // TODO: if room exist add the room to user, else create new room
            // Room room = new Room("name", port);
            // user.addRoom(room);
            // ObservableList<Button> rooms = roomList.getItems();
                
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @FXML
    void removeRoom(ActionEvent event){
        // TODO: remove user from room and room from user, if room is empty, remove room
    }

    /**
     * Create a new user by input user name and password
     * 
     * @param event
     */
    @FXML
    void createNewUser(ActionEvent event) {
        // TODO: check if user exist by checking the name, if not create a new user by the password and username
        userName = userNameInput.getText();
        pwd = passwordInput.getText();

        // if user name exist
        userNameInput.setText("");
        passwordInput.setText("");
        Alert alert = new Alert(AlertType.ERROR, "User Already Exist!");
        alert.show();

        // if user name not exist, create new user and log in user with the new name and password
        this.login(event);
    }

    private void sendMessage(String msg, Room room){
        managers.get(room.getName()).sendMessage(user.getName(), msg, 0, room.getPort());
    }

    public void disconnect() {
        // TODO: disconnect user from all the rooms
        for (Room room : user.getRooms()) {
            managers.get(room.getName()).disconnect(listener);
        }
    }
}