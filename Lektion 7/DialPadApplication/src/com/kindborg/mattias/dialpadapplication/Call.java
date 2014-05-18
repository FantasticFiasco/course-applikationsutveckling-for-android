package com.kindborg.mattias.dialpadapplication;

public class Call {

    private final String dateTime;
    private final String number;

    public Call(String dateTime, String number) {
        this.dateTime = dateTime;
        this.number = number;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", number, dateTime);
    }
}