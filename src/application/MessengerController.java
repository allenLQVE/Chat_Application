package application;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class MessengerController {

    // chat panel components
    @FXML
    private BorderPane chatPane;
    @FXML
    private Button returnBtn;
    @FXML
    private ListView<String> userList;
    @FXML
    private Label roomLabel;
    @FXML
    private TextArea msgTextDisplay;
    @FXML
    private TextField msgTextInput;
    @FXML
    private Button sendMsgBtn;

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
    private Room currRoom;
    private MessageListener listener  = new MyMessageListener();
    private Dictionary<String, MessageManager> managers;

    // TODO: array for testing, should be replace by database
    private ArrayList<User> userDB;
    private ArrayList<Room> roomDB;
 
    /**
     * create user and room db for testing
     */
    private void createDumyDB(){
        User user1 = new User("Allen", "1234");
        User user2 = new User("Bob", "1234");
        User user3 = new User("Cirby", "1234");

        userDB = new ArrayList<User>();
        roomDB = new ArrayList<Room>();
        for (int i = 0; i < 3; i++) {
            Room room = new Room("room " + i, i + 1000);
            room.addUser(user1);
            room.addUser(user2);
            room.addUser(user3);
            roomDB.add(room);
        }

        user1.setRooms(new ArrayList<Room>(roomDB));
        user2.setRooms(new ArrayList<Room>(roomDB));
        user3.setRooms(new ArrayList<Room>(roomDB));

        user2.removeRoom(roomDB.get(1));
        roomDB.get(1).removeUser(user2);

        userDB.add(user1);
        userDB.add(user2);
        userDB.add(user3);
    }

    // Methods for Main Panel ---------------------------------------------------
    /**
     * Login user then connect the user to the server
     * 
     * @param event
     */
    @FXML
    void login(ActionEvent event) {
        userName = userNameInput.getText();
        pwd = passwordInput.getText();

        // TODO: should be replaced with real database
        createDumyDB();

        user = this.login(userName, pwd);

        // if login success, login user and connect to the server
        if(user == null){
            // if login failed, ask user to re-enter the password
            passwordInput.setText("");
            Alert alert = new Alert(AlertType.ERROR, "Incorrect Password!");
            alert.show();
        } else {
            this.connect();
            this.showMainPane();
        }
    }

    /**
     * check if user in the database
     * 
     * @param userName
     * @param pwd
     * @return
     */
    private User login(String userName, String pwd) {
        // TODO: change to use DB later
        for (User user : userDB) {
            if(user.getName().equals(userName) && user.checkPassword(pwd)){
                return user;
            }
        }
        return null;
    }

    /**
     * connect to the server
     */
    private void connect() {
        // connect to mutiple rooms at once
        managers = new Hashtable<String, MessageManager>();
        for (int i = 0; i < user.getRooms().size(); i ++) {
            MessageManager manager = new SocketMessageManager("localhost");

            manager.connect(listener, user.getRooms().get(i).getPort());
            managers.put(user.getRooms().get(i).getName(), manager);
        }
    }

    private class MyMessageListener implements MessageListener{
        @Override
        public void messageReceived(String sender, String msg, int send_port, int listen_port) {
            // if the currRoom is the room receving the msg
            if(currRoom != null && currRoom.getPort() == listen_port){
                currRoom.addContent(sender + ": " + msg);
                msgTextDisplay.appendText(sender + ": " + msg + "\n");
            } else {
                for (Room room : user.getRooms()) {
                    if(room.getPort() == listen_port){
                        room.addContent(sender + ": " + msg);
                    }
                }
            }
            
            System.out.println(sender + ": " + msg);
        }
        
    }

    /**
     * Display the view of a chat room
     * 
     * @param room
     */
    private void openChatRoom(Room room) {
        currRoom = room;
        
        // set up the chat panel
        List<String> userNames = new ArrayList<String>();
        for (User user : room.getUsers()) {
            userNames.add(user.getName());
        }

        userList.setItems(FXCollections.observableArrayList(userNames));
        roomLabel.setText(room.getName());
        for (String msg : room.getContents()) {
            msgTextDisplay.appendText(msg + "\n");
        }

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
            Room newRoom = null;
            for (Room room : roomDB) {
                // if room already exist return
                if(user.getRooms().contains(room)){
                    return;
                }

                // add the new room if the room already exist
                if(room.getPort() == port){
                    newRoom = room;
                }
            }

            // get create a new room and save to DB
            if(newRoom == null){
                dialog = new TextInputDialog();
                dialog.setHeaderText("Please enter the name for the room.");
                dialog.setTitle("Add Room");
                option = dialog.showAndWait();

                String roomName = option.get();
                
                newRoom = new Room(roomName, port);
            }
            
            user.addRoom(newRoom);
            newRoom.addUser(user);

            // update main panel
            showMainPane();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @FXML
    void removeRoom(ActionEvent event){
        // TODO: remove user from room and room from user, if room is empty, remove room
    }

    public void disconnect() {
        // TODO: disconnect user from all the rooms
        for (Room room : user.getRooms()) {
            managers.get(room.getName()).disconnect(listener);
        }
    }

    // Methods for Chat Panel ---------------------------------------------------
    @FXML
    void returnToMain(ActionEvent event) {
        msgTextInput.clear();
        msgTextDisplay.clear();
        currRoom = null;

        chatPane.setVisible(false);
        mainPane.setVisible(true);
    }

    @FXML
    void sendMsg(ActionEvent event) {
        managers.get(currRoom.getName()).sendMessage(user.getName(), msgTextInput.getText(), 0, currRoom.getPort());
        msgTextInput.setText("");
    }

    // Methods for Login Panel ---------------------------------------------------
    /**
     * Display the main menu
     */
    private void showMainPane() {
        loginPane.setVisible(false);
        mainPane.setVisible(true);

        nameLabel.setText(user.getName());
        
        // add the rooms to the list view
        List<Button> roomBtns = new ArrayList<Button>();
        for (Room room : user.getRooms()) {
            Button roomBtn = new Button(room.getName());
            roomBtn.setStyle("-fx-border:none; -fx-background-color:transparent;");         
            roomBtn.setOnMouseClicked(e -> openChatRoom(room));
            roomBtn.setMaxWidth(Integer.MAX_VALUE);
            roomBtns.add(roomBtn);
        }

        roomList.setItems(FXCollections.observableArrayList(roomBtns));
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
}