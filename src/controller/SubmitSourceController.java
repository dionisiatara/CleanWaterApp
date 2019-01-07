package controller;

import com.lynden.gmapsfx.javascript.object.LatLong;
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
import model.*;
import model.Control;

import java.time.LocalDate;

/**
 * Created by dionisiatara on 10/11/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SubmitSourceController {
    @FXML ComboBox<WaterType> waterTypeComboBox;
    @FXML ComboBox<WaterCondition> waterConditionComboBox;
    @FXML TextField reportName;
    @FXML
    TextField reportTime;
    @FXML
    TextField reportDescription;
    @FXML Button cancelButton;
    @FXML DatePicker date;
    @FXML TextField textLocation;
    private WaterSourceReport report;
    private ObservableList<WaterSourceReport> sourceReports;
    private User user;
    @SuppressWarnings("CanBeFinal")
    private final MainFXApplication mainApp = new MainFXApplication();

    /**
     * called automatically in order to populate the waterTypeComboBox with water types
     * and the waterConditionComboBox with condition types
     */
    @FXML
    private void initialize() {
        ObservableList<WaterType> typeList = FXCollections.observableArrayList(WaterType.values());
        waterTypeComboBox.setItems(typeList);
        ObservableList<WaterCondition> conditionList = FXCollections.observableArrayList(WaterCondition.values());
        waterConditionComboBox.setItems(conditionList);
        sourceReports = mainApp.getWaterSourceReports();
    }

    /**
     * sets user from login screen
     * @param user current user
     * @throws NullPointerException if user is null
     */
    public void setUser(User user) throws NullPointerException {
        this.user = user;
        reportName.setText(user.getName());
    }

    /**
     * sets submit fields to water report's data
     * @param report report to pull data from
     */
    public void setReport(WaterSourceReport report) {
        date.setValue(report.getDate());
        reportTime.setText(report.getTime());
        reportName.setText(report.getReporterName());
        reportDescription.setText(report.getLocation().getDescription());
        waterConditionComboBox.setValue(report.getCondition());
        waterTypeComboBox.setValue(report.getType());
    }

    /**
     * sets water report location to be chosen location
     * @param latLong latitude/longitude to set current report to
     */

    public void setCurrentLocation(LatLong latLong) {
        /*String locText = "";
        this.latLong = latLong;
        if (latLong.getLatitude() > 0) {
            locText += latLong.getLatitude() + "*N ";
        } else {
            locText += latLong.getLatitude() + "*S ";
        }
        if (latLong.getLongitude() > 0) {
            locText += latLong.getLongitude() + "*E";
        } else {
            locText += latLong.getLongitude() + "*W";
        }
        textLocation.setText(locText);*/
        Location temp = new Location("", latLong);
        textLocation.setText(temp.getName());
    }

    /**
     * handles choosing of location
     * @param event selects choose location button
     * @throws java.io.IOException cannot switch to map controller and map screen
     */
    @FXML
    protected void handleChooseLocation(ActionEvent event) throws java.io.IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MapScreen.fxml"));
        Parent root = fxmlLoader.load();
        MapController controller = fxmlLoader.getController();

        // Passes on user and report data to user scene, order determines which tab will be active last
        controller.setUser(user);
        controller.sourceSelection();
        //controller.setSourceReportsList(sourceReports);
       // controller.setQualityReportsList(qualityReports);
        Location tempLocation = new Location("", "", false);
        if (reportName.getText() != null) {
            tempLocation.setName(reportName.getText());
        }
        if (reportDescription.getText() != null) {
            tempLocation.setDescription(reportDescription.getText());
        }
        WaterSourceReport report = new WaterSourceReport(
                sourceReports.size() + 1,
                reportName.getText(),
                date.getValue(),
                reportTime.getText(),
                tempLocation,
                waterConditionComboBox.getValue(),
                waterTypeComboBox.getValue());
        controller.setSourceReport(report);
        controller.setChooseLoc();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * handles cancel request, returns to user screen
     * @param event cancel report submission
     */
    @FXML
    protected void handleCancel(ActionEvent event) throws java.io.IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/UserScreen.fxml"));
        Parent root = fxmlLoader.load();
        UserScreenController controller = fxmlLoader.getController();

        // Passes on user and report data to user scene, order determines which tab will be active last
        controller.setUser(user);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * reads user water report input, adds report to report list
     * @param event when the button is pressed
     * @throws java.io.IOException unable to access database
     */
    @FXML
    protected void handleSubmit(ActionEvent event) throws java.io.IOException {
        if (isValidSubmit()) {
            sourceReports.add(report);
            Control.getInstance().getDatabase().addWaterSourceReport(report);
            mainApp.setSourceReports(sourceReports);

            Stage stage = (Stage) reportTime.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/UserScreen.fxml"));
            Parent root = fxmlLoader.load();
            UserScreenController controller = fxmlLoader.getController();

            controller.setUser(user);
            controller.setToReportsTab();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * checks if water report is valid
     * @return true if valid water report submit
     */
    @FXML
    private boolean isValidSubmit() {
        String errorMessage = "";
        //get text from registration form

        String name = reportName.getText();
        String description = reportDescription.getText();
        String time = reportTime.getText();
        WaterCondition condition = waterConditionComboBox.getValue();
        WaterType type = waterTypeComboBox.getValue();
        LocalDate localDate = date.getValue();

        if (name == null) {
            errorMessage += "Please enter your name!\n";
        }
        if (localDate == null) {
            errorMessage += "Please select a valid date!\n";
        }
        if ((time == null) || time.isEmpty() || time.contains("/")) {
            errorMessage += "Please enter a valid time!\n";
        }
        if ((textLocation.getText() == null) || "".equals(textLocation.getText())) {
            errorMessage += "Please enter a valid location!\n";
        }
        if (waterConditionComboBox.getValue() == null) {
            errorMessage += "Please enter a water condition!\n";
        }
        if (waterTypeComboBox.getValue() == null) {
            errorMessage += "Please select a water type.\n";
        }
        else if (description == null) {
            errorMessage += "Please enter a valid description!\n";
        } /*else if (description == "") {
            location.setDescription("No Description");
        }*/
        if (errorMessage.isEmpty()) {
            report = new WaterSourceReport(sourceReports.size() + 1,
                    name, localDate, time, textLocation.getText(),
                    description, condition, type);
            return true;
        } else {
            //send alert warning of registration error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Water Report Submission");
            alert.setHeaderText("Please check your water report");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
