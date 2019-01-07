package controller;

import fxapp.MainFXApplication;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.User;
import model.WaterQualityReport;
import java.io.IOException;
import java.time.Month;

/**
 * Created by twalker61 on 10/29/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class HistoryGraphController {

    @FXML CategoryAxis xAxis;
    @FXML NumberAxis yAxis;
    @FXML LineChart historyGraph;
    @FXML Button backButton;
    private ObservableList<WaterQualityReport> qualityReports;
    private User user;
    private final MainFXApplication mainApp = new MainFXApplication();

    private int graphYear;
    private boolean virus = false;
    private boolean contaminant = false;
    private String location;

    /**
     * Called automatically to set the legend for the axes
     */
    @FXML
    private void initialize() {
        // not working yet
        /*
]        ObservableList<String> months = FXCollections.observableArrayList();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        xAxis.setCategories(months);

        */
        qualityReports = mainApp.getWaterQualityReports();
        //setupGraph();
    }

    /**
     * sets the graph year
     * @param year the year for the graph
     */
    public void setYear(int year) {
        graphYear = year;
    }

    /**
     * sets virus to be represented
     */
    public void setVirus() {
        virus = true;
    }

    /**
     * sets contaminant to be represented
     */
    public void setContam() {
        contaminant = true;
    }

    /**
     * sets location of the graph
     * @param location the location of the graph
     */
    public void setLocation(String location) {
        this.location = location;
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
     * Return to the create history graph screen
     * @param event back button selected
     * @throws IOException problem with receiving back command
     */
    @FXML
    protected void handleBackButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/SetHistoryGraphScreen.fxml"));
        Parent root = fxmlLoader.load();
        SetHistoryGraphController controller = fxmlLoader.getController();
        controller.setUser(user);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * setting up the graph
     */
    @FXML
    void setupGraph() {
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Virus");
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Contaminant");

        FXCollections.sort(qualityReports, (o1, o2) -> {
            if (o1.getDate().equals(o2.getDate())) {
                return o1.getTime().compareTo(o2.getTime());
            } else {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        for (int i = 0; i < qualityReports.size(); ) {
            if ((graphYear == (qualityReports.get(i).getDate().getYear()))
                    && location.equals(qualityReports.get(i).getLocation().toString())) {
                int idx = 0;
                if (virus) {
                    try {
                        Month reportMonth = qualityReports.get(i).getDate().getMonth();
                        double totalVirus = qualityReports.get(i).getVirusPPM();
                        double monthCount = 1;
                        if (qualityReports.get(i+1) != null) {
                            for (int j = i + 1; (j < qualityReports.size())
                                    && qualityReports.get(j).getDate().getMonth().equals(reportMonth);
                                 j++) {
                                if (reportMonth.equals(qualityReports.get(j).getDate().getMonth())) {
                                    monthCount++;
                                    totalVirus += qualityReports.get(j).getVirusPPM();
                                }
                            }
                        }
                        double average = totalVirus / monthCount;
                        try {
                            series1.getData().add(new XYChart.Data(reportMonth.toString(), average));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        idx += monthCount;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (contaminant) {
                    try {
                        Month reportMonth = qualityReports.get(i).getDate().getMonth();
                        double totalContam = qualityReports.get(i).getContamPPM();
                        double monthCount = 1;
                        if (qualityReports.get(i+1) != null) {
                            for (int j = i + 1; (j < qualityReports.size())
                                    && qualityReports.get(j).getDate().getMonth().equals(reportMonth);
                                 j++) {
                                if (reportMonth.equals(qualityReports.get(j).getDate().getMonth())) {
                                    monthCount++;
                                    totalContam += qualityReports.get(j).getContamPPM();
                                }
                            }
                        }
                        double average = totalContam / monthCount;
                        try {
                            series2.getData().add(new XYChart.Data(reportMonth.toString(), average));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                i += idx;
            }
        }
        try {
            historyGraph.getData().add(series1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            historyGraph.getData().add(series2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
