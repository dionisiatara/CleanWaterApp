package controller;

import model.AccountType;
import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import model.User;
import model.Title;

import static org.junit.Assert.*;

/**
 * Created by twalker61 on 11/9/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class UserScreenControllerTest extends GuiTest {

    private FXMLLoader fxmlLoader;
    private UserScreenController controller;
    private Label welcome;
    private Label email;
    private Label address;
    private Label contact;
    private Button quality;
    private Button hist;

    @Override
    protected Parent getRootNode() {

        Parent parent = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../view/UserScreen.fxml"));
            parent = fxmlLoader.load();
            return parent;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return parent;

    }

    @Before
    public void setup() {
        controller = fxmlLoader.getController();
        welcome = find("#welcomeMessage");
        email = find("#emailLabel");
        address = find("#addressLabel");
        contact = find("#contactLabel");
        quality = find("#submitQualityReport");
        //Button hist = find("#historyGraphButton");
    }

    /**
     * Test method for {@link controller.UserScreenController#setUser(User u)}.
     */
    @Test
    public void setUserTest() {

        //ExpectedException e = ExpectedException.none();

        User user;
        user = new User(null, "pass");
        controller.setUser(user);
        assertEquals(welcome.getText(), "Welcome");
        assertEquals(email.getText(), "Email: Edit your profile");
        assertEquals(address.getText(), "Address: Edit your profile");
        assertEquals(contact.getText(), "Contact: Edit your profile");

        user = new User("user", "pass");
        controller.setUser(user);
        assertEquals(welcome.getText(), "Welcome, user!");
        assertEquals(email.getText(), "Email: Edit your profile");
        assertEquals(address.getText(), "Address: Edit your profile");
        assertEquals(contact.getText(), "Contact: Edit your profile");

        user = new User("user", "me", "pass", null);
        //user.setName("me");
        controller.setUser(user);
        assertEquals(welcome.getText(), "Welcome, me!");
        assertEquals(email.getText(), "Email: Edit your profile");
        assertEquals(address.getText(), "Address: Edit your profile");
        assertEquals(contact.getText(), "Contact: Edit your profile");

        user = new User(new User("user", "pass"), new UserProfile("us"));
        //user.getProfile().setName("us");
        controller.setUser(user);
        assertEquals(welcome.getText(), "Welcome, us!");
        assertEquals(email.getText(), "Email: Edit your profile");
        assertEquals(address.getText(), "Address: Edit your profile");
        assertEquals(contact.getText(), "Contact: Edit your profile");

        user = new User(new User("user", "me", "pass", null), new UserProfile("us"));
        //user.setName("me");
        //user.getProfile().setName("us");
        controller.setUser(user);
        assertEquals(welcome.getText(), "Welcome, us!");
        assertEquals(email.getText(), "Email: Edit your profile");
        assertEquals(address.getText(), "Address: Edit your profile");
        assertEquals(contact.getText(), "Contact: Edit your profile");

        user = new User(new User("user", "me", "pass", null), new UserProfile("us", "Edit your profile",
                "Edit your profile", "Edit your profile", Title.Mr));
        //user.setName("me");
        //user.getProfile().setName("us");
        //user.getProfile().setTitle(Title.Mr);
        //controller.setUser(user);
        assertEquals(welcome.getText(), "Welcome, us!"); //why not Mr. me?
        assertEquals(email.getText(), "Email: Edit your profile");
        assertEquals(address.getText(), "Address: Edit your profile");
        assertEquals(contact.getText(), "Contact: Edit your profile");

        user = new User("user", "pass");
        user.setName("me");
        user.getProfile().setName("us");
        user.getProfile().setTitle(Title.Mr);
        user.getProfile().setEmail("me@me.com");
        user.getProfile().setAddress("Under a Rock");
        user.getProfile().setNumber("1112223334");
        controller.setUser(user);
        assertEquals(welcome.getText(), "Welcome, Mr. me!");
        assertEquals(email.getText(), "Email: me@me.com");
        assertEquals(address.getText(), "Address: Under a Rock");
        assertEquals(contact.getText(), "Contact: 1112223334");

        user = new User("user", "me", "pass", AccountType.User);
        controller.setUser(user);
        assertFalse(quality.isVisible());
        //assertFalse(hist.isVisible());

        user = new User("user", "me", "pass", AccountType.Worker);
        controller.setUser(user);
        assertTrue(quality.isVisible());
        //assertFalse(hist.isVisible());

        user = new User("user", "me", "pass", AccountType.Manager);
        controller.setUser(user);
        assertTrue(quality.isVisible());
        //assertTrue(hist.isVisible());

        //how to test null user?
    }
}
