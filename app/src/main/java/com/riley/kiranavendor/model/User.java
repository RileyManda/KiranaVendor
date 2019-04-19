package com.riley.kiranavendor.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String uid;
    public String phonenumber;
    public String firstname;
    public String lastname;
    public String age;
    public String status;
    ///public  String verif;

    public User() {
        // Default constructor
    }

    public User(String uid, String phonenumber, String firstname, String lastname, String age, String status) {
        this.uid = uid;
        this.phonenumber = phonenumber;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.status = status;
    }
}
