package sample.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;


import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.server.DatabaseController;
import sample.shared.Message;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Formatter;

public class LoginController {
    @FXML
    private Button signInBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text actiontarget;

    public LoginController() {
    }

    @FXML
    void handleSubmitButtonAction(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException {

        DatabaseController databaseController = DatabaseController.getInstance();
        byte[] passwordInBytes = passwordField.getText().getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        String passwordHash = byteArray2Hex(md.digest(passwordInBytes));
        if(databaseController.verifyUser(usernameField.getText(),passwordHash)){
            Parent tableViewParent = FXMLLoader.load(getClass().getResource("client.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);

            //This line gets the Stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(tableViewScene);
            window.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Oops you have entered invalid username or password");
            alert.show();
        }



    }

    private String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

}
