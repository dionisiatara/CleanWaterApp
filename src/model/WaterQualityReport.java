package model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by twalker61 on 10/24/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class WaterQualityReport {

    private final StringProperty time = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private Location location;
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final IntegerProperty reportNum = new SimpleIntegerProperty();
    private final ObjectProperty<OverallCondition> overallCondition = new SimpleObjectProperty<>();
    private String virusPPM;
    private String contaminantPPM;

    /**
     * initializes report time, number, name, date, water condition and virus/contaminant levels, and location object
     * @param reportNum the auto-generated number of the report
     * @param reporterName the name of the user that made the report
     * @param date the date of report creation
     * @param time the time of report creation
     * @param location the location object of the report
     * @param overallCondition the overall water quality
     * @param virusPPM the virus ppm
     * @param contamPPM the contaminant ppm
     */
    public WaterQualityReport(int reportNum, String reporterName, LocalDate date, String time, Location location,
                              OverallCondition overallCondition, String virusPPM, String contamPPM) {
        this.reportNum.set(reportNum);
        this.name.set(reporterName);
        this.date.set(date);
        this.time.set(time);
        this.location = location;
        this.overallCondition.set(overallCondition);
        this.virusPPM = virusPPM;
        this.contaminantPPM = contamPPM;
    }

    /**
     * initializes report time, number, name, date, water condition and virus/contaminant levels, and location object
     * @param reportNum the auto-generated number of the report
     * @param reporterName the name of the user that made the report
     * @param date the date of report creation
     * @param time the time of report creation
     * @param location the string text location of the report
     * @param overallCondition the overall water quality
     * @param virusPPM the virus ppm
     * @param contamPPM the contaminant ppm
     */
    public WaterQualityReport(int reportNum, String reporterName, LocalDate date, String time, String location,
                              OverallCondition overallCondition, String virusPPM, String contamPPM) {
        this.reportNum.set(reportNum);
        this.name.set(reporterName);
        this.date.set(date);
        this.time.set(time);
        this.location = getLocationObject(location);
        this.overallCondition.set(overallCondition);
        this.virusPPM = virusPPM;
        this.contaminantPPM = contamPPM;
    }

    /**
     * returns time of report
     * @return time of report
     */
    public String getTime() {return time.get();}

    /**
     * returns location of report
     * @return location of report
     */
    public int getReportNum() {return reportNum.getValue();}

    /**
     * returns name of reporter
     * @return name of reporter of water report
     */
    public String getReporterName() {return name.get();}

    /**
     * returns time of report
     * @return time of report
     */
    public LocalDate getDate() {return date.get();}

    /**
     * returns location of report
     * @return location of report
     */
    public Location getLocation() {return location;}

    /**
     * returns location object of report
     * @return location object of report
     */
    private Location getLocationObject(String name) {
        return new Location(name, "", true);
    }

    /**
     * returns overall condition
     * @return overall condition of report
     */
    public OverallCondition getOverallCondition() {
        return overallCondition.get();
    }

    /**
     * returns virus count of report
     * @return virus count of report
     */
    public double getVirusPPM() {
        return Double.parseDouble(virusPPM);
    }

    /**
     * returns contaminant count of report
     * @return contaminant count of report
     */
    public double getContamPPM() {
        return Double.parseDouble(contaminantPPM);
    }

    /**
     * change time of report
     * @param time new time of report
     */
    public void setTime(String time) {this.time.set(time);}

    /**
     * change location of report
     * @param location new location of report
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * change overall condition of report
     * @param condition new overall condition of report
     */
    public void setOverallCondition(OverallCondition condition) {this.overallCondition.set(condition);}

    /**
     * change virus ppm of report
     * @param ppm new virus ppm of report
     */
    public void setVirusPPM(String ppm) {virusPPM = ppm;}

    /**
     * change contaminant ppm of report
     * @param ppm new contaminant ppm of report
     */
    public void setContaminantPPM(String ppm) {contaminantPPM = ppm;}

    /**
     * change number in report
     * @param num new report number
     */
    public void setReportNum(int num) {this.reportNum.setValue(num);}

    /**
     * returns the string concatenation of the water report data
     * @return the string representation of the report
     */
    @Override
    public String toString() {
        return "Quality " + reportNum.get() + ":  "
                + date.get() + " / "
                + time.get() + " / "
                + location.getLatLongString() + "* / "
                + overallCondition.get().toString() + " / "
                + virusPPM + "/"
                + contaminantPPM;
    }
}
