package ru.made.twitter.dto;

public class AddTweetForm {

    private String userToken;
    private String content;

    public AddTweetForm() {
    }

    public AddTweetForm(String userToken, String content) {
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
        return "AddTweetForm{" +
                "userToken='" + userToken + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
