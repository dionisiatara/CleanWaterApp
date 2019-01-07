package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

/**
 * Created by Allen on 10/24/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Database {
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet result;

    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<WaterSourceReport> wsReports = FXCollections.observableArrayList();
    private final ObservableList<WaterQualityReport> wqReports = FXCollections.observableArrayList();

    /**
     * Attempts to connect to a database
     */
    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            String url = "jdbc:mysql://localhost/cleanwater?autoReconnect=true&useSSL=false";
            connection = DriverManager.getConnection (url, "admin", "genos");
            restoreUsers();
            //System.out.println("connection set");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * adds user to database
     * @param user new user
     * @return true if user successfully added, otherwise false
     */
    public boolean addUser(User user) {
        try {
            String query = "SELECT 'username' FROM users WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            result = statement.executeQuery();
            if(!(result.next())) {
                query = "INSERT INTO users (id, username, password, name, accountType, userProfile) "
                        + "values (null, ?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getName());
                statement.setString(4, user.getAccountType().toString());
                statement.setString(5, user.getProfile().toString());
                statement.execute();
                users.add(user);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * adds water source report to database
     * @param waterSourceReport new water source report
     * @return true if report successfully added, false otherwise
     */
    public boolean addWaterSourceReport(WaterSourceReport waterSourceReport) {
        try {
            String query = "INSERT INTO watersource (id, time, name, location, overallCondition, type, date, reportNum)"
                    + " values (null, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, waterSourceReport.getTime());
            statement.setString(2, waterSourceReport.getReporterName());
            statement.setString(3, waterSourceReport.getLocation().toString());
            statement.setString(4, waterSourceReport.getCondition().toString());
            statement.setString(5, waterSourceReport.getType().toString());
            statement.setString(6, waterSourceReport.getDate().toString());
            statement.setDouble(7, waterSourceReport.getReportNum());
            statement.execute();
            wsReports.add(waterSourceReport);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * adds water quality report to database
     * @param waterPurityReport new water quality report
     * @return true if report successfully added, false otherwise
     */
    public boolean addWaterQualityReport(WaterQualityReport waterPurityReport) {
        try {
            String query = "INSERT INTO waterquality (id, time, name, location, date, "
                    + "reportNum, overallCondition, virusPPM, contaminantPPM)"
                    + " values (null, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, waterPurityReport.getTime());
            statement.setString(2, waterPurityReport.getReporterName());
            statement.setString(3, waterPurityReport.getLocation().toString());
            statement.setString(4, waterPurityReport.getDate().toString());
            statement.setDouble(5, waterPurityReport.getReportNum());
            statement.setString(6, waterPurityReport.getOverallCondition().toString());
            statement.setDouble(7, waterPurityReport.getVirusPPM());
            statement.setDouble(8, waterPurityReport.getContamPPM());
            statement.execute();
            wqReports.add(waterPurityReport);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Looks for user in database
     * @param user user searched for
     * @return true if user is in database, false if not
     */
    public boolean findUser(User user) {
        for (User person : users) {
            if (person.getUsername().equals(user.getUsername())) {
                if (person.getPassword().equals(user.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeUser(String username) {
        try {
            String query = "DELETE FROM users WHERE username=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.execute();
            restoreUsers();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * changes user information according to parameters
     * @param username username of new user
     * @param user user object for new user
     * @return true if edit successfully performed, false if not
     */
    public boolean editUser(String username, User user) {
        try {
            String query = "UPDATE users SET name=?, userProfile=? WHERE username=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getProfile().toString());
            statement.setString(3, username);
            statement.execute();
            for (User person : users) {
                if (person.getUsername().equals(user.getUsername())) {
                    person.setProfile(user.getProfile());
                    person.setName(person.getProfile().getName());
                }
            }
            restoreUsers();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * retrieves user based on username
     * @param username username of desired user
     * @return the user corresponding to username
     */
    public User getUser(String username) {
        for (User person : users) {
            if (person.getUsername().equals(username)) {
                return person;
            }
        }
        return null;
    }

    private void restoreUsers() {
        users.clear();
        String query = "SELECT * FROM users";

        try {
            statement = connection.prepareStatement(query);
            result = statement.executeQuery();
            while(result.next()) {
                String username = result.getString("username");
                String password = result.getString("password");
                String name = result.getString("name");
                AccountType accountType = AccountType.valueOf(result.getString("accountType"));
                String profile = result.getString("userProfile");
                String array[] = profile.split("/");
                UserProfile userProfile;
                if (array.length == 3) {
                    userProfile = new UserProfile(name, array[0], array[1], array[2]);
                } else {
                    userProfile = new UserProfile(name, array[0], array[1], array[2], Title.valueOf(array[3]));
                }
                User user = new User(username, name, password, accountType);
                User uUser = new User(user, userProfile);
                users.add(uUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * restores water source reports to application from database
     */
    public void restoreWsReports() {
        wsReports.clear();
        String query = "SELECT * FROM watersource";

        try {
            statement = connection.prepareStatement(query);
            result = statement.executeQuery();
            while(result.next()) {
                String time = result.getString("time");
                String nameOfReporter = result.getString("name");
                LocalDate date = LocalDate.parse(result.getString("date"));
                WaterCondition overallCondition = WaterCondition.valueOf(result.getString("overallCondition"));
                WaterType type = WaterType.valueOf(result.getString("type"));
                String desc = result.getString("location");
                String array[] = desc.split(",", 6);
                Location location = new Location(array[1], array[0], true);
                location.setCity(array[2]);
                location.setState(array[3]);
                location.setCountry(array[4]);
                Integer reportNum = (int)result.getDouble("reportNum");
                WaterSourceReport wsReport
                        = new WaterSourceReport(reportNum, nameOfReporter,
                        date, time, location, overallCondition, type);
                wsReports.add(wsReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * restores water quality reports to application from database
     */
    public void restoreWqReports() {
        wqReports.clear();
        String query = "SELECT * FROM waterquality";

        try {
            statement = connection.prepareStatement(query);
            result = statement.executeQuery();
            while(result.next()) {
                String time = result.getString("time");
                String nameOfReporter = result.getString("name");
                LocalDate date = LocalDate.parse(result.getString("date"));
                String desc = result.getString("location");
                String array[] = desc.split(",", 6);
                Location location = new Location(array[1], array[0], true);
                location.setCity(array[2]);
                location.setState(array[3]);
                location.setCountry(array[4]);
                Integer reportNum = (int)result.getDouble("reportNum");
                OverallCondition overallCondition = OverallCondition.valueOf(result.getString("overallCondition"));
                String virusPPM = Double.toString(result.getDouble("virusPPM"));
                String contaminantPPM = Double.toString(result.getDouble("contaminantPPM"));
                WaterQualityReport wqReport
                        = new WaterQualityReport(reportNum, nameOfReporter, date, time,
                        location, overallCondition, virusPPM, contaminantPPM);
                wqReports.add(wqReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * retrieves user list
     * @return ObservableList of users
     */
    public ObservableList<User> getUsers() {
        return users;
    }

    /**
     * retrieves list of water source reports
     * @return ObservableList of water source reports
     */
    public ObservableList<WaterSourceReport> getWsReports() {
        return wsReports;
    }

    /**
     * retrieves list of water quality reports
     * @return ObservableList of water quality reports
     */
    public ObservableList<WaterQualityReport> getWqReports() {
        return wqReports;
    }
}
