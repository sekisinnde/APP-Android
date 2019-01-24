package com.example.oumar.topquiz.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

public class User implements Comparator<User>, Comparable<User>, Serializable{
    private String mFirstname;
    private Integer mScore;

    public User() {
    }

    public User(String firstname, int score) {
        mFirstname = firstname;
        mScore = score;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String firstname) {
        mFirstname = firstname;
    }

    public Integer getScore() {
        return mScore;
    }

    public void setScore(Integer score) {
        mScore = score;
    }

    @Override
    public String toString() {
        return "User{" +
                "mFirstname='" + mFirstname + '\'' +
                ", mScore=" + mScore +
                '}';
    }

    @Override
    public int compare(User user1, User user2) {

        int result;
        result = user1.getScore().compareTo(user2.getScore());
        if (result == 0) result = user1.getFirstname().compareTo(user2.getFirstname());

        return result;
    }

    @Override
    public int compareTo(@NonNull User user) {
        return this.mFirstname.compareTo(user.getFirstname());
    }
}