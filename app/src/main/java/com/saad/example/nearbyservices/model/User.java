package com.saad.example.nearbyservices.model;

/**
 * Created by Team Mashwara on 12/29/2019.
 */

    public class User  {
        public String id;
        public String username;
        public String email;
        public String pass;
        public String birthday, gender,fb_id;
        public int age;
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
            this.pass=pass;
        }
        public User(String uid,String email,String username,String birthday,String gender,String fb_id)
        {
            this.id= uid;
            this.email=email;
            this.username= username;
            this.birthday=birthday;
            this.gender= gender;
            this.fb_id=fb_id;

        }
}
