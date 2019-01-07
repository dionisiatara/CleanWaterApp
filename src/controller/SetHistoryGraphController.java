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
import javafx.stage.Stage;
import model.User;
import model.WaterQualityReport;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by twalker61 on 10/29/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SetHistoryGraphController {

    @FXML ComboBox<String> locationList = new ComboBox<>();
    @FXML RadioButton virusButton;
    @FXML RadioButton contamButton;
    @FXML TextField graphYear;
    @FXML Button cancelButton;
    @FXML Button submitButton;
    private User user;
    private final MainFXApplication mainApp = new MainFXApplication();

    /**
     * called automatically in order to populate the location list view with available locations
     */
    @FXML
    private void initialize() {
        Set<String> temp = new HashSet<>();
        ObservableList<WaterQualityReport> qualityReports = mainApp.getWaterQualityReports();
        if (qualityReports != null) {
            for (WaterQualityReport r : qualityReports) {
                temp.add(r.getLocation().toString());
            }
        }
        ObservableList<String> locations = FXCollections.observableArrayList(temp);
        locationList.setItems(locations);
    }

    /**
     * sets user from login screen
     * @param user current user
     * @throws NullPointerException if user is null
     */
    public void setUser(User user) throws NullPointerException {
        this.user = user;
    }


    /**
     * Submit the data for the history graph and switch to graph screen
     * @param event submit button selected
     * @throws IOException cannot switch back to previous scene
     */
    @FXML
    protected void handleSubmitGraph(ActionEvent event) throws IOException {
        if (isValidSubmit()) {
            Stage stage = (Stage) submitButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/HistoryGraphScreen.fxml"));
            Parent root = fxmlLoader.load();
            HistoryGraphController controller = fxmlLoader.getController();
            controller.setUser(user);
            settingInputs(controller);
            controller.setupGraph();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * checks if all needed graphs information are filled in
     */
    @FXML
    private boolean isValidSubmit() {
        String errorMessage = "";

        if (graphYear.getText().isEmpty()) {
            errorMessage += "Please enter the year!\n";
        }
        if (!virusButton.isSelected() && !contamButton.isSelected()) {
            errorMessage += "Please pick at least one type!\n";
        }
        if (locationList.getValue() == null) {
            errorMessage += "Please pick a location!\n";
        }
        if (!errorMessage.isEmpty()) {
            //send alert warning of registration error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Graph Setup");
            alert.setHeaderText("Please check your graph setup again");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * sets all the inputs needed for setting up the graph
     * @param controller HistoryGraphController object
     */
    private void settingInputs(HistoryGraphController controller) {
        controller.setYear(Integer.parseInt(graphYear.getText()));
        if (virusButton.isSelected()) {
            controller.setVirus();
        }
        if (contamButton.isSelected()) {
            controller.setContam();
        }
        controller.setLocation(locationList.getValue());
    }

    /**
     * Return to User Screen
     * @param event cancel button selected
     * @throws IOException cannot switch back to previous scene
     */
    @FXML
    protected void handleCancelButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/UserScreen.fxml"));
        Parent root = fxmlLoader.load();
        UserScreenController controller = fxmlLoader.getController();
        controller.setUser(user);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
