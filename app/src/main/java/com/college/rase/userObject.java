package com.college.rase;

public class userObject {
    private String chatroomId , userEmail, profilePicture , userName;

    public userObject(String chatroomId , String userEmail){
        this.chatroomId = chatroomId;
        this.userEmail = userEmail;

    }

    public String getChatroomId() { return chatroomId; }

    public String getUserEmail(){
        return userEmail;
    }

    public String getProfilePicture() { return profilePicture; }

    public String getUserName() { return userName; }

    public void setUserName(String userName){ this.userName = userName; }

    public void setProfilePicture(String profilePicture){ this.profilePicture = profilePicture; }
}
