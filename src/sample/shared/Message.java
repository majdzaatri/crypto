package sample.shared;

import java.io.Serializable;

public class Message implements Serializable {

    private String encryptedMessage;

    private String digitalSignature;

    private String imageDigitalSignature;

    private String user;

    private boolean isVerified = false;

    private byte[] cipherImage;




    String getEncryptedMessage() {
        return encryptedMessage;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    @Override
    public String toString() {
        System.out.println( "********** message **********");
        System.out.println("enc msg " + getEncryptedMessage());
        System.out.println("dig sign " + getDigitalSignature());
        return super.toString();
    }

    public String getImageDigitalSignature() {
        return imageDigitalSignature;
    }

    public void setImageDigitalSignature(String imageDigitalSignature) {
        this.imageDigitalSignature = imageDigitalSignature;
    }


    public byte[] getCipherImage() {
        return cipherImage;
    }

    public void setCipherImage(byte[] cipherImage) {
        this.cipherImage = cipherImage;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }
}
