package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.AccountType;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;

/**
 * Created by dionisiatara on 11/14/16.
 */
public class RegistrationScreenControllerTest extends GuiTest {
    private FXMLLoader fxmlLoader;
    private RegistrationScreenController controller;
    private TextField name;
    private TextField username;
    private TextField password;
    private ComboBox<AccountType> type;

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../view/RegistrationScreen.fxml"));
            parent = fxmlLoader.load();
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    @Before
    public void setup() {
        controller = fxmlLoader.getController();
        name = find("#registrationName");
        username = find("#registrationUsername");
        password = find("#registrationPassword");
        type = find("#accountTypeBox");
    }

    /**
     * Test method for {@link RegistrationScreenController#isValidUser()}
     */
    @Test(expected = Exception.class)
    public void blankInputTest() {
        // test if all inputs are blanks
        assertEquals(0, name.getText().length());
        assertEquals(0, username.getText().length());
        assertEquals(0, password.getText().length());
        assertNull(type.getValue());
        assertFalse(controller.isValidUser());
    }

    /**
     * Test method for {@link RegistrationScreenController#isValidUser()}
     */
    @Test(expected = Exception.class)
    public void missingNameTest() {
        // test if missing name field
        username.setText("abcd");
        verifyThat("#registrationUsername", hasText("abcd"));
        password.setText("1234");
        verifyThat("#registrationPassword", hasText("1234"));
        //type.setValue(AccountType.Admin);
        //assertEquals(AccountType.Admin, type.getValue());
        assertEquals(0, name.getText().length());
        assertFalse(controller.isValidUser());
    }

    /**
     * Test method for {@link RegistrationScreenController#isValidUser()}
     */
    @Test(expected = Exception.class)
    public void slashInputTest() {
        // test if input contains "/"
        name.setText("Jo/hn");
        verifyThat("#registrationName", hasText("Jo/hn"));
        assertTrue(name.getText().contains("/"));
        username.setText("abcd");
        verifyThat("#registrationUsername", hasText("abcd"));
        password.setText("1234");
        verifyThat("#registrationPassword", hasText("1234"));
        assertFalse(controller.isValidUser());
    }

    /**
     * Test method for {@link RegistrationScreenController#isValidUser()}
     */
    @Test(expected = Exception.class)
    public void validInputTest() {
        // test if all inputs are valid
        name.setText("John");
        verifyThat("#registrationName", hasText("John"));
        assertFalse(name.getText().contains("/"));
        username.setText("abcd");
        verifyThat("#registrationUsername", hasText("abcd"));
        password.setText("1234");
        verifyThat("#registrationPassword", hasText("1234"));
        assertTrue(controller.isValidUser());
    }
}
