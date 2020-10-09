package ru.made.flitter.dto;

public class User {

    private String userName;
    private String userToken;

    public User() {
    }

    public User(String userName, String userToken) {
        this.userName = userName;
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userToken='" + userToken + '\'' +
                '}';
    }
}
