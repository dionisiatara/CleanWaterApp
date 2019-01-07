package model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taiga on 9/21/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
class UserDatabase {

    private User user;
    private String username;
    private HashMap<String, User> database = new HashMap<>();
    //database file path
    private final File databaseFile = new File("database.txt");

    /**
     * create userDatabase after reading database file
     */
    public UserDatabase() {
        try {
            //if database.txt file does not exist
            if (!databaseFile.exists()) {
                try {
                    // create new database file at database file path
                    databaseFile.createNewFile();
                    //write initial data in database
                } catch (IOException e) {
                    throw new IOException(e);
                }
            }
            // read database file to userdatabase
            FileReader inputDatabase = new FileReader(databaseFile.getAbsolutePath());
            BufferedReader bufferReader = new BufferedReader(inputDatabase);
            String databaseLine;
            //iterate through each line until null line
            //noinspection NestedAssignment
            while ((databaseLine = bufferReader.readLine()) != null) {
                // split line based on "/"
                String[] userData = databaseLine.split("/");
                // create user based on data from line
                User tempUser = new User(userData[0], userData[1], userData[2], AccountType.valueOf(userData[3]));
                User newUser;
                if (userData.length >= 4) {
                    UserProfile profile = new UserProfile(userData[1], userData[4], userData[5], userData[6]);
                    newUser = new User(tempUser, profile);
                } else {
                    newUser = new User(tempUser, new UserProfile(userData[1]));
                }

                // add user to database
                database.put(tempUser.getUsername(), newUser);
            }
            bufferReader.close();
            // catch possible IOException or NullPointerException
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * creates userdatabase with imported data
     *
     * @param database imported database
     */
    public UserDatabase(HashMap<String, User> database) {
        this.database = database;
    }

    /**
     * checks to see if a user exists in the database
     *
     * @param user String username
     * @return boolean value whether user exists in database
     */
    private boolean userExists(String user) {
        return database.containsKey(user);
    }

    /**
     * returns user of certain username
     *
     * @param username username to search for
     * @return user with user data
     */
    public User getUser(String username) {
        return database.get(username);
    }

    /**
     * checks to see if a user exists in the database
     *
     * @param user User object
     * @return boolean value whether user exists in database
     */
    private boolean userExists(User user) {
        try {
            return database.containsValue(user);
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * checks to see if login (user) is valid
     *
     * @param user user
     * @return boolean value whether login was valid
     */
    public boolean login(User user) throws NullPointerException {
        String username = user.getUsername();
        User tempUser = database.get(username);
        if (database.get(username) == null) {
            throw new NullPointerException("This user does not exist");
        }
        return tempUser.getPassword().equals(user.getPassword());
    }

    /**
     * appends database to current user database
     *
     * @param database database to be added
     */
    public void addDatabase(HashMap<String, User> database) {
        this.database.putAll(database);
    }

    /**
     * checks if user database is empty
     *
     * @return boolean whether or not userbase is empty
     */
    public boolean isEmpty() {
        return database.isEmpty();
    }

    /**
     * return database
     *
     * @return database
     */
    public HashMap<String, User> getDatabase() {
        return database;
    }

    /**
     * removes user
     *
     * @param user user to be removed
     * @return boolean whether or not the user was removed
     */
    public boolean removeUser(User user) {
        if (userExists(user)) {
            database.remove(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * removes user
     *
     * @param user user to be removed
     * @return boolean whether or not the user was removed
     */
    public boolean removeUser(String user) {
        if (userExists(user)) {
            database.remove(user);
            return true;
        } else {
            return false;
        }
    }


    /**
     * adds user to database
     *
     * @param user user
     */
    public void addUser(User user) throws java.io.IOException {
        try {
            database.put(user.getUsername(), user);
            saveDatabase();
        } catch (IOException e) {
            throw new IOException("Error writing to database" + e.getMessage());
        }
    }

    /**
     * replaces old user data with new user data
     *
     * @param oldUsername old username to check database for
     * @param newUser new user to replace with
     * @return whether or not the user was able to be replaced
     */

    public boolean editUser(String oldUsername, User newUser) throws IOException {
        try {
            database.remove(oldUsername);
            database.put(oldUsername, newUser);
            saveDatabase();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * saves database to file
     * @throws IOException cannot access file
     */
    private void saveDatabase() throws IOException {
        try {
            FileWriter databaseWriter = new FileWriter(databaseFile.getAbsolutePath());
            //BufferedWriter bufferedWriter = new BufferedWriter(databaseWriter);
            // iterate through user data and append to one line, using the User.toString method
            for (Map.Entry<String, User> entry : database.entrySet()) {
                databaseWriter.write(entry.getValue().toString() + "\n");
            }
            databaseWriter.close();
        } catch (IOException e) {
            throw new IOException("Error writing to database" + e.getMessage());
        }
    }

}
