package ru.made.flitter.dto;

public class AddFlitForm {

    private String userToken;
    private String content;

    public AddFlitForm() {
    }

    public AddFlitForm(String userToken, String content) {
        this.userToken = userToken;
        this.content = content;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AddFlitForm{" +
                "userToken='" + userToken + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
