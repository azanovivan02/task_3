package ru.made.twitter.dto;

public class Tweet {

    private String userName;
    private String content;

    public Tweet() {
    }

    public Tweet(String userName, String content) {
        this.userName = userName;
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
