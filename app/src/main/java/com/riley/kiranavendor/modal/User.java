package com.riley.kiranavendor.modal;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String uid;
    public String phonenumber;
    public String firstname;
    public String lastname;
    public String age;
    public  String account_type;
    public String status;
    ///public  String verif;

    public User() {
        // Default constructor
    }

    public User (String uid, String phonenumber, String firstname, String lastname, String age,String account_type, String status) {
        this.uid = uid;
        this.phonenumber = phonenumber;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.account_type = account_type;
        this.status = status;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }
}
