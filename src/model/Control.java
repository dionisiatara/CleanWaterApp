package model;

/**
 * Created by Allen on 10/30/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public final class Control {
    private final Database database;
    private static final Control instance = new Control();

    private Control() {
        database = new Database();
        //test users
        database.addUser(new User("u", "a", "p", AccountType.User));
        database.getUsers().add(new User("u", "a", "p", AccountType.User));
        database.addUser(new User("w", "a", "p", AccountType.Worker));
        database.getUsers().add(new User("w", "a", "p", AccountType.Worker));
        database.addUser(new User("m", "a", "p", AccountType.Manager));
        database.getUsers().add(new User("m", "a", "p", AccountType.Manager));
        database.addUser(new User("a", "a", "p", AccountType.Admin));
        database.getUsers().add(new User("a","a", "p", AccountType.Admin));
    }

    /**
     * restores water source reports from database
     */
    public void restoreWsReports() {
        database.restoreWsReports();
    }

    /**
     * restores water quality reports from database
     */
    public void restoreWqReports() {
        database.restoreWqReports();
    }

    /**
     * add a user to the database
     *
     * @param user the user to add
     * @return true if successful add of user
     */
    public boolean addUser(User user) {
        return (database != null && database.addUser(user));
    }

    /**
     * returns database for application
     * @return database
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * Returns this instance of control
     * @return this Control class
     */
    public static Control getInstance() {
        return instance;
    }
}
