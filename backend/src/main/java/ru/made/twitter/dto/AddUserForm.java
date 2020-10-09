package ru.made.twitter.dto;

public class AddUserForm {

    private String userName;

    public AddUserForm() {
    }

    public AddUserForm(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "AddUserForm{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
