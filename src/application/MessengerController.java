package application;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    /**
     * Login user then connect the user to the server
     * 
     * @param event
     */
    @SuppressWarnings("unused")
    @FXML
    void login(ActionEvent event) {
        userName = userNameInput.getText();
        pwd = passwordInput.getText();

        // TODO: login user, find user byusing user name from DB then check if pwd is correct
        // if login success, login user and connect to the server
        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            rooms.add(new Room("room " + i, 0));
        }
        
        User user = new User("test", "test");
        user.setRooms(rooms);
        this.showMainPane(user);

        // if login failed, ask user to re-enter the password
        if(false){
            passwordInput.setText("");
            Alert alert = new Alert(AlertType.ERROR, "Incorrect Password!");
            alert.show();
        }
    }

    /**
     * Display the main menu
     * 
     * @param user
     */
    private void showMainPane(User user) {
        loginPane.setVisible(false);
        mainPane.setVisible(true);

        nameLabel.setText("Current User: " + user.getName());
        
        // add the rooms to the list view
        List<Button> roomBtns = new ArrayList<Button>();
        for (Room room : user.getRooms()) {
            Button roomBtn = new Button(room.getName());
            roomBtn.setStyle("-fx-border:none -fx-background-color:trabsparent");         
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
        // TODO Auto-generated method stub
        mainPane.setVisible(false);
        chatPane.setVisible(true);
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

    public void disconnect() {
        // TODO Auto-generated method stub
        System.out.println("disconnect");
    }

}