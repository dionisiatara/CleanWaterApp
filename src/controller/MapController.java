package controller;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import fxapp.MainFXApplication;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Location;
import javafx.collections.ObservableList;
import model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.WaterSourceReport;
import model.WaterQualityReport;
import model.AccountType;
import netscape.javascript.JSObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Kevin on 10/17/2016.
 */
@SuppressWarnings({"DefaultFileTemplate"})
public class MapController implements Initializable, MapComponentInitializedListener {

    @FXML
    private GoogleMapView mapView;
    private GoogleMap map;

    private boolean chooseLoc = false;
    private User user;
    private WaterSourceReport sourceReport;
    private WaterQualityReport qualityReport;
    private ObservableList<WaterSourceReport> sourceReports;
    private ObservableList<WaterQualityReport> qualityReports;
    private final MainFXApplication mainApp = new MainFXApplication();
    private boolean sourceLocationSelection;
    private boolean qualityLocationSelection;

    @FXML
    Button exitMapViewButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mapView.addMapInializedListener(this);
        sourceReports = mainApp.getWaterSourceReports();
        qualityReports = mainApp.getWaterQualityReports();
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
     * Sets the current source report whose location the user is choosing
     * @param report the source report
     */
    @FXML
    public void setSourceReport(WaterSourceReport report) {sourceReport = report;}

    /**
     * Sets the current quality report whose location the user is choosing
     * @param report the quality report
     */
    @FXML
    public void setQualityReport(WaterQualityReport report) {qualityReport = report;}

    /**
     * Determines whether controller is being used to selection source report location
     */
    public void sourceSelection() {
        sourceLocationSelection = true;
    }

    /**
     * Determines whether controller is being used to selection quality report location
     */
    public void qualitySelection() {
        qualityLocationSelection = true;
    }

    /**
     * sets whether or not loc should be chosen
     */
    public void setChooseLoc() {
        chooseLoc = true;
    }

    /**
     * Set map properties, display map, obtain locations of reports, and display report markers on map
     */
    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();

        int atlLat = 34;
        int atlLong = -84;

        LatLong center = new LatLong(atlLat, atlLong);

        options.center(center)
                .zoom(7)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .mapType(MapTypeIdEnum.TERRAIN);

        map = mapView.createMap(options);

        if (user.getAccountType().equals(AccountType.Worker) || user.getAccountType().equals(AccountType.Manager)) {
            for (WaterQualityReport report : qualityReports) {
                MarkerOptions markerOptions = new MarkerOptions();
                Location l = report.getLocation();
                LatLong loc = new LatLong(l.getLat(), l.getLong());

                markerOptions.position(loc)
                        .visible(Boolean.TRUE)
                        .title(l.getName());

                Marker marker = new Marker(markerOptions);

                map.addUIEventHandler(marker,
                        UIEventType.click,
                        (JSObject obj) -> {
                            InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                            infoWindowOptions.content(
                                    "<h4>Quality Report " + report.getReportNum() + "</h4>"
                                            + "Submitted by " + report.getReporterName() + "<br/>"
                                            + l.getName() + "<br />"
                                            + report.getDate() + "at " + report.getTime() + "<br />"
                                            + report.getOverallCondition() + "<br />"
                                            + "Virus PPM: " + report.getVirusPPM() + "<br />"
                                            + "Contaminant PPM: " + report.getContamPPM() + "<br />"
                            );
                            InfoWindow window = new InfoWindow(infoWindowOptions);
                            window.open(map, marker);
                        });

                map.addMarker(marker);
            }
        }
        for (WaterSourceReport report : sourceReports) {
            MarkerOptions markerOptions = new MarkerOptions();
            Location l = report.getLocation();
            LatLong loc = new LatLong(l.getLat(), l.getLong());

            markerOptions.position(loc)
                    .icon("http://maps.google.com/mapfiles/ms/icons/blue-dot.png")
                    .visible(Boolean.TRUE)
                    .title(l.getName());

            Marker marker = new Marker(markerOptions);
            map.addUIEventHandler(marker,
                    UIEventType.click,
                    (JSObject obj) -> {
                        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                        infoWindowOptions.content(
                                "<h4>Source Report " + report.getReportNum() + "</h4>"
                                        + "Submitted by " + report.getReporterName() + "<br/>"
                                        + l.getName() + "<br />"
                                        + report.getDate() + " at " + report.getTime() + "<br />"
                                        + report.getType() + ": " + report.getCondition() + "<br />"
                        );

                        InfoWindow window = new InfoWindow(infoWindowOptions);
                        window.open(map, marker);
                    });

            map.addMarker(marker);
        }
        //if at map screen in order to choose a location as opposed to simply viewing it
        if (chooseLoc) {
            map.addUIEventHandler(map, UIEventType.click, (JSObject obj) -> {
                LatLong latLong = new LatLong((JSObject) obj.getMember("latLng"));
                //alert user to confirm location selection
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Location Confirmation");
                alert.setContentText("Are you sure you want to choose this location?");
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        try {
                            //go back to submit report screen and preserve location selected
                            Stage stage = (Stage) exitMapViewButton.getScene().getWindow();
                            if (qualityLocationSelection) {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                                        .getResource("../view/SubmitQualityScreen.fxml"));
                                Parent root = fxmlLoader.load();
                                SubmitQualityController controller = fxmlLoader.getController();
                                controller.setUser(user);
                                controller.setReport(qualityReport);
                                controller.setCurrentLocation(latLong);
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            } else {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                                        .getResource("../view/SubmitSourceScreen.fxml"));
                                Parent root = fxmlLoader.load();
                                SubmitSourceController controller = fxmlLoader.getController();
                                controller.setUser(user);
                                controller.setReport(sourceReport);
                                controller.setCurrentLocation(latLong);
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            }
                        } catch (IOException e) {
                            //catch io exception for fxmlLoader
                        }
                    }
                }));

            });
        }
    }

    /**
     * Handles exiting the map
     * @param event exit the map
     * @throws IOException problem returning to or calling up the previous scene
     */
    @FXML
    protected void handleMapExit(ActionEvent event) throws IOException {
        Stage stage = (Stage) exitMapViewButton.getScene().getWindow();
        FXMLLoader fxmlLoader;
        Parent root;
        if (sourceLocationSelection) {
            fxmlLoader = new FXMLLoader(getClass().getResource("../view/SubmitSourceScreen.fxml"));
            root = fxmlLoader.load();
            SubmitSourceController controller = fxmlLoader.getController();
            controller.setUser(user);
            if (chooseLoc) {
                controller.setReport(sourceReport);
            }
        } else if (qualityLocationSelection) {
            fxmlLoader = new FXMLLoader(getClass().getResource("../view/SubmitQualityScreen.fxml"));
            root = fxmlLoader.load();
            SubmitQualityController controller = fxmlLoader.getController();
            controller.setUser(user);
            if (chooseLoc) {
                controller.setReport(qualityReport);
            }
        } else {
            fxmlLoader = new FXMLLoader(getClass().getResource("../view/UserScreen.fxml"));
            root = fxmlLoader.load();
            UserScreenController controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setToMainTab();
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
