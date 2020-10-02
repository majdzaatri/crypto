package sample.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sample.server.DatabaseController;
import sample.shared.Message;
import sample.shared.PublicKeys;
import sample.shared.RSA;
import sample.shared.TEA;

import sample.shared.ServerConnectionEvents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Controller extends ServerConnectionEvents{

    private RSA rsa;

    private TEA tea;

    private Client client;

    private PublicKeys serverPublicKey;

    private String TEAkey;

    @FXML
    TextArea message_box;

    @FXML
    TextArea message_list;

    @FXML
    TextArea public_key;

    @FXML
    TextArea public_exponent;



    public Controller() {

        rsa = new RSA(2048);
        TEAkey = "And is there honey still for tea?";
        tea = new TEA(TEAkey.getBytes());
        client = new Client("127.0.0.1", 8686, this);

    }

    public void initialize() {
        public_key.setText(rsa.getModulus().toString());
        public_key.setWrapText(true);
        public_exponent.setText(rsa.getPublicKey().toString());

        client.setPublicKeys(getPublicKeys(rsa));

        client.start();
    }

    public void connectServer() {


    }

    public void sendMessage() throws IOException, NoSuchAlgorithmException {
        //Send the symmetric key in order for the server to decrypt the image

        File file = new File(message_box.getText());
        if(file.exists()){

            Message message = encrypt(rsa, TEAkey, serverPublicKey);
            System.out.println(DatabaseController.getUsername());

            //sign the image and save it in message
            message.setUser(DatabaseController.getUsername());
            message.setImageDigitalSignature(signImage(rsa, message_box.getText()));

            //send the encrypted image
            byte[] cipherImage = encryptWithTea(tea,message_box);
            message.setCipherImage(cipherImage);
            client.sendMessage(message);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File not found");
            alert.show();

        }
    }

    void onMessageArrived(Message message) throws SQLException, NoSuchAlgorithmException {

        messageArrived(message, rsa, serverPublicKey, message_list);

    }

    void onStringMessageArrived(String message) throws SQLException, NoSuchAlgorithmException {

        message_list.appendText(message + "\n");

    }

    @Override
    public void onPublicKeyArrived(PublicKeys serverPublicKey) {
        this.serverPublicKey = serverPublicKey;
    }
}



