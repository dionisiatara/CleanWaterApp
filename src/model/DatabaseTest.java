package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Allen on 11/11/2016.
 */
public class DatabaseTest {
    private Database database;
    private UserProfile profile;
    private User user;
    private int wqCounter, wsCounter, userCounter;

    @Before
    public void setup() {
        database = new Database();
    }

    @Test
    public void DatabaseTest() {
        assertTrue(database != null);

        //Test restores user gets restored at start with default users while reports are not restored
        //unless done manually
        userCounter = database.getUsers().size();
        assertTrue(userCounter != 0);

        wsCounter = database.getWsReports().size();
        wqCounter = database.getWqReports().size();
        assertTrue(wsCounter == 0);
        assertTrue(wqCounter == 0);

        //Use locally stored database to restore reports manually
        //Database must have watersource and waterquality reports for these tests to pass
        database.restoreWsReports();
        database.restoreWqReports();
        wsCounter = database.getWsReports().size();
        wqCounter = database.getWqReports().size();
        assertTrue(wsCounter != 0);
        assertTrue(wqCounter != 0);


        //Test addUser
        //Only works if user with username "user" does not already exist
        profile = new UserProfile("name", "Edit your profile", "Edit your profile", "Edit your profile");
        user = new User(new User("user","name,","pass", AccountType.User),profile);
        assertTrue(database.addUser(user));
        userCounter++;
        assertTrue(userCounter == database.getUsers().size());

        //Test getUser
        User temp = database.getUser("user");
        assertTrue(temp.equals(user));
        //Test findUser
        assertTrue(database.findUser(user));
        User tempu = new User("u","p");
        User tempw = new User("w","p");
        User tempm = new User("m","p");
        User tempa = new User("a","p");
        //Test default profiles
        assertTrue(database.findUser(tempu));
        assertTrue(database.findUser(tempw));
        assertTrue(database.findUser(tempm));
        assertTrue(database.findUser(tempa));

        //Test editProfile
        profile = new UserProfile("name", "usermail.com", "123 Drive Lane", "4041230987", Title.Mr);
        user = new User(new User("user","name,","pass", AccountType.User),profile);;
        assertTrue(database.editUser(user.getUsername(), user));

        assertTrue(database.getUser("user").getProfile().getNumber().equals("4041230987"));
        assertTrue(database.getUser("user").getProfile().getEmail().equals("usermail.com"));
        assertTrue(database.getUser("user").getProfile().getAddress().equals("123 Drive Lane"));
        assertTrue(database.getUser("user").getProfile().getTitle().equals(Title.Mr));


        //Test removeUser
        assertTrue(database.removeUser(user.getUsername()));
        assertTrue(!database.findUser(user));
        userCounter--;
        assertTrue(userCounter == database.getUsers().size());

    }
}
