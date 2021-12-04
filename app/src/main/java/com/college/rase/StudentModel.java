package com.college.rase;

public class StudentModel {
    private String profilePicture;
    private String name;
    private String yop;
    private String itemId;
    private String currentCompany;
    private String token;

    public StudentModel(String profilePicture , String name , String yop , String itemId , String currentCompany , String token){
        this.profilePicture=profilePicture;
        this.name=name;
        this.yop=yop;
        this.itemId=itemId;
        this.currentCompany=currentCompany;
        this.token=token;
    }
    public StudentModel() {
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYop() {
        return yop;
    }

    public void setYop(String yop) {
        this.yop = yop;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
