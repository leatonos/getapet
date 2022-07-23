package com.example.capstone_devwolfs_get_a_pet.classes;

public class Pet {

    private String petName;
    private String shelterId;
    private String breed;
    private String type;
    private String size;
    private String description;
    private String petImage;

    public Pet(String petName, String shelterId, String breed, String type, String size, String description,String petImage){
        this.petName = petName;
        this.shelterId = shelterId;
        this.breed = breed;
        this.type = type;
        this.size = size;
        this.description = description;
        this.petImage =  petImage;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getShelterId() {
        return shelterId;
    }

    public void setShelterId(String shelterId) {
        this.shelterId = shelterId;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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
}
