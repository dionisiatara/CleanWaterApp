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
 * Created by taiga on 10/24/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SubmitQualityController {
    @FXML ComboBox<OverallCondition> overallComboBox;
    @FXML TextField reporterName;
    @FXML
    TextField virusPPMField;
    @FXML
    TextField contamPPMField;
    @FXML
    TextField reportTime;
    @FXML DatePicker date;
    @FXML Button cancelButton;
    @FXML
    Button submitButton;
    @FXML TextField textLocation;
    private WaterQualityReport report;
    private ObservableList<WaterQualityReport> qualityReports;
    private User user;
    private final MainFXApplication mainApp = new MainFXApplication();

    /**
     * called automatically in order to populate the waterTypeComboBox with water types
     * and the waterConditionComboBox with condition types
     */
    @FXML
    private void initialize() {
        ObservableList<OverallCondition> overallList = FXCollections.observableArrayList(OverallCondition.values());
        overallComboBox.setItems(overallList);
        qualityReports = mainApp.getWaterQualityReports();
    }

    /**
     * sets user from login screen
     * @param user current user
     * @throws NullPointerException if user is null
     */
    public void setUser(User user) throws NullPointerException {
        this.user = user;
        reporterName.setText(user.getName());
    }

    /**
     * sets submit fields to water report's data
     * @param report report to pull data from
     */
    public void setReport(WaterQualityReport report) {
        date.setValue(report.getDate());
        reportTime.setText(report.getTime());
        reporterName.setText(report.getReporterName());
        overallComboBox.setValue(report.getOverallCondition());
        //virusPPMField.setText("" + report.getVirusPPM());
        //contamPPMField.setText("" + report.getContamPPM());
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
        controller.qualitySelection();
        Location tempLocation = new Location("", "", false);
        if (reporterName.getText() != null) {
            tempLocation.setName(reporterName.getText());
        }
        WaterQualityReport report = new WaterQualityReport(
                qualityReports.size() + 1,
                reporterName.getText(),
                date.getValue(),
                reportTime.getText(),
                tempLocation,
                overallComboBox.getValue(),
                virusPPMField.getText(),
                contamPPMField.getText());
        controller.setQualityReport(report);
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
            qualityReports.add(report);
            Control.getInstance().getDatabase().addWaterQualityReport(report);
            mainApp.setQualityReports(qualityReports);

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
        String name = reporterName.getText();
        String time = reportTime.getText();
        OverallCondition condition = overallComboBox.getValue();
        LocalDate localDate = date.getValue();
        String virusPPM = virusPPMField.getText();
        String contamPPM = contamPPMField.getText();

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
        if (overallComboBox.getValue() == null) {
            errorMessage += "Please select a water type.\n";
        }
        if (errorMessage.isEmpty()) {
            report = new WaterQualityReport(qualityReports.size() + 1, name,
                    localDate, time, textLocation.getText(),
                    condition, virusPPM, contamPPM);
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
