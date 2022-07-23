package com.example.capstone_devwolfs_get_a_pet.classes;

public class Adopter {

    private String adopterName;
    private String adopterEmail;
    private String adopterPhone;
    private String adopterAddress;
    private String adopterDescription;
    private String adopterPassword;
    private String adopterImage;

    public Adopter(String adopterName, String adopterEmail, String adopterPhone, String adopterAddress, String adopterDescription, String adopterPassword, String adopterImage) {
        this.adopterName = adopterName;
        this.adopterEmail = adopterEmail;
        this.adopterPhone = adopterPhone;
        this.adopterAddress = adopterAddress;
        this.adopterDescription = adopterDescription;
        this.adopterPassword = adopterPassword;
        this.adopterImage = adopterImage;
    }

    public String getAdopterName() {
        return adopterName;
    }

    public void setAdopterName(String adopterName) {
        this.adopterName = adopterName;
    }

    public String getAdopterEmail() {
        return adopterEmail;
    }

    public void setAdopterEmail(String adopterEmail) {
        this.adopterEmail = adopterEmail;
    }

    public String getAdopterPhone() {
        return adopterPhone;
    }

    public void setAdopterPhone(String adopterPhone) {
        this.adopterPhone = adopterPhone;
    }

    public String getAdopterAddress() {
        return adopterAddress;
    }

    public void setAdopterAddress(String adopterAddress) {
        this.adopterAddress = adopterAddress;
    }

    public String getAdopterDescription() {
        return adopterDescription;
    }

    public void setAdopterDescription(String adopterDescription) {
        this.adopterDescription = adopterDescription;
    }

    public String getAdopterPassword() {
        return adopterPassword;
    }

    public void setAdopterPassword(String adopterPassword) {
        this.adopterPassword = adopterPassword;
    }

    public String getAdopterImage() {
        return adopterImage;
    }

    public void setAdopterImage(String adopterName) {
        this.adopterImage = adopterImage;
    }
}
