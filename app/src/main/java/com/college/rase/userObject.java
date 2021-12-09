package com.college.rase;

public class userObject {
    private String chatroomId , userNo, profilePicture , userName;
    private Boolean isOnline;
    public userObject(String chatroomId , String userNo){
        this.chatroomId = chatroomId;
        this.userNo = userNo;
        this.isOnline = false;

    }

    public String getChatroomId() { return chatroomId; }

    public  void setIsOnline(Boolean isOnline){ this.isOnline = isOnline;}

    public Boolean getIsOnline(){ return this.isOnline;}

    public String getUserNo(){
        return userNo;
    }

    public String getProfilePicture() { return profilePicture; }

    public String getUserName() { return userName; }

    public void setUserName(String userName){ this.userName = userName; }

    public void setProfilePicture(String profilePicture){ this.profilePicture = profilePicture; }
}
