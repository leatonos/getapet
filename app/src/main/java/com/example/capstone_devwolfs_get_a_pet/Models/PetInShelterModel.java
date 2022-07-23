package com.example.capstone_devwolfs_get_a_pet.Models;

import com.google.firebase.firestore.DocumentId;

public class PetInShelterModel {

    private String petID;
    private String petName;
    private String breed;
    private String description;
    private String petImage;
    private String size;
    private String type;
    private String shelterId;

    public PetInShelterModel(String petID, String petName, String breed, String description, String petImage, String size, String type, String shelterId) {
        this.petID = petID;
        this.petName = petName;
        this.breed = breed;
        this.description = description;
        this.petImage = petImage;
        this.size = size;
        this.type = type;
        this.shelterId = shelterId;
    }

    public PetInShelterModel() {

    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPetImage() {
        return petImage;
    }

    public void setPetImage(String petImage) {
        this.petImage = petImage;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPetName(){
        return petName;
    }
    public void setPetName(String petName){
        this.petName = petName;
    }

    @DocumentId
    public String getPetID() {
        return petID;
    }

    public String getShelterId() {
        return shelterId;
    }

    public void setShelterId(String shelterId) {
        this.shelterId = shelterId;
    }
}
