package controller;

import fxapp.MainFXApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Control;
import model.Title;
import model.User;
import model.UserProfile;

import java.io.IOException;

/**
 * Created by Allen on 10/2/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class EditProfileController {
    @FXML
    TextField profileName;
    @FXML TextField profileAddress;
    @FXML TextField profileEmail;
    @FXML TextField profileContact;
    @FXML ComboBox<Title> profileTitle;
    @FXML Button profileSubmit;
    @FXML Button profileCancel;
    private User user;
    private UserProfile userProfile;
    private final MainFXApplication mainApp = new MainFXApplication();

    /**
     * called automatically in order to populate the titleBox with Titles and set reports
     */
    @FXML
    private void initialize() {
        ObservableList<Title> list = FXCollections.observableArrayList(Title.values());
        profileTitle.setItems(list);
    }

    /**
     * sets user from login screen
     * @param user user
     * @throws NullPointerException if user is null
     */
    public void setUser(User user) throws NullPointerException {
        this.user = user;
        profileName.setText(user.getProfile().getName());
        profileAddress.setText(user.getProfile().getAddress());
        profileEmail.setText(user.getProfile().getEmail());
        profileContact.setText(user.getProfile().getNumber());
    }

    /**
     * handles edit profile submission, return to user screen
     * @param event submit profile
<<<<<<< HEAD
     * @throws java.io.IOException can't access userdatabase
=======
     * @throws java.io.IOException can't access user database
>>>>>>> 6775af3109914a236ad2ea5d79a036320be77030
     */
    @FXML
    protected void handleSubmit(ActionEvent event) throws java.io.IOException {
        String alert = isValidProfileEdit();
        if (alert != null) {
            String alertData[] = alert.split("/");
            sendAlert(alertData[0],alertData[1],alertData[2]);
        } else {
            swapToUserScreen(new User(user, userProfile));
        }

    }

    /**
     * handles cancel button response, return to user screen
     * @param event cancel submission
     * @throws java.io.IOException problem handling cancel request
     */
    @FXML
    protected void handleCancel(ActionEvent event) throws java.io.IOException {
        swapToUserScreen(user);
    }

    /**
     * swap to user screen on stage
     * @param user user to pass
     * @throws IOException error accessing database
     */
    @FXML
    private void swapToUserScreen(User user) throws IOException {
        Stage stage = (Stage) profileCancel.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/UserScreen.fxml"));
        Parent root = fxmlLoader.load();
        UserScreenController controller = fxmlLoader.getController();
        controller.setUser(user);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * checks if valid edit profile changes
     * @return true if valid profile edit
     */
    public String isValidProfileEdit() {
        String errorMessage = "";
        String name = profileName.getText();
        String email = profileEmail.getText();
        String addr = profileAddress.getText();
        String contact = profileContact.getText();
        Title title = profileTitle.getValue();
        if ((name == null) || name.isEmpty() || name.contains("/")) {
            errorMessage += "Please enter a valid name!\n";
        }
        if ((email == null) || email.isEmpty() || email.contains("/") || !email.contains("@")) {
            errorMessage += "Please enter a valid email!\n";
        }
        if ((addr == null) || addr.isEmpty() || addr.contains("/")) {
            errorMessage += "Please enter a valid address!\n";
        }
        if ((contact == null) || contact.isEmpty() || contact.contains("/")) {
            errorMessage += "Please enter a valid contact number!\n";
        }
        if (profileTitle.getValue() == null) {
            errorMessage += "You have selected an invalid title.\n";
        }
        if (!errorMessage.isEmpty()) {
            return "ERROR/Invalid Profile Edit/" + errorMessage;
        } else {
            //sends user profile data to database saving method
            return sendProfileToDatabase(new UserProfile(name, email, addr, contact, title));
        }
    }

    /**
     * sends changed profile data to database
     * @param userProfile profile to append to current user and add to database
     * @return whether or not the profile was able to be added to database
     */
    private String sendProfileToDatabase(UserProfile userProfile) {
        try {
            user = new User(user, userProfile);
            user.setName(userProfile.getName());
            if (Control.getInstance().getDatabase().editUser(user.getUsername(), user)) {
                sendAlert("INFORMATION","Edit Profile Success","The user profile has been updated successfully.");
                return null;
            } else {
                return "ERROR/Edit Profile Error/The user profile could not be saved to database.";
            }
        } catch (NullPointerException e) {
            // creates alert window notifying of user not existing in database
            return "ERROR/Invalid Profile Change/You have left some fields blank.";
        }
    }

    /**
     * Sends alert of invalid submission
     * @param type type of alert
     * @param title alert title
     * @param text alert message
     */
    private void sendAlert(String type, String title, String text) {
        Alert alert = new Alert(Alert.AlertType.valueOf(type));
        alert.setTitle(title);
        alert.initOwner(profileName.getScene().getWindow());
        alert.setContentText(text);
        alert.showAndWait();
    }
}
