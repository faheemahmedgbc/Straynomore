/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This activity is forum initialisation which helps in creating/retrieving forum info */
package com.example.straynomore;

public class ForumHelper {
    String title, message, uid;

    public ForumHelper(){}

    public ForumHelper(String title, String message, String uid) {
        this.title = title;
        this.message = message;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
