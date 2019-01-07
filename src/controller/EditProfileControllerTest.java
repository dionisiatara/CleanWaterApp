package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Title;
import model.User;
import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import java.io.IOException;
import static org.junit.Assert.*;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;

/**
 * Created by Quang on 11/11/16.
 * Tests the input for isValidProfileEdit method in EditProfileController
 * Currently does not test further through database saving method due to inability to access remote database.
 */
public class EditProfileControllerTest extends GuiTest {

    private FXMLLoader fxmlLoader;
    private EditProfileController controller;
    private TextField name;
    private TextField email;
    private TextField address;
    private TextField number;
    private ComboBox<Title> title;

    @Override
    protected Parent getRootNode() {
        Parent root = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../view/EditProfile.fxml"));
            root = fxmlLoader.load();
            return root;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;

    }

    @Before
    public void setup() {
        controller = fxmlLoader.getController();
        name = find("#profileName");
        email = find("#profileEmail");
        address = find("#profileAddress");
        number = find("#profileContact");
        title = find("#profileTitle");
    }

    /**
     * Test method for {@link EditProfileController#isValidProfileEdit()}.
     */
    @Test
    public void ValidEditTest() throws IOException {
        //Testing null profile
        UserProfile profile = new UserProfile(null, null, null, null, null);
        User user = new User(new User(null, null, null, null), profile);

        controller.setUser(user);
        assertNull(name.getText());
        assertNull(email.getText());
        assertNull(address.getText());
        assertNull(number.getText());
        assertNull(title.getValue());
        assertEquals(controller.isValidProfileEdit(), "ERROR/Invalid Profile Edit/" +
                "Please enter a valid name!\n" +
                "Please enter a valid email!\n" +
                "Please enter a valid address!\n" +
                "Please enter a valid contact number!\n" +
                "You have selected an invalid title.\n");

        //Testing blank profile
        profile = new UserProfile("", "", "", "");
        user = new User(new User("",""), profile);

        controller.setUser(user);
        assertEquals("Blank profile name not correct.", "", name.getText());
        assertEquals("Blank profile email not correct.", "", email.getText());
        assertEquals("Blank profile address not correct.", "", address.getText());
        assertEquals("Blank profile number not correct.", "", number.getText());
        assertEquals("Blank profile edit did not return correct error message.", "ERROR/Invalid Profile Edit/" +
                "Please enter a valid name!\n" +
                "Please enter a valid email!\n" +
                "Please enter a valid address!\n" +
                "Please enter a valid contact number!\n" +
                 "You have selected an invalid title.\n",
                controller.isValidProfileEdit());

        //Testing null title
        profile = new UserProfile("name", "user@mail.com", "123 Drive Lane", "4041230987");
        user = new User(new User("user","pass"), profile);

        controller.setUser(user);
        assertNull(profile.getTitle());
        assertNull(title.getValue());
        assertEquals(controller.isValidProfileEdit(), "ERROR/Invalid Profile Edit/" +
                "You have selected an invalid title.\n");


        //Testing valid user
        profile = new UserProfile("name", "user@mail.com", "123 Drive Lane", "4041230987");
        user = new User(new User("user","pass"), profile);

        controller.setUser(user);
        assertEquals("Name of valid user is incorrect.", "name", name.getText());
        assertEquals("Email of valid user is incorrect.", "user@mail.com", email.getText());
        assertEquals("Address of valid user is incorrect.", "123 Drive Lane", address.getText());
        assertEquals("Number of valid user is incorrect.", "4041230987", number.getText());
        verifyThat("#profileName", hasText("name"));
        verifyThat("#profileEmail", hasText("user@mail.com"));
        verifyThat("#profileAddress", hasText("123 Drive Lane"));
        verifyThat("#profileContact", hasText("4041230987"));
        assertEquals("All valid data except Title Error", "ERROR/Invalid Profile Edit/You have selected an invalid title.\n",
                controller.isValidProfileEdit());


        //Testing missing @ symbol in email field
        profile = new UserProfile("name", "usermail.com", "123 Drive Lane", "4041230987", Title.Mr);
        user = new User(new User("user","pass"), profile);

        controller.setUser(user);
        verifyThat("#profileName", hasText("name"));
        verifyThat("#profileEmail", hasText("usermail.com"));
        verifyThat("#profileAddress", hasText("123 Drive Lane"));
        verifyThat("#profileContact", hasText("4041230987"));
        assertEquals("Invalid Email Profile Edit Error","ERROR/Invalid Profile Edit/Please enter a valid email!\n" +
                        "You have selected an invalid title.\n",
                controller.isValidProfileEdit());

        //Testing invalid profiles containing "/"
        profile = new UserProfile("na/me", "user@ma/il.com", "123 Dr/ive Lane", "404/1230987", Title.Dr);
        user = new User(new User("us/er","pas/s"), profile);

        controller.setUser(user);
        assertTrue(name.getText().contains("/"));
        assertTrue(email.getText().contains("/"));
        assertTrue(address.getText().contains("/"));
        assertTrue(number.getText().contains("/"));
        assertEquals(controller.isValidProfileEdit(), "ERROR/Invalid Profile Edit/" +
                "Please enter a valid name!\n" +
                "Please enter a valid email!\n" +
                "Please enter a valid address!\n" +
                "Please enter a valid contact number!\n" +
                "You have selected an invalid title.\n");

    }


}
