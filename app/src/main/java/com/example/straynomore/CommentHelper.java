package com.example.straynomore;

import java.util.Date;
// this class is for the functionality of the comment feature on the app.
// It has its constructor with the two values ( name,content)
// Contains all necessary getters and setters for these values
public class CommentHelper {
    private String name, comment;
    private Date date;

    public CommentHelper()
    {}

    public CommentHelper(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
