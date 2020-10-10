package ru.made.flitter.dto;

public class SubscribeForm {

    private String subscriberToken;
    private String publisherName;

    public SubscribeForm() {
    }

    public SubscribeForm(String subscriberToken, String publisherName) {
        this.subscriberToken = subscriberToken;
        this.publisherName = publisherName;
    }

    public String getSubscriberToken() {
        return subscriberToken;
    }

    public void setSubscriberToken(String subscriberToken) {
        this.subscriberToken = subscriberToken;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    @Override
    public String toString() {
        return "SubscribeForm{" +
                "subscriberToken='" + subscriberToken + '\'' +
                ", publisherName='" + publisherName + '\'' +
                '}';
    }
}
