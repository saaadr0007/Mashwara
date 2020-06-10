package com.saad.example.nearbyservices.model;

import android.widget.TextView;

/**
 * Created by Team Mashwara on 12/29/2019.
 */

    public class User  {
        public String id;
        public String username;
        public String email;
        public String pass;
        public String birthday, gender,fb_id;
        public String age;
        public User()
        {

        }
        public User(String Email, String Pass)
        {
            this.email=Email;
            this.pass=Pass;
        }
        public User(String uid, String email,String username)
        {
            this.id= uid;
            this.email=email;
            this.username= username;

        }
        public User(String uid,String email,String username,String birthday,String gender,String fb_id,String age)
        {
            this.id= uid;
            this.email=email;
            this.username= username;
            this.birthday=birthday;
            this.gender= gender;
            this.fb_id=fb_id;
            this.age=age;
        }

    public User(String usrname, String email, String birthday, String age) {
        this.username= usrname;
        this.email=email;
        this.birthday= birthday;
        this.age=age;

    }


    public String getEmail() {
        return email;
    }

    public String getFb_id() {
        return fb_id;
    }

    public String getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }

}
