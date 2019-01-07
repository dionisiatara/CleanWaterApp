package controller;

import fxapp.MainFXApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;
import model.Control;

import java.io.IOException;

/**
 * Created by Taiga on 10/1/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class UserScreenController {
    @FXML private Label welcomeMessage;
    @FXML Button logoutButton;
    @FXML
    Button editProfileButton;
    @FXML
    Button submitReportButton;
    @FXML
    Button submitQualityReport;
    @FXML
    Button viewMapButton;
    @FXML
    Button historyGraphButton;
    @FXML Label emailLabel;
    @FXML
    Label addressLabel;
    @FXML
    Label contactLabel;
    @FXML ListView<String> reportListView = new ListView<>();
    @FXML TabPane tabPane;
    @FXML Tab reportsTab;
    @FXML
    Tab profileTab;
    @FXML Text reportsCategories;
    private ObservableList<WaterSourceReport> sourceReports;
    private ObservableList<WaterQualityReport> qualityReports;
    private final ObservableList<String> reportStrings = FXCollections.observableArrayList();
    private User user;
    private final MainFXApplication mainApp = new MainFXApplication();

    /**
     * called automatically to assign report lists and fill in list view with current reports
     */
    @FXML
    private void initialize() {
        model.Control.getInstance().restoreWqReports();
        model.Control.getInstance().restoreWsReports();
        sourceReports = model.Control.getInstance().getDatabase().getWsReports();
        qualityReports = model.Control.getInstance().getDatabase().getWqReports();
        mainApp.setQualityReports(qualityReports);
        mainApp.setSourceReports(sourceReports);
        for (WaterSourceReport report : sourceReports) {
            reportStrings.add(report.toString());
        }
        for (WaterQualityReport report : qualityReports) {
            reportStrings.add(report.toString());
        }
        reportListView.setItems(reportStrings);
    }

    /**
     * sets user from login screen
     * @param user user
     * @throws NullPointerException if user is null
     */
    public void setUser(User user) throws NullPointerException {
        this.user = user;
        try {
            if ((user.getProfile().getName() != null) && (user.getProfile().getTitle() != null) && (user.getName() != null)) {
                welcomeMessage.setText("Welcome, " + user.getProfile().getTitle().toString()
                        + ". " + user.getName() + "!");
            } else if (user.getProfile().getName() != null) {
                welcomeMessage.setText("Welcome, " + user.getProfile().getName() + "!");
            } else if (user.getName() != null) {
                welcomeMessage.setText("Welcome, " + user.getName() + "!");
            } else if (user.getUsername() != null) {
                welcomeMessage.setText("Welcome, " + user.getUsername() + "!");
            }
            emailLabel.setText("Email: " + user.getProfile().getEmail());
            addressLabel.setText("Address: " + user.getProfile().getAddress());
            contactLabel.setText("Contact: " + user.getProfile().getNumber());
            changeTab(profileTab);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if ((user.getAccountType() != null) && user.getAccountType().equals(AccountType.User)) {
            submitQualityReport.setVisible(false);
            //historyGraphButton.setVisible(false);
        }
        if ((user.getAccountType() != null) && (user.getAccountType().equals(AccountType.Worker)
                || user.getAccountType().equals(AccountType.Manager))) {
            //historyGraphButton.setVisible(false);
            submitQualityReport.setVisible(true);
        }
    }

    /**
     * Changes tab displayed to main screen tab
     */
    public void setToMainTab() {
        changeTab(profileTab);
    }

    /**
     * Changes tab displayed to reports tab
     */
    public void setToReportsTab() {
        changeTab(profileTab);
    }

    /**
     * change tab of user screen tabpane
     * @param tab tab to make active
     */
    @FXML
    private void changeTab(Tab tab) {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(tab);
    }

    /**
     * handles logout request - return to welcome screen
     * @param event logout
     */
    @SuppressWarnings("SuspiciousNameCombination")
    @FXML
    protected void handleLogout(ActionEvent event) throws IOException {
        int sceneWidth = 400;
        int sceneHeight = 275;
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../view/WelcomeScreen.fxml"));
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * handles edit profile - switch to edit profile screen
     * @param event edit profile selected
     */
    @FXML
    protected void handleEditProfile(ActionEvent event) throws IOException {
        Stage stage = (Stage) editProfileButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/EditProfile.fxml"));
        Parent root = fxmlLoader.load();
        EditProfileController controller = fxmlLoader.getController();
        controller.setUser(user);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * handles submit a report - switch to report submission screen
     * @param event submit report selected
     */
    @FXML
    protected void handleSubmitReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) editProfileButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/SubmitSourceScreen.fxml"));
        Parent root = fxmlLoader.load();
        SubmitSourceController controller = fxmlLoader.getController();
        controller.setUser(user);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * handles submit a quality report - switches to submit quality report screen
     * @param event submit report selected
     */
    @FXML
    protected void handleSubmitQuality(ActionEvent event) throws IOException {
        if (!user.getAccountType().equals(AccountType.Worker) && !user.getAccountType().equals(AccountType.Manager)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Account Type");
            alert.setHeaderText("Please check your account type");
            alert.setContentText(
                    "You do not have the valid privileges to access the quality report submission screen.");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) editProfileButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/SubmitQualityScreen.fxml"));
            Parent root = fxmlLoader.load();
            SubmitQualityController controller = fxmlLoader.getController();
            controller.setUser(user);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    /**
     * Switches to history graph creation screen
     * @param event view history graph button selected
     */
    @FXML
    protected void handleQualityHistory(ActionEvent event) throws IOException {
        if (!user.getAccountType().equals(AccountType.Worker) && !user.getAccountType().equals(AccountType.Manager)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Account Type");
            alert.setHeaderText("Please check your account type");
            alert.setContentText(
                    "You do not have the valid privileges to access the quality report submission screen.");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) editProfileButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/SetHistoryGraphScreen.fxml"));
            Parent root = fxmlLoader.load();
            SetHistoryGraphController controller = fxmlLoader.getController();
            controller.setUser(user);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Handles viewing the map - switch to map scene
     * @param event View Map selected
     * @throws IOException unable to switch to map screen
     */
    @FXML
    protected void handleViewMap(ActionEvent event) throws IOException {
        mainApp.setQualityReports(qualityReports);
        mainApp.setSourceReports(sourceReports);
        Stage stage = (Stage) viewMapButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MapScreen.fxml"));
        Parent root = fxmlLoader.load();
        MapController controller = fxmlLoader.getController();
        controller.setUser(user);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
